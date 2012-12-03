package org.notch.dcpu;

public interface DCPUHardware {

    void tick60hz();

    void query();

    void interrupt();

}
