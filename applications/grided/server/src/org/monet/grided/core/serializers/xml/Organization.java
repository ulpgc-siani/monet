package org.monet.grided.core.serializers.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Organization {
    public static final String IMAGES_PATH = "images/";
    @Attribute(name="name", required=true) String name;
    @Attribute(name="url", required=true) String url;
    @Element(name="label", required=false) String label;    
    @Element(name="logo", required=false) Logo logo;
    
    public Organization() {        
        this("", "");
    }

    public Organization(@Attribute(name="name", required=true) String name) {
        this(name, "");     
    }
    
    public Organization(@Attribute(name="name", required=true) String name, @Attribute(name="url", required=true) String url) {
        this(name, url, new Logo());     
    }

    public Organization(@Attribute(name="name", required=true) String name, @Attribute(name="url", required=true) String url, @Element(name="logo", required=false) Logo logo) {
        this.name = name;     
        this.logo = logo;
        this.url = url;
        this.label = "";
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(String url) {
        this.url = url;       
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void setLabel(String label) {
        this.label = label;        
    }
           
    public Logo getLogo() { return logo; }
    public void setLogo(Logo logo) { this.logo = logo; }        
}
