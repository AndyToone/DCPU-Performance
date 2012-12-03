package com.feertech.dcpu.common;

public class EmptyCPU extends BaseDCPU implements RunnableDCPU {

    private final int memory[] = new int[MEMORY_LENGTH];
    private volatile boolean canRun = true;
    
    @Override
    public long runEmulation() {
        long instructionCount = 0;
        while(canRun) {
            instructionCount++;
        }
        return instructionCount;
    }

    @Override
    public void stop() {
        canRun = false;
    }

    @Override
    public void reset() {
        canRun = true;
    }

    @Override
    public void setWord(int address, int value) {
        memory[address] = value;
    }

}
