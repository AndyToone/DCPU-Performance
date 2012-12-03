package org.notch.dcpu;

import java.util.ArrayList;

import com.feertech.dcpu.common.BaseDCPU;
import com.feertech.dcpu.common.RunnableDCPU;

public class NotchsDCPU extends BaseDCPU implements RunnableDCPU {

    public char ram[];
    public char pc;
    public char sp;
    public char ex;
    public char in;
    public char registers[];
    public int cycles;
    public ArrayList<DCPUHardware> hardware;
    private static volatile boolean stop = false;
    private boolean isSkipping;
    private char interrupts[];
    private int ip;
    private int iwp;

    public NotchsDCPU() {
        ram = new char[65536];
        registers = new char[8];
        hardware = new ArrayList<>();
        isSkipping = false;
        interrupts = new char[256];
    }

    @Override
    public long runEmulation() {
        while (!stop) {
            tick();
            cycles++;
        }

        return cycles;
    }

    @Override
    public void stop() {
        stop = true;
    }

    @Override
    public void reset() {
        cycles = 0;
        pc = 0;
        stop = false;
    }

    @Override
    public void setWord(int address, int value) {
        ram[pc] = (char) value;
    }

    public int getAddrB(int type) {
        switch (type & 248) {
        case 0: // '\0'
            return 65536 + (type & 7);

        case 8: // '\b'
            return registers[type & 7];

        case 16: // '\020'
            cycles++;
            return ram[pc++] + registers[type & 7] & 65535;

        case 24: // '\030'
            switch (type & 7) {
            case 0: // '\0'
                return --sp & 65535;

            case 1: // '\001'
                return sp & 65535;

            case 2: // '\002'
                cycles++;
                return ram[pc++] + sp & 65535;

            case 3: // '\003'
                return 65544;

            case 4: // '\004'
                return 65545;

            case 5: // '\005'
                return 65552;

            case 6: // '\006'
                cycles++;
                return ram[pc++];
            }
            cycles++;
            return 131072 | ram[pc++];
        }
        throw new IllegalStateException((new StringBuilder(
                "Illegal a value type ")).append(Integer.toHexString(type))
                .append("! How did you manage that!?").toString());
    }

    public String getStr(int type, boolean isA) {
        if (type >= 32)
            return Integer.toHexString((type & 31) + 65535 & 65535);
        switch (type & 248) {
        case 0: // '\0'
            return (new StringBuilder()).append("ABCXYZIJ".charAt(type & 7))
                    .toString();

        case 8: // '\b'
            return (new StringBuilder("[")).append("ABCXYZIJ".charAt(type & 7))
                    .append("]").toString();

        case 16: // '\020'
            return (new StringBuilder("["))
                    .append(Integer.toHexString(ram[pc++])).append("+")
                    .append("ABCXYZIJ".charAt(type & 7)).append("]").toString();

        case 24: // '\030'
            switch (type & 7) {
            case 0: // '\0'
                return isA ? "POP" : "PUSH";

            case 1: // '\001'
                return "PEEK";

            case 2: // '\002'
                return (new StringBuilder("["))
                        .append(Integer.toHexString(ram[pc++])).append("+SP]")
                        .toString();

            case 3: // '\003'
                return "SP";

            case 4: // '\004'
                return "PC";

            case 5: // '\005'
                return "EX";

            case 6: // '\006'
                return (new StringBuilder("["))
                        .append(Integer.toHexString(ram[pc++])).append("]")
                        .toString();
            }
            return Integer.toHexString(ram[pc++]);
        }
        throw new IllegalStateException((new StringBuilder(
                "Illegal value type ")).append(Integer.toHexString(type))
                .append("! How did you manage that!?").toString());
    }

    public int getAddrA(int type) {
        if (type >= 32)
            return 131072 | (type & 31) + 65535 & 65535;
        switch (type & 248) {
        case 0: // '\0'
            return 65536 + (type & 7);

        case 8: // '\b'
            return registers[type & 7];

        case 16: // '\020'
            cycles++;
            return ram[pc++] + registers[type & 7] & 65535;

        case 24: // '\030'
            switch (type & 7) {
            case 0: // '\0'
                return sp++ & 65535;

            case 1: // '\001'
                return sp & 65535;

            case 2: // '\002'
                cycles++;
                return ram[pc++] + sp & 65535;

            case 3: // '\003'
                return 65544;

            case 4: // '\004'
                return 65545;

            case 5: // '\005'
                return 65552;

            case 6: // '\006'
                cycles++;
                return ram[pc++];
            }
            cycles++;
            return 131072 | ram[pc++];
        }
        throw new IllegalStateException((new StringBuilder(
                "Illegal a value type ")).append(Integer.toHexString(type))
                .append("! How did you manage that!?").toString());
    }

