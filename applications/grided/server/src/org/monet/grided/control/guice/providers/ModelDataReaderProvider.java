package org.monet.grided.control.guice.providers;

import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.persistence.filesystem.readers.ModelDataReader;
import org.monet.grided.core.serializers.xml.model.impl.XMLModelDataSerializer;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ModelDataReaderProvider implements Provider<ModelDataReader> {

    private Configuration configuration;
    private Filesystem filesystem;
    private XMLModelDataSerializer serializer;

    @Inject
    public ModelDataReaderProvider(Configuration configuration, Filesystem filesystem, XMLModelDataSerializer serializer) {        
        this.configuration = configuration;        
        this.filesystem = filesystem;
        this.serializer = serializer;
    }
    
    @Override
    public ModelDataReader get() {
        String tempPath = this.configuration.getProperty(Configuration.TEMP_PATH);
        return new ModelDataReader(tempPath, filesystem, serializer);        
    }
}

