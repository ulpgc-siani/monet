package org.monet.federation.accountservice.accountactions.impl.messagemodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.Serializable;
import java.io.StringWriter;

@Root(name = "federationsocketinfo")
public class FederationSocketInfo implements Serializable {
    private static final long serialVersionUID = -1277945125210886769L;
    private static Serializer persister = new Persister();

    @Attribute
    private String host;
    @Attribute
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String serializeToXML() throws Exception {
        StringWriter writer = new StringWriter();
        persister.write(this, writer);
        return writer.toString();
    }

}
