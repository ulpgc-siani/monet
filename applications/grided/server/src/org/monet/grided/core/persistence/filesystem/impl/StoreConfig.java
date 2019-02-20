package org.monet.grided.core.persistence.filesystem.impl;

import java.util.HashMap;
import java.util.Map;

public abstract class StoreConfig {
    public static final String STORE_PATH = "store-path";
    public static final String TEMP_STORE_PATH = "temp-path";
    public static final String ROOT = "root";    
    
    public Map<String, String> paths;
    private String prefix;
        
    public StoreConfig(String prefix) {
        this(prefix, new HashMap<String, String>());     
    }
        
    public StoreConfig(String prefix, Map<String, String> paths) {
        this.prefix = prefix;
        this.paths = paths;      
        if (this.paths.get(ROOT) == null ) this.paths.put(ROOT, "");
    }
    
    public void addPath(String key, String value) {
        this.paths.put(key, value);
    }
    
    public String getPath(String key) {
        return this.paths.get(key);
    }
    
    public String getPrefix() {
        return this.prefix;
    }
}