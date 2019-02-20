package org.monet.grided.core.serializers.xml;

import org.monet.grided.core.model.MetaModelVersion;
import org.monet.grided.core.persistence.filesystem.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="model")
public class ModelData implements Data {

    @Attribute(name="name", required=true) String name;
    @Element(name="label", required=false) String label;    
    @Element(name="version", required=false) Version version;
    
    public ModelData(@Attribute(name="name", required=true) String name) {
        this(name, "");
    }
    
    public ModelData(@Attribute(name="name", required=true) String name, @Element(name="label", required=false) String label) {
        this.name = name;
        this.label = label;
    }

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

    public void setVersion(Version version) {
        this.version = version;
    }
    
    public long getDate() {
        return this.version.getDate();
    }
    
    public MetaModelVersion getMetaModel() {
        return this.version.getMetaModel();
    }
    
    public static class Version {
        @Attribute(name="date") long date;
        @Attribute(name="metamodel") String metaModel;
        
        public Version(@Attribute(name="date") long date, @Attribute(name="metamodel") String metaModel) {
            this.date = date;
            this.metaModel = metaModel;
        }
        
        public long getDate() {
            return this.date;
        }
        
        public MetaModelVersion getMetaModel() {
            MetaModelVersion version = new MetaModelVersion(this.metaModel);
            return version;
        }
    }       
}
