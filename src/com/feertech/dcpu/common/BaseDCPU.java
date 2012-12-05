package com.feertech.dcpu.common;

public class BaseDCPU {

    public static final int MEMORY_LENGTH = 65536;
    
    public static final char SET = 0x01; // 1 | 0x01 | SET b, a | sets b to a
    public static final char ADD = 0x02; // 2 | 0x02 | ADD b, a | sets b to b+a, sets EX to 0x0001 if there's an overflow, x0 otherwise
    public static final char SUB = 0x03; // 2 | 0x03 | SUB b, a | sets b to b-a, sets EX to 0xffff if there's an underflow, 0x0 otherwise
    public static final char MUL = 0x04; // 2 | 0x04 | MUL b, a | sets b to b*a, sets EX to ((b*a)>>16)&0xffff (treats b, a as unsigned)
    public static final char MLI = 0x05; // 2 | 0x05 | MLI b, a | like MUL, but treat b, a as signed
    public static final char DIV = 0x06; // 3 | 0x06 | DIV b, a | sets b to b/a, sets EX to ((b<<16)/a)&0xffff. if a==0, sets b and EX to 0 instead. (treats b, a as unsigned)
    public static final char DVI = 0x07; // 3 | 0x07 | DVI b, a | like DIV, but treat b, a as signed. Rounds towards 0
    public static final char MOD = 0x08; // 3 | 0x08 | MOD b, a | sets b to b%a. if a==0, sets b to 0 instead.
               
    public static final char MDI = 0x09; // 3 | 0x09 | MDI b, a | like MOD, but treat b, a as signed. (MDI -7, 16 == -7)
    public static final char AND = 0x0a; // 1 | 0x0a | AND b, a | sets b to b&a
    public static final char BOR = 0x0b; // 1 | 0x0b | BOR b, a | sets b to b|a
    public static final char XOR = 0x0c; // 1 | 0x0c | XOR b, a | sets b to b^a
    public static final char SHR = 0x0d; // 1 | 0x0d | SHR b, a | sets b to b>>>a, sets EX to ((b<<16)>>a)&0xffff (logical shift)
    public static final char ASR = 0x0e; // 1 | 0x0e | ASR b, a | sets b to b>>a, sets EX to ((b<<16)>>>a)&0xffff (arithmetic shift) (treats b as signed)
    public static final char SHL = 0x0f; // 1 | 0x0f | SHL b, a | sets b to b<<a, sets EX to ((b<<a)>>16)&0xffff

    public static final char IFB = 0x10; // 2+| 0x10 | IFB b, a | performs next instruction only if (b&a)!=0
    public static final char IFC = 0x11; // 2+| 0x11 | IFC b, a | performs next instruction only if (b&a)==0
    public static final char IFE = 0x12; // 2+| 0x12 | IFE b, a | performs next instruction only if b==a 
    public static final char IFN = 0x13; // 2+| 0x13 | IFN b, a | performs next instruction only if b!=a 
    public static final char IFG = 0x14; // 2+| 0x14 | IFG b, a | performs next instruction only if b>a 
    public static final char IFA = 0x15; // 2+| 0x15 | IFA b, a | performs next instruction only if b>a (signed)
    public static final char IFL = 0x16; // 2+| 0x16 | IFL b, a | performs next instruction only if b<a 
    public static final char IFU = 0x17; // 2+| 0x17 | IFU b, a | performs next instruction only if b<a (signed)
    
    public static final char NA18= 0x18; // - | 0x18 | -        |
    public static final char NA19= 0x19; // - | 0x19 | -        |
    public static final char ADX = 0x1a; // 3 | 0x1a | ADX b, a | sets b to b+a+EX, sets EX to 0x0001 if there is an over-flow, 0x0 otherwise
    public static final char SBX = 0x1b; // 3 | 0x1b | SBX b, a | sets b to b-a+EX, sets EX to 0xFFFF if there is an under-flow, 0x0 otherwise
    public static final char NA1c= 0x1c; // - | 0x1c | -        | 
    public static final char NA1d= 0x1d; // - | 0x1d | -        |
    public static final char STI = 0x1e; // 2 | 0x1e | STI b, a | sets b to a, then increases I and J by 1
    public static final char STD = 0x1f; // 2 | 0x1f | STD b, a | sets b to a, then decreases I and J by 1
    
    public static final char NA00= 0x00; // - | 0x00 | n/a   | reserved for future expansion
    public static final char JSR = 0x01; // 3 | 0x01 | JSR a | pushes the address of the next instruction to the stack,then sets PC to a
    public static final char NA02= 0x02; // - | 0x02 | -     |
    public static final char NA03= 0x03; // - | 0x03 | -     |
    public static final char NA04= 0x04; // - | 0x04 | -     |
    public static final char NA05= 0x05; // - | 0x05 | -     |
    public static final char NA06= 0x06; // - | 0x06 | -     |
    public static final char NA07= 0x07; // - | 0x07 | -     | 
    public static final char INT = 0x08; // 4 | 0x08 | INT a | triggers a software interrupt with message a
    public static final char IAG = 0x09; // 1 | 0x09 | IAG a | sets a to IA 
    public static final char IAS = 0x0a; // 1 | 0x0a | IAS a | sets IA to a
    public static final char RFI = 0x0b; // 3 | 0x0b | RFI a | disables interrupt queueing, pops A from the stack, then  pops PC from the stack
    public static final char IAQ = 0x0c; // 2 | 0x0c | IAQ a | if a is nonzero, interrupts will be added to the queue instead of triggered. if a is zero, interrupts will be triggered as normal again
    public static final char NA0d= 0x0d; // - | 0x0d | -     |
    public static final char NA0e= 0x0e; // - | 0x0e | -     |
    public static final char NA0f= 0x0f; // - | 0x0f | -     |
    public static final char HWN = 0x10; // 2 | 0x10 | HWN a | sets a to number of connected hardware devices
    public static final char HWQ = 0x11; // 4 | 0x11 | HWQ a | sets A, B, C, X, Y registers to information about hardware a A+(B<<16) is a 32 bit word identifying the hardware id
                                         //                    C is the hardware version
                                         //                    X+(Y<<16) is a 32 bit word identifying the manufacturer
    public static final char HWI = 0x12; // 4+| 0x12 | HWI a | sends an interrupt to hardware a

    public static final int REG_A = 0;
    public static final int REG_B = 1;
    public static final int REG_C = 2;
    public static final int REG_X = 3;
    public static final int REG_Y = 4;
    public static final int REG_Z = 5;
    public static final int REG_I = 6;
    public static final int REG_J = 7;

}
