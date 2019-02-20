package org.monet.grided.core.model;

public class State {
    private final boolean value;
    private final long time;
    
    private State(boolean value, long time) {
        this.value = value;
        this.time = time;
    }
    
    public static State running(long time) {        
        return new State(true, time);
    }
    
    public static State stopped() {
        return new State(false, 0);
    }
    
    public boolean isRunning() {
        return this.value == true;
    }
    
    public long getRunningFrom() {
        return this.time;
    }        
}

