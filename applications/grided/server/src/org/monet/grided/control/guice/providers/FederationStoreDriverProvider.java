package org.monet.grided.control.guice.providers;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.monet.grided.control.guice.annotations.InjectLogger;
import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.persistence.filesystem.DataStoreDriver;
import org.monet.grided.core.persistence.filesystem.FederationDataStoreDriver;
import org.monet.grided.core.persistence.filesystem.impl.DataStoreDriverImpl;
import org.monet.grided.core.persistence.filesystem.impl.FederationDataStoreDriverImpl;
import org.monet.grided.core.persistence.filesystem.impl.FederationStoreConfig;
import org.monet.grided.core.persistence.filesystem.impl.ServerStoreConfig;
import org.monet.grided.core.persistence.filesystem.impl.StoreConfig;
import org.monet.grided.core.serializers.xml.federation.FederationData;
import org.monet.grided.core.serializers.xml.federation.impl.XMLFederationDataSerializer;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class FederationStoreDriverProvider implements Provider<FederationDataStoreDriver> {

    private Configuration configuration;
    private Filesystem filesystem;
    private XMLFederationDataSerializer serializer;
    @InjectLogger private Logger logger;
    
    @Inject
    public FederationStoreDriverProvider(Configuration configuration, Filesystem filesystem, XMLFederationDataSerializer serializer) {    
        this.configuration = configuration;
        this.filesystem = filesystem;
        this.serializer = serializer;
    }
    
    @Override
    public FederationDataStoreDriver get() {
        String storePath = this.configuration.getProperty(Configuration.DATA_STORE_PATH);
        String tempPath = this.configuration.getProperty(Configuration.TEMP_PATH);
        
        Map<String, String> paths = new HashMap<String, String>();
        paths.put(StoreConfig.STORE_PATH, storePath);
        paths.put(StoreConfig.TEMP_STORE_PATH, tempPath);
                
        ServerStoreConfig serverStoreConfig = new ServerStoreConfig(paths);        
        FederationStoreConfig federationStoreConfig = new FederationStoreConfig(paths);
        federationStoreConfig.addPath(StoreConfig.ROOT, "servers");
        
        DataStoreDriver<Federation, FederationData> genericStoreDriver = new DataStoreDriverImpl<Federation, FederationData>(federationStoreConfig, this.filesystem, this.serializer, this.logger);
        
        FederationDataStoreDriverImpl driver = new FederationDataStoreDriverImpl(serverStoreConfig, federationStoreConfig, genericStoreDriver);
        return driver;
    }
}

