package org.monet.grided.core.serializers.xml;

import org.simpleframework.xml.Attribute;

public class Logo {
    @Attribute(name="name") String name;
    @Attribute(name="path") String path;

    public Logo() {          
        this("");
    }     

    public Logo(@Attribute(name="name") String name) {
        this.name = name;
        this.path = "images";
    }

    public Logo(@Attribute(name="name") String name, @Attribute(name="path") String path) {
        this.name = name;
        this.path = path;        
    }

    public void setName(String name) { 
        this.name = name;           
    }

    public String getName() { 
        return this.name; 
    }

    public String getPath() { 
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;       
    }
}