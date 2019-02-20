package org.monet.grided.core.persistence.filesystem;

import java.io.File;

public interface StoreDriver {
    public void create(String dataPath, long id);        
    public void delete(String dataPath);
    public void copy(String dataPath, File file);
    public String[] list(String dataPath);
}

