package org.monet.grided.core.serializers.xml.federation.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import org.monet.grided.core.serializers.xml.XMLSerializer;
import org.monet.grided.core.serializers.xml.federation.FederationData;
import org.simpleframework.xml.core.Persister;

public class XMLFederationDataSerializer implements XMLSerializer<FederationData> {

    Persister persister;      
    
    public XMLFederationDataSerializer() {
        this.persister = new Persister();
    }
    
    @Override
    public String serialize(FederationData federationData) {        
        StringWriter writer = new StringWriter();
        try {
            this.persister.write(federationData, writer);
        } catch (Exception ex) {
            throw new RuntimeException(ex);            
        }
        return writer.toString();
    }

    @Override
    public FederationData unSerialize(String xml) {
        FederationData federationData;
        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));  
            federationData = this.persister.read(FederationData.class, is, false);
        } catch (Exception ex) {            
            throw new RuntimeException(ex);
        }
        return federationData;
    }
}

