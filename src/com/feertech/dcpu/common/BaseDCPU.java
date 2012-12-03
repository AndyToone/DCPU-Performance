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
               
    public static final char MDI = 0x08; // 3 | 0x09 | MDI b, a | like MOD, but treat b, a as signed. (MDI -7, 16 == -7)
    public static final char AND = 0x08; // 1 | 0x0a | AND b, a | sets b to b&a
    public static final char BOR = 0x08; // 1 | 0x0b | BOR b, a | sets b to b|a
    public static final char XOR = 0x08; // 1 | 0x0c | XOR b, a | sets b to b^a
    public static final char SHR = 0x08; // 1 | 0x0d | SHR b, a | sets b to b>>>a, sets EX to ((b<<16)>>a)&0xffff (logical shift)
    public static final char ASR = 0x08; // 1 | 0x0e | ASR b, a | sets b to b>>a, sets EX to ((b<<16)>>>a)&0xffff (arithmetic shift) (treats b as signed)
    public static final char SHL = 0x08; // 1 | 0x0f | SHL b, a | sets b to b<<a, sets EX to ((b<<a)>>16)&0xffff

    public static final char IFB = 0x08; // 2+| 0x10 | IFB b, a | performs next instruction only if (b&a)!=0
    public static final char IFC = 0x08; // 2+| 0x11 | IFC b, a | performs next instruction only if (b&a)==0
    public static final char IFE = 0x08; // 2+| 0x12 | IFE b, a | performs next instruction only if b==a 
    public static final char IFN = 0x08; // 2+| 0x13 | IFN b, a | performs next instruction only if b!=a 
    public static final char IFG = 0x08; // 2+| 0x14 | IFG b, a | performs next instruction only if b>a 
    public static final char IFA = 0x08; // 2+| 0x15 | IFA b, a | performs next instruction only if b>a (signed)
    public static final char IFL = 0x08; // 2+| 0x16 | IFL b, a | performs next instruction only if b<a 
    public static final char IFU = 0x08; // 2+| 0x17 | IFU b, a | performs next instruction only if b<a (signed)
    
    public static final char NA18= 0x08; // - | 0x18 | -        |
    public static final char NA19= 0x08; // - | 0x19 | -        |
    public static final char ADX = 0x08; // 3 | 0x1a | ADX b, a | sets b to b+a+EX, sets EX to 0x0001 if there is an over-flow, 0x0 otherwise
    public static final char SBX = 0x08; // 3 | 0x1b | SBX b, a | sets b to b-a+EX, sets EX to 0xFFFF if there is an under-flow, 0x0 otherwise
    public static final char NA1c= 0x08; // - | 0x1c | -        | 
    public static final char NA1d= 0x08; // - | 0x1d | -        |
    public static final char STI = 0x08; // 2 | 0x1e | STI b, a | sets b to a, then increases I and J by 1
    public static final char STD = 0x08; // 2 | 0x1f | STD b, a | sets b to a, then decreases I and J by 1
}
