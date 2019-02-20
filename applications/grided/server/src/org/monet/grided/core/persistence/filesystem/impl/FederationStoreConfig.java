package org.monet.grided.core.persistence.filesystem.impl;

import java.util.Map;

import org.monet.grided.core.persistence.filesystem.Prefixes;

public class FederationStoreConfig extends StoreConfig {
   
    public FederationStoreConfig() {
        super(Prefixes.FEDERATION);        
    }

    public FederationStoreConfig(Map<String, String> paths) {        
        super(Prefixes.FEDERATION, paths);        
    }
}

