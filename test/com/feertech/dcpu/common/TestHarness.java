package com.feertech.dcpu.common;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class TestHarness {

    private EmptyFactory factory;
    private PerformanceHarness harness;
    
    @Before
    public void setup() {
        factory = new EmptyFactory();
        harness = new PerformanceHarness(factory);
    }
    
    @Test
    public void test() throws IOException {
        harness.runSequence();
        
        System.out.println(harness.reportResults());
    }
    
    private class EmptyFactory implements DCPUFactory {

        @Override
        public RunnableDCPU createCPU() {
            return new EmptyCPU();
        }

        @Override
        public String cpuType() {
            return "Bare CPU";
        }
        
    }

}
