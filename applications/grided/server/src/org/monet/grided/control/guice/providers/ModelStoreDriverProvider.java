package org.monet.grided.control.guice.providers;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.monet.grided.control.guice.annotations.InjectLogger;
import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.model.Model;
import org.monet.grided.core.persistence.filesystem.DataStoreDriver;
import org.monet.grided.core.persistence.filesystem.ModelDataStoreDriver;
import org.monet.grided.core.persistence.filesystem.impl.DataStoreDriverImpl;
import org.monet.grided.core.persistence.filesystem.impl.ModelDataStoreDriverImpl;
import org.monet.grided.core.persistence.filesystem.impl.ModelStoreConfig;
import org.monet.grided.core.serializers.xml.ModelData;
import org.monet.grided.core.serializers.xml.model.impl.XMLModelDataSerializer;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ModelStoreDriverProvider implements Provider<ModelDataStoreDriver> {

    private Configuration configuration;
    private Filesystem filesystem;
    private XMLModelDataSerializer serializer;
    @InjectLogger private Logger logger;

    @Inject
    public ModelStoreDriverProvider(Configuration configuration, Filesystem filesystem, XMLModelDataSerializer serializer) {
        this.configuration = configuration;
        this.filesystem = filesystem;        
        this.serializer = serializer;
    }
        
    @Override
    public ModelDataStoreDriver get() {
        String storePath = this.configuration.getProperty(Configuration.DATA_STORE_PATH);
        String tempPath = this.configuration.getProperty(Configuration.TEMP_PATH);
        
        Map<String, String> paths = new HashMap<String, String>();
        paths.put(ModelStoreConfig.STORE_PATH, storePath);
        paths.put(ModelStoreConfig.TEMP_STORE_PATH, tempPath);
        paths.put(ModelStoreConfig.VERSIONS_PATH, "versions");
        paths.put(ModelStoreConfig.ROOT, "models");        
                
        ModelStoreConfig config = new ModelStoreConfig(paths);        
        DataStoreDriver<Model, ModelData> genericStoreDriver = new DataStoreDriverImpl<Model, ModelData>(config, this.filesystem, this.serializer, this.logger);
        
        ModelDataStoreDriver driver = new ModelDataStoreDriverImpl(config, genericStoreDriver);
        return driver;
    }
}

