package com.feertech.dcpu.common;

public class DataUtils {

    private DataUtils() {
        // Private constructor for static utils class
    }
    
    public static final int readHex(String text) {
        if( text == null ) throw new IllegalArgumentException("Cannot guess a value for null string");
        
        text = text.trim();

        int value = 0;
        
        int end = text.length();
        int index = text.startsWith("0x") ? 2 : 0;
        
        while( index < end ) {
            value = (value << 4) + Character.digit(text.charAt(index++), 16);
        }
        return value;
    }
}
