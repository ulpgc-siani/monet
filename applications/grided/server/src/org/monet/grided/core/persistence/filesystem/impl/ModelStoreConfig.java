package org.monet.grided.core.persistence.filesystem.impl;

import java.util.Map;

import org.monet.grided.core.persistence.filesystem.Prefixes;

public class ModelStoreConfig extends StoreConfig {
    
    public static final String VERSIONS_PATH = "versions";
    
    public ModelStoreConfig() {
        super(Prefixes.MODEL);
    }
    
    public ModelStoreConfig(Map<String, String> paths) {
        super(Prefixes.MODEL, paths);
    }    

}