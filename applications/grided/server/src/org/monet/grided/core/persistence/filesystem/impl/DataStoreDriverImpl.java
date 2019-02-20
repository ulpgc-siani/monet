package org.monet.grided.core.persistence.filesystem.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.monet.grided.constants.ErrorCode;
import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.model.Image;
import org.monet.grided.core.persistence.filesystem.Data;
import org.monet.grided.core.persistence.filesystem.DataStoreDriver;
import org.monet.grided.core.persistence.filesystem.HasData;
import org.monet.grided.core.serializers.xml.XMLSerializer;
import org.monet.grided.exceptions.DataStoreException;
import org.monet.grided.library.LibraryFile;

import com.google.inject.Inject;

public class DataStoreDriverImpl<T extends HasData<R>, R extends Data> implements DataStoreDriver<T, R> {
    
    private Filesystem filesystem;
    private XMLSerializer<R> serializer;
    private StoreConfig config;
    private Logger logger;

    @Inject
    public DataStoreDriverImpl(StoreConfig config, Filesystem filesystem, XMLSerializer<R> serializer, Logger logger) {
        this.config = config;
        this.filesystem = filesystem;
        this.serializer = serializer;
        this.logger = logger;
    }
            
    @Override
    public void create(String dataPath, long id, R data) {
        String path = this.buildPath(dataPath); 
        String filename = path   + Filesystem.FILE_SEPARATOR + this.config.getPrefix() + id + ".xml";
        
        this.filesystem.mkdirs(path);        
        String xml = this.serializer.serialize(data);
        this.filesystem.writeFile(filename, xml);                                                                          
    }
    
    @Override
    public void create(String dataPath, long id, R data, Image image) {                               
        this.create(dataPath, id, data);
        this.saveImage(dataPath, image);
    }
            
    @Override
    public R load(File folder) {
        DataFile dataFile = this.getDataFile(folder);
        return this.load(dataFile);                           
    }
    
    @Override
    public R load(String dataPath, long id) {     
        String path = this.buildPath(dataPath); 
        String filename = path   + Filesystem.FILE_SEPARATOR + this.config.getPrefix() + id + ".xml";
        
        File file = new File(filename);
        if (! file.exists()) throw new DataStoreException(ErrorCode.DATA_FILE_NOT_FOUND, filename);
        return this.load(new DataFile(file));        
    }
    
    @Override
    public void save(String dataPath, T object) {    
        String path = this.buildPath(dataPath);        
        String xml = this.serializer.serialize(object.getData());
        DataFile dataFile = this.getDataFile(new File(path));
        this.filesystem.writeFile(dataFile.getAbsolutePath(), xml);        
    }
    
    @Override
    public void save(String dataPath, T object, Image image) {
        String path = this.buildPath(dataPath); 
        String xml = this.serializer.serialize(object.getData());
        DataFile dataFile = this.getDataFile(new File(path));
        this.filesystem.writeFile(dataFile.getAbsolutePath(), xml);
        this.saveImage(dataPath, image);
    }    
    
    @Override
    public void save(String dataPath, T object, File folder) {
        String path = this.buildPath(dataPath); 
        String filename = path   + Filesystem.FILE_SEPARATOR + this.config.getPrefix() + object.getId() + ".xml";
        String imagesPath = path + Filesystem.FILE_SEPARATOR + "images";
        
        if (! this.filesystem.existFile(path)) this.filesystem.mkdirs(path);
        
        String xml = this.serializer.serialize(object.getData());
        
        this.filesystem.writeFile(filename, xml);
                
        if (this.filesystem.existFile(imagesPath)) this.filesystem.removeDir(imagesPath);
        this.filesystem.mkdirs(imagesPath);
                    
        this.filesystem.copyDir(folder.getAbsolutePath() + Filesystem.FILE_SEPARATOR + "images", imagesPath);        
    }
    
    @Override
    public void saveFile(File file, String dataPath) {
        String path = this.buildPath(dataPath); 
        if (! this.filesystem.existFile(path)) this.filesystem.mkdirs(path);
        this.filesystem.copyFile(file.getAbsolutePath(), path);
    }
        
    @Override
    public void delete(String dataPath) {                
        String path = this.buildPath(dataPath); 
                
        File file = new File(path);        
        if (file.exists()) {
            this.filesystem.removeDir(path);                                    
        }        
    }    
       
    @Override
    public byte[] loadFile(String filename) {
        byte[] bytes = null;
        
        String path = this.buildPath("") + filename;
        try {
            bytes = this.filesystem.getBytesFromFile(path);
        } catch (Exception ex) {
            bytes = null;
//            this.logger.error("File not found: " + filename, ex);
            this.logger.warn("File not found: " + filename);
        }
        return bytes;               
    }
    
    @Override
    public String[] listFiles(String dataPath) {
        String path = this.buildPath(dataPath); 
        return this.filesystem.listFiles(path);
    } 
    
    @Override
    public void createDirectory(String dirname) {
        if (dirname == null || dirname.isEmpty()) return;
        String path = this.buildPath("") + dirname; 
        this.filesystem.mkdirs(path);        
    }


    
    
    private R load(DataFile dataFile) {
        R data = null;
        try {            
            String xml = this.filesystem.readFile(dataFile.getAbsolutePath());
            data = this.serializer.unSerialize(xml);
        } catch(Exception ex) {
            throw new DataStoreException(ErrorCode.READ_DATA_FILE, ex, dataFile.getAbsolutePath());
        }        
        return data;
    }
    
    private DataFile getDataFile(File folder) {                       
        String[] filenames = this.filesystem.listFiles(folder.getAbsolutePath(), new FilenameFilter() {                
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".xml")) return true;
                return false;
            }
        });
        if (filenames.length == 0 || filenames.length > 1) throw new RuntimeException(ErrorCode.MULTIPLE_DATA_FILE_IN_ZIP);
        
        return new DataFile(filenames[0]);
    }
    
    private void saveImage(String dataPath, Image image) {
        String path = this.buildPath(dataPath); 
        String imagesPath = path + Filesystem.FILE_SEPARATOR + "images";
        
        if (this.filesystem.existFile(imagesPath)) this.filesystem.removeDir(imagesPath);
        this.filesystem.mkdirs(imagesPath);
        
        String imageFilename = imagesPath + Filesystem.FILE_SEPARATOR + image.getName();

        try {
            String filename = image.getAbsolutePath();
            BufferedImage bufferedImage = ImageIO.read(image.getFile());
            
            ImageIO.write(bufferedImage, LibraryFile.getExtension(filename), new File(imageFilename));            
        } catch (IOException ex) {
            throw new DataStoreException(ErrorCode.SAVE_IMAGE, ex, imageFilename);            
        }        
    }
    
    private String buildPath(String dataPath) {
        String root = this.config.getPath(StoreConfig.ROOT).equals("")? "" : this.config.getPath(StoreConfig.ROOT); 
        String storePath =  (root.equals(""))? this.config.getPath(StoreConfig.STORE_PATH) : this.config.getPath(StoreConfig.STORE_PATH) + Filesystem.FILE_SEPARATOR + root;
        
        return storePath + Filesystem.FILE_SEPARATOR + dataPath;
    }
}

