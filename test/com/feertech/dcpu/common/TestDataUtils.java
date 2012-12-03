package com.feertech.dcpu.common;

import static org.junit.Assert.*;
import static com.feertech.dcpu.common.DataUtils.*;

import org.junit.Test;

public class TestDataUtils {

    @Test
    public void testSimpleHex() {
        assertEquals(1, readHex("1"));
        assertEquals(15, readHex("f"));

        assertEquals(254, readHex("fe"));
        assertEquals(254, readHex("FE"));

        assertEquals(0, readHex("0"));
        assertEquals(0, readHex("0000"));
    }

    
    @Test
    public void testPrefixHex() {
        assertEquals(1, readHex("0x1"));
        assertEquals(15, readHex("0xf"));

        assertEquals(254, readHex("0xfe"));
        assertEquals(254, readHex("0xFE"));

        assertEquals(0, readHex("0x0"));
        assertEquals(0, readHex("0x0000"));
    }
}
