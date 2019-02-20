package org.monet.grided.core.persistence.filesystem.impl;

import java.io.File;

import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.persistence.filesystem.StoreDriver;

import com.google.inject.Inject;

public class StoreDriverImpl implements StoreDriver {

    private StoreConfig config;
    private Filesystem filesystem;

    @Inject
    public StoreDriverImpl(StoreConfig config, Filesystem filesystem) {
        this.config = config;
        this.filesystem = filesystem;
    }
    
    @Override
    public void create(String dataPath, long id) {
        String storePath = this.config.getPath(StoreConfig.STORE_PATH);
        String path = storePath + Filesystem.FILE_SEPARATOR + dataPath;
        String folder = path + Filesystem.FILE_SEPARATOR + this.config.getPrefix() + id;
        
        this.filesystem.mkdirs(folder);
    }

    @Override
    public void delete(String dataPath) {
        String storePath = this.config.getPath(StoreConfig.STORE_PATH);
        String path = storePath + Filesystem.FILE_SEPARATOR + dataPath;
        
        this.filesystem.removeDir(path);        
    }

    @Override
    public void copy(String dataPath, File file) {
        String storePath = this.config.getPath(StoreConfig.STORE_PATH);
        String path = storePath + Filesystem.FILE_SEPARATOR + dataPath;
        
        this.filesystem.copyFile(file.getAbsolutePath(), path);
    }

    @Override
    public String[] list(String dataPath) {
        return this.filesystem.listFiles(dataPath);
    }
}

