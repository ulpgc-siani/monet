package org.monet.grided.core.services.space.data;

import java.io.Serializable;

public class Service implements Serializable {
    private static final long serialVersionUID = 7717877466166391128L;
    private String name;
    private ServiceType type;

    public Service(String name, ServiceType type) {
        this.name = name;
        this.type = type;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ServiceType getType() {
        return this.type;
    }
}

