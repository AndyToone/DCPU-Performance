package com.feertech.dcpu.common;

public class PerformanceResult {

    public final long nanoTime;
    public final long instructionCount;
    
    public PerformanceResult(long nanoTime, long instructionCount) {
        this.nanoTime = nanoTime;
        this.instructionCount = instructionCount;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder("Result ");
        sb.append(instructionCount).append(" instructions in ").append(nanoTime).append("ns ");
        sb.append(" avg. ").append( instructionsPerMicrosecond() ).append(" ins/μs");
        
        return sb.toString();
    }

    public long instructionsPerMicrosecond() {
        return (instructionCount * 1000l) / nanoTime;
    }
    
    public long getNanoTime() {
        return nanoTime;
    }
    
    public long getInstructionCount() {
        return instructionCount;
    }
}
