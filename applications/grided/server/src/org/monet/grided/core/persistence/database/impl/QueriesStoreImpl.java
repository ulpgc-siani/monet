package org.monet.grided.core.persistence.database.impl;

import java.io.InputStream;
import java.util.Properties;

import org.monet.grided.control.log.Logger;
import org.monet.grided.core.persistence.database.QueriesStore;
import org.monet.grided.core.util.Resources;

import com.google.inject.Inject;

public class QueriesStoreImpl implements QueriesStore {

    private Logger logger;
    private Properties properties;

    @Inject
    public QueriesStoreImpl(Logger logger, String filename) throws Exception {
        this.logger = logger;   
        this.loadQueries(filename);         
    }
    
    private void loadQueries(String filename) throws Exception {
        InputStream stream = Resources.getAsStream(filename);
        properties = new Properties();
        properties.load(stream);        
    }

    @Override
    public String get(String key) {
        String value = this.properties.getProperty(key);
        if (value == null) {
            this.logger.error(String.format("Error loading database query %s", key));
        }
        return value;
    }
}

