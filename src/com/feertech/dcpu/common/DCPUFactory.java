package com.feertech.dcpu.common;

public interface DCPUFactory {

    /**
     * Create a newly initialised CPU
     * @return A CPU instance
     */
    public RunnableDCPU createCPU();
    
    /**
     * Return a string describing the type of CPU this factory creates.
     * @return The description of the CPU created by this factory
     */
    public String cpuType();
}
