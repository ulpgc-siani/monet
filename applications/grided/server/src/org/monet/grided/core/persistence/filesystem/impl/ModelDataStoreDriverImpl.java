package org.monet.grided.core.persistence.filesystem.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.model.Image;
import org.monet.grided.core.model.Model;
import org.monet.grided.core.model.SpaceVersion;
import org.monet.grided.core.persistence.filesystem.DataStoreDriver;
import org.monet.grided.core.persistence.filesystem.ModelDataStoreDriver;
import org.monet.grided.core.serializers.xml.ModelData;
import org.monet.grided.library.LibraryFile;

public class ModelDataStoreDriverImpl implements ModelDataStoreDriver {

    private DataStoreDriver<Model, ModelData> driver;
    private StoreConfig config;
    
    public ModelDataStoreDriverImpl(ModelStoreConfig config, DataStoreDriver<Model, ModelData> driver) {
        this.config = config;
        this.driver = driver;
    }
    
    public void create(long id, ModelData data) {
        this.driver.create(this.getDataPath(id), id, data);
    }
    
    public void create(long id, ModelData data, Image image) {
        this.driver.createDirectory(config.getPath(ModelStoreConfig.VERSIONS_PATH));
        this.driver.create(this.getDataPath(id), id, data, image);
    }
    
    public ModelData load(long id) {
        ModelData modelData = this.driver.load(this.getDataPath(id), id);        
        return modelData;        
    }
    
    public void save(Model model) {
        this.driver.save(this.getDataPath(model.getId()), model);
    }
    
    public void save(Model model, Image image) {
        this.driver.save(this.getDataPath(model.getId()), model, image);
    }
    

    @Override
    public void saveVersionFile(long modelId, long versionId, File versionFile) {
      String path = this.getDataPath(modelId) + "/" + this.config.getPath(ModelStoreConfig.VERSIONS_PATH);
      String filenamePath = LibraryFile.getDirname(versionFile.getAbsolutePath());
      String filename = filenamePath + Filesystem.FILE_SEPARATOR + versionId + ".zip";
      
      File targetFile = new File(filename);
      if (! versionFile.renameTo(targetFile)) throw new RuntimeException("Imposible rename file: " + versionFile.getAbsolutePath());
      
      this.driver.saveFile(targetFile, path);     
    }    
    
    public void delete(long modelId) {
        this.driver.delete(this.getDataPath(modelId));
    }
    
    @Override
    public List<SpaceVersion> getModelVersions(long modelId) {
        List<SpaceVersion> versions = new ArrayList<SpaceVersion>();
        String  versionsPath = this.config.getPath(ModelStoreConfig.VERSIONS_PATH);
        String[] filenames = this.driver.listFiles(this.getDataPath(modelId) + "/" + versionsPath);
                
        for (String filename : filenames) {
            SpaceVersion version = new SpaceVersion(LibraryFile.getFilenameWithoutExtension(filename));
            versions.add(version);           
        }
        return versions;
    }
    
    @Override
    public byte[] loadVersion(long modelId, long versionId) {
        String filename = versionId + ".zip";
        return this.driver.loadFile(this.getDataPath(modelId) + "/versions/" + filename);        
    }
        
    @Override
    public byte[] loadImage(long modelId, String filename) {
        return this.driver.loadFile(this.getDataPath(modelId) + "/images/" + filename);        
    }
    
    
    private String getDataPath(long id) {
        return this.config.getPrefix() + id;
    }
}