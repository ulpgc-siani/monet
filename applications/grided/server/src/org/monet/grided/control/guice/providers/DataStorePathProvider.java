package org.monet.grided.control.guice.providers;

import org.monet.grided.control.log.Logger;
import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.exceptions.SystemException;

import com.google.inject.Provider;

public class DataStorePathProvider implements Provider<String> {

    private Logger logger;
    private Configuration configuration;

    DataStorePathProvider(Logger logger, Configuration configuration) {
        this.logger = logger;
        this.configuration = configuration;
    }
    
    @Override
    public String get() {
        String storePath = configuration.getProperty(Configuration.DATA_STORE_PATH);
        if (storePath == null || storePath.trim().equals("")) {
            String message = "Error STORE_PATH config parameters is not setted";  
            logger.error(message);
            throw new SystemException(message);
        }
        return storePath;                
    }
}

