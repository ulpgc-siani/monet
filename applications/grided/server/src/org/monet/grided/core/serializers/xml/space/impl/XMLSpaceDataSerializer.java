package org.monet.grided.core.serializers.xml.space.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import org.monet.grided.core.serializers.xml.XMLSerializer;
import org.monet.grided.core.serializers.xml.space.SpaceData;
import org.simpleframework.xml.core.Persister;

public class XMLSpaceDataSerializer implements XMLSerializer<SpaceData> {

    Persister persister;      
    
    public XMLSpaceDataSerializer() {
        this.persister = new Persister();
    }
    
    @Override
    public String serialize(SpaceData SpaceData) {        
        StringWriter writer = new StringWriter();
        try {
            this.persister.write(SpaceData, writer);
        } catch (Exception ex) {
            throw new RuntimeException(ex);            
        }
        return writer.toString();
    }

    @Override
    public SpaceData unSerialize(String xml) {
        SpaceData SpaceData;
        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));  
            SpaceData = this.persister.read(SpaceData.class, is, false);
        } catch (Exception ex) {            
            throw new RuntimeException(ex);
        }
        return SpaceData;
    }
}


