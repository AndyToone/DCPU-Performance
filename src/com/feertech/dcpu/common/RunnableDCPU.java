package com.feertech.dcpu.common;

public interface RunnableDCPU {

    /**
     * Start emulating the processor in this thread, returning when stop() has been called
     * @return The number of instructions that have been executed
     */
    public long runEmulation();
    
    /**
     * Stop emulating the processor as soon as possible.
     */
    public void stop();
    
    /**
     * Reset the CPU
     */
    public void reset();
    
    /**
     * Set a memory location to the given value
     * @param address The address to set
     * @param value The value to store at the given address
     */
    public void setWord(int address, int value);
}
