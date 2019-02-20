package org.monet.grided.core.serializers.xml.model.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import org.monet.grided.core.serializers.xml.ModelData;
import org.monet.grided.core.serializers.xml.XMLSerializer;
import org.simpleframework.xml.core.Persister;

public class XMLModelDataSerializer implements XMLSerializer<ModelData> {

   Persister persister;      
    
    public XMLModelDataSerializer() {
        this.persister = new Persister();
    }
    
    @Override
    public String serialize(ModelData modelData) {        
        StringWriter writer = new StringWriter();
        try {
            this.persister.write(modelData, writer);
        } catch (Exception ex) {
            throw new RuntimeException(ex);            
        }
        return writer.toString();
    }

    @Override
    public ModelData unSerialize(String xml) {
        ModelData modelData;
        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));  
            modelData = this.persister.read(ModelData.class, is, false);
        } catch (Exception ex) {            
            throw new RuntimeException(ex);
        }
        return modelData;
    }
}

