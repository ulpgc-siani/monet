package org.monet.federation.accountservice.accountactions.impl.messagemodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.Serializable;
import java.io.StringWriter;

@Root(name = "federationinfo")
public class FederationInfo implements Serializable {
    private static final long serialVersionUID = -1277945125210886767L;

    private static Serializer persister = new Persister();

    @Attribute
    private String name;
    @Attribute
    private String label;
    @Attribute
    private String logoPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String serializeToXML() throws Exception {
        StringWriter writer = new StringWriter();
        persister.write(this, writer);
        return writer.toString();
    }
}
