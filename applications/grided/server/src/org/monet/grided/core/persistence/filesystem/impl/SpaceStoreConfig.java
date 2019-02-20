package org.monet.grided.core.persistence.filesystem.impl;

import java.util.Map;

import org.monet.grided.core.persistence.filesystem.Prefixes;

public class SpaceStoreConfig extends StoreConfig {

    public SpaceStoreConfig() {
        super(Prefixes.SPACE);
    }
    
    public SpaceStoreConfig(Map<String, String> paths) {
        super(Prefixes.SPACE, paths);
    }

}

