package org.monet.grided.core.model;

public class Process {

    private final long id;
    private final String name;
    private final ProcessType type;

    public Process(long id, String name, ProcessType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProcessType getType() {
        return type;
    }    
}