    public char getValA(int type) {
        if (type >= 32)
            return (char) ((type & 31) + 65535);
        switch (type & 248) {
        case 0: // '\0'
            return registers[type & 7];

        case 8: // '\b'
            return ram[registers[type & 7]];

        case 16: // '\020'
            cycles++;
            return ram[ram[pc++] + registers[type & 7] & 65535];

        case 24: // '\030'
            switch (type & 7) {
            case 0: // '\0'
                return ram[sp++ & 65535];

            case 1: // '\001'
                return ram[sp & 65535];

            case 2: // '\002'
                cycles++;
                return ram[ram[pc++] + sp & 65535];

            case 3: // '\003'
                return sp;

            case 4: // '\004'
                return pc;

            case 5: // '\005'
                return ex;

            case 6: // '\006'
                cycles++;
                return ram[ram[pc++]];
            }
            cycles++;
            return ram[pc++];
        }
        throw new IllegalStateException((new StringBuilder(
                "Illegal a value type ")).append(Integer.toHexString(type))
                .append("! How did you manage that!?").toString());
    }

    public char get(int addr) {
        if (addr < 65536)
            return ram[addr & 65535];
        if (addr < 65544)
            return registers[addr & 7];
        if (addr >= 131072)
            return (char) addr;
        if (addr == 65544)
            return sp;
        if (addr == 65545)
            return pc;
        if (addr == 65552)
            return ex;
        else
            throw new IllegalStateException((new StringBuilder(
                    "Illegal address ")).append(Integer.toHexString(addr))
                    .append("! How did you manage that!?").toString());
    }

    public void set(int addr, char val) {
        if (addr < 65536)
            ram[addr & 65535] = val;
        else if (addr < 65544)
            registers[addr & 7] = val;
        else if (addr < 131072)
            if (addr == 65544)
                sp = val;
            else if (addr == 65545)
                pc = val;
            else if (addr == 65552)
                ex = val;
            else
                throw new IllegalStateException((new StringBuilder(
                        "Illegal address ")).append(Integer.toHexString(addr))
                        .append("! How did you manage that!?").toString());
    }

    public static int getInstructionLength(char opcode) {
        int len = 1;
        int cmd = opcode & 31;
        if (cmd == 0) {
            cmd = opcode >> 5 & 31;
            if (cmd > 0) {
                int atype = opcode >> 10 & 63;
                if ((atype & 248) == 16 || atype == 31 || atype == 30)
                    len++;
            }
        } else {
            int atype = opcode >> 5 & 31;
            int btype = opcode >> 10 & 63;
            if ((atype & 248) == 16 || atype == 31 || atype == 30)
                len++;
            if ((btype & 248) == 16 || btype == 31 || btype == 30)
                len++;
        }
        return len;
    }

    public void skip() {
        isSkipping = true;
    }

