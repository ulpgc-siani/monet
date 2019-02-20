package org.monet.api.federation.setupservice.impl.model;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringReader;

public class FederationSocketInfo extends BaseObject {
    private String host;
    private int port;

    public FederationSocketInfo() {
        this.host = "";
        this.port = -1;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void deserializeFromXML(String content) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        StringReader reader;
        org.jdom.Document document;
        Element node;

        if (content.isEmpty())
            return;

        while (!content.substring(content.length() - 1).equals(">"))
            content = content.substring(0, content.length() - 1);

        reader = new StringReader(content);

        document = builder.build(reader);
        node = document.getRootElement();

        this.deserializeFromXML(node);
    }

    public void deserializeFromXML(Element info) {
        if (info.getAttribute("host") != null) this.host = info.getAttributeValue("host");
        if (info.getAttribute("port") != null) this.port = Integer.valueOf(info.getAttributeValue("port"));
    }

    public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {

        serializer.startTag("", "federationsocketinfo");

        serializer.attribute("", "host", this.host);
        serializer.attribute("", "port", String.valueOf(this.port));

        serializer.endTag("", "federationsocketinfo");

    }

}