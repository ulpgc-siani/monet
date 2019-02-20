package org.monet.grided.control.guice.providers;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.monet.grided.control.guice.annotations.InjectLogger;
import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.persistence.filesystem.DataStoreDriver;
import org.monet.grided.core.persistence.filesystem.SpaceDataStoreDriver;
import org.monet.grided.core.persistence.filesystem.impl.DataStoreDriverImpl;
import org.monet.grided.core.persistence.filesystem.impl.FederationStoreConfig;
import org.monet.grided.core.persistence.filesystem.impl.ModelStoreConfig;
import org.monet.grided.core.persistence.filesystem.impl.ServerStoreConfig;
import org.monet.grided.core.persistence.filesystem.impl.SpaceDataStoreDriverImpl;
import org.monet.grided.core.persistence.filesystem.impl.SpaceStoreConfig;
import org.monet.grided.core.persistence.filesystem.impl.StoreConfig;
import org.monet.grided.core.serializers.xml.space.SpaceData;
import org.monet.grided.core.serializers.xml.space.impl.XMLSpaceDataSerializer;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class SpaceStoreDriverProvider implements Provider<SpaceDataStoreDriver> {

    private Configuration configuration;
    private Filesystem filesystem;
    private XMLSpaceDataSerializer serializer;
    @InjectLogger private Logger logger;
    
    @Inject    
    public SpaceStoreDriverProvider(Configuration configuration, Filesystem filesystem, XMLSpaceDataSerializer serializer) {
        this.configuration = configuration;
        this.filesystem = filesystem;
        this.serializer = serializer;  
    }
    
    @Override
    public SpaceDataStoreDriver get() {
        String storePath = this.configuration.getProperty(Configuration.DATA_STORE_PATH);
        String tempPath = this.configuration.getProperty(Configuration.TEMP_PATH);
        
        Map<String, String> paths = new HashMap<String, String>();
        paths.put(ModelStoreConfig.STORE_PATH, storePath);
        paths.put(ModelStoreConfig.TEMP_STORE_PATH, tempPath);
                
        ServerStoreConfig serverStoreConfig = new ServerStoreConfig(paths);
        FederationStoreConfig federationStoreConfig = new FederationStoreConfig(paths);
        SpaceStoreConfig spaceStoreConfig = new SpaceStoreConfig(paths);
        spaceStoreConfig.addPath(StoreConfig.ROOT, "servers");
        
        DataStoreDriver<Space, SpaceData> genericStoreDriver = new DataStoreDriverImpl<Space, SpaceData>(spaceStoreConfig, this.filesystem, this.serializer, this.logger);
        
        SpaceDataStoreDriver driver = new SpaceDataStoreDriverImpl(serverStoreConfig, federationStoreConfig, spaceStoreConfig, genericStoreDriver);
        return driver;
    }
}

