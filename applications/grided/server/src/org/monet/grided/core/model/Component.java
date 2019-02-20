package org.monet.grided.core.model;

public interface Component {    
    public void start();
    public void stop();
    public boolean isRunning();
    public void setState(State state);   
    public State getState();
}

