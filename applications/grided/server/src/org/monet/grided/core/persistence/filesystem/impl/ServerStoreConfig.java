package org.monet.grided.core.persistence.filesystem.impl;

import java.util.Map;

import org.monet.grided.core.persistence.filesystem.Prefixes;

public class ServerStoreConfig extends StoreConfig {
        
    public ServerStoreConfig() {
        super(Prefixes.SERVER);
    }

    public ServerStoreConfig(Map<String, String> paths) {
        super(Prefixes.SERVER, paths);
    }
}