    public void tick() {
        cycles++;
        char opcode;
        int cmd;
        if (isSkipping) {
            opcode = ram[pc];
            cmd = opcode & 31;
            pc += getInstructionLength(opcode);
            if (cmd >= 16 && cmd <= 23)
                isSkipping = true;
            else
                isSkipping = false;
            return;
        }
        if (ip != iwp) {
            char a = interrupts[ip = ip + 1 & 255];
            if (in > 0) {
                ram[--sp & 65535] = pc;
                ram[--sp & 65535] = registers[0];
                registers[0] = a;
                pc = in;
            }
        }
        char a = ram[pc++];
        cmd = a & 31;
        if (cmd == 0) {
            cmd = a >> 5 & 31;
            if (cmd != 0) {
                int atype = a >> 10 & 63;
                int aaddr = getAddrA(atype);
                a = get(aaddr);
                switch (cmd) {
                case 1: // '\001'
                    cycles += 2;
                    ram[--sp & 65535] = pc;
                    pc = a;
                    break;

                case 7: // '\007'
                    cycles += 8;
                    break;

                case 8: // '\b'
                    cycles += 3;
                    interrupt(a);
                    break;

                case 9: // '\t'
                    set(aaddr, in);
                    break;

                case 10: // '\n'
                    in = a;
                    break;

                case 16: // '\020'
                    cycles++;
                    set(aaddr, (char) hardware.size());
                    break;

                case 17: // '\021'
                    cycles += 3;
                    if (a >= 0 && a < hardware.size())
                        ((DCPUHardware) hardware.get(a)).query();
                    break;

                case 18: // '\022'
                    cycles += 3;
                    if (a >= 0 && a < hardware.size())
                        ((DCPUHardware) hardware.get(a)).interrupt();
                    break;
                }
            }
        } else {
            int atype = a >> 10 & 63;
            a = getValA(atype);
            int btype = a >> 5 & 31;
            int baddr = getAddrB(btype);
            char b = get(baddr);
            switch (cmd) {
            case 24: // '\030'
            case 25: // '\031'
            default:
                break;

            case 1: // '\001'
            {
                b = a;
                break;
            }

            case 2: // '\002'
            {
                cycles++;
                int val = b + a;
                b = (char) val;
                ex = (char) (val >> 16);
                break;
            }

            case 3: // '\003'
            {
                cycles++;
                int val = b - a;
                b = (char) val;
                ex = (char) (val >> 16);
                break;
            }

            case 4: // '\004'
            {
                cycles++;
                int val = b * a;
                b = (char) val;
                ex = (char) (val >> 16);
                break;
            }

            case 5: // '\005'
            {
                cycles++;
                int val = (short) b * (short) a;
                b = (char) val;
                ex = (char) (val >> 16);
                break;
            }

            case 6: // '\006'
            {
                cycles += 2;
                if (a == 0) {
                    b = ex = '\0';
                } else {
                    long val = ((long) b << 16) / (long) a;
                    b = (char) (int) (val >> 16);
                    ex = (char) (int) val;
                }
                break;
            }

            case 7: // '\007'
            {
                cycles += 2;
                if (a == 0) {
                    b = ex = '\0';
                } else {
                    long val = ((long) (short) b << 16) / (long) (short) a;
                    b = (char) (int) (val >> 16);
                    ex = (char) (int) val;
                }
                break;
            }

            case 8: // '\b'
            {
                cycles += 2;
                if (a == 0)
                    b = '\0';
                else
                    b %= a;
                break;
            }

            case 9: // '\t'
            {
                b &= a;
                break;
            }

            case 10: // '\n'
            {
                b |= a;
                break;
            }

            case 11: // '\013'
            {
                b ^= a;
                break;
            }

            case 12: // '\f'
            {
                cycles++;
                ex = (char) ((b << 16) >> a);
                b >>>= a;
                break;
            }

            case 13: // '\r'
            {
                cycles++;
                ex = (char) ((b << 16) >>> a);
                b >>= a;
                break;
            }

            case 14: // '\016'
            {
                cycles++;
                ex = (char) ((b << a) >> 16);
                b <<= a;
                break;
            }

            case 15: // '\017'
            {
                b = a;
                registers[6]++;
                registers[7]++;
                break;
            }

            case 16: // '\020'
            {
                cycles++;
                if ((b & a) == 0)
                    skip();
                return;
            }

            case 17: // '\021'
            {
                cycles++;
                if ((b & a) != 0)
                    skip();
                return;
            }

            case 18: // '\022'
            {
                cycles++;
                if (b != a)
                    skip();
                return;
            }

            case 19: // '\023'
            {
                cycles++;
                if (b == a)
                    skip();
                return;
            }

            case 20: // '\024'
            {
                cycles++;
                if (b <= a)
                    skip();
                return;
            }

            case 21: // '\025'
            {
                cycles++;
                if ((short) b <= (short) a)
                    skip();
                return;
            }

            case 22: // '\026'
            {
                cycles++;
                if (b >= a)
                    skip();
                return;
            }

            case 23: // '\027'
            {
                cycles++;
                if ((short) b >= (short) a)
                    skip();
                return;
            }

            case 26: // '\032'
            {
                cycles++;
                int val = b + a + ex;
                b = (char) val;
                ex = (char) (val >> 16);
                break;
            }

            case 27: // '\033'
            {
                cycles++;
                int val = (b - a) + ex;
                b = (char) val;
                ex = (char) (val >> 16);
                break;
            }
            }
            set(baddr, b);
        }
    }

    public void interrupt(char a) {
        interrupts[iwp = iwp + 1 & 255] = a;
    }

    public void tickHardware() {
        for (int i = 0; i < hardware.size(); i++)
            ((DCPUHardware) hardware.get(i)).tick60hz();

    }

}
