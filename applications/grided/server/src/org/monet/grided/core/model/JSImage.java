package org.monet.grided.core.model;

public class JSImage {

    private final String name;
    private final String src;
    
    public JSImage(String name, String src) {
        this.name = name;
        this.src = src;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getSource() {
        return this.src;
    }
}

