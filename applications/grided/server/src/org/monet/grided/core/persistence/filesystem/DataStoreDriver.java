package org.monet.grided.core.persistence.filesystem;

import java.io.File;

import org.monet.grided.core.model.Image;


public interface DataStoreDriver<T, R> {
        
    public void create(String dataPath, long id, R data);
    public void create(String dataPath, long id, R data, Image image);
    
    public R load(File folder);
    public R load(String dataPath, long id);      
    
    public void save(String dataPath, T object, File file);
    public void save(String dataPath, T object);
    public void save(String dataPath, T object, Image image);        
    public void delete(String dataPath);
    
    public byte[] loadFile(String filename);       
    public String[] listFiles(String path);    
    public void saveFile(File file, String path);
    
    public void createDirectory(String dirname);     
}

