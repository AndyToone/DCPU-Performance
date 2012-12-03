package com.feertech.dcpu.common;

import static com.feertech.dcpu.common.DataUtils.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PerformanceHarness {

    private static final int RESULT_COUNT = 10;

    private final DCPUFactory factory;
    
    private final PerformanceResult[] results;
    
    public PerformanceHarness(DCPUFactory factory) {
        this.factory = factory;
        results = new PerformanceResult[RESULT_COUNT];
    }
    
    public void runSequence() throws IOException {
        // A little bit of warm up
        System.out.print("Warming up");

        runTest();
        runTest();
        
        System.out.print("\nRunning tests");

        for(int i=0; i<RESULT_COUNT; i++) {
            results[i] = runTest();
        }
    }
    
    public String reportResults() {
        StringBuilder sb = new StringBuilder();
        sb.append(factory.cpuType());
        sb.append("\n-----------------------------------------------------------------------------\n");
        
        long totalTime = 0;
        long totalCount = 0;
        
        for(PerformanceResult result: results) {
            sb.append(result.toString()).append("\n");
            totalTime += result.getNanoTime();
            totalCount += result.getInstructionCount();
        }
        sb.append("-----------------------------------------------------------------------------\n");

        sb.append( new PerformanceResult(totalTime, totalCount).toString());
        sb.append("\n");
        
        return sb.toString();
    }
    
    public PerformanceResult runTest() throws IOException {
        final RunnableDCPU cpu = factory.createCPU();
        cpu.reset();
        
        loadTest(cpu);
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cpu.stop();
            }
            
        }).start();
        
        long startTimeNanos = System.nanoTime();
        
        System.out.print(".");
        long instructions = cpu.runEmulation();
        
        long endTimeNanos = System.nanoTime();
        
        return new PerformanceResult(endTimeNanos - startTimeNanos, instructions);
    }

    private void loadTest(RunnableDCPU cpu) throws IOException {
        try ( InputStream stream = getClass().getResourceAsStream("/graphic_test.txt");
              InputStreamReader bis = new InputStreamReader(stream);
              BufferedReader reader = new BufferedReader(bis)) {
            String line = null;
            int count = 0;
            
            while( (line = reader.readLine()) != null ) {
                count++;
                String[] parts = line.split(":");
                
                if( parts.length != 2 ) throw new IOException("Invalid data on line "+count);
                
                int address = readHex(parts[0]);
                
                String[] data = parts[1].trim().split(" ");
                
                for( String word: data ) {
                    cpu.setWord(address++, readHex(word));
                }              
            }
        }
    }
}
