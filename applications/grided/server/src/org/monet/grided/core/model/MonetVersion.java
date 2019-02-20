package org.monet.grided.core.model;

public interface MonetVersion {
    public String getValue();
    public boolean isCompatible(String version);       
}

