package org.monet.grided.core.persistence.impl;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.model.Image;
import org.monet.grided.core.model.Model;
import org.monet.grided.core.model.ModelVersion;
import org.monet.grided.core.persistence.ModelsRepository;
import org.monet.grided.core.persistence.database.impl.ModelsProducer;
import org.monet.grided.core.persistence.exceptions.DuplicateModelVersionException;
import org.monet.grided.core.persistence.filesystem.ModelDataStoreDriver;
import org.monet.grided.core.persistence.filesystem.readers.ModelDataReader;
import org.monet.grided.core.serializers.xml.ModelData;
import org.monet.grided.core.util.ResourceConstants;
import org.monet.grided.core.util.Resources;

import com.google.inject.Inject;

public class ModelsRepositoryImpl implements ModelsRepository {

    private ModelsProducer producer;
    private ModelDataStoreDriver driver;
    private ModelDataReader reader;
    private Filesystem filesystem;

    @Inject
    public ModelsRepositoryImpl(ModelsProducer producer, ModelDataReader reader, ModelDataStoreDriver driver) {    
        this.producer = producer;
        this.reader = reader;
        this.driver = driver;
    }
    
    @Inject
    public void injectFilesystem(Filesystem filesystem) {
        this.filesystem = filesystem;
    }
           
    @Override
    public Model createModel(String name, File versionFile) {
        Model model = new Model(name);        
        model.setData(new ModelData(name));        
                
        this.producer.saveModel(model);        
        this.driver.create(model.getId(), model.getData());
        
        ModelData data = this.reader.read(versionFile);
        ModelVersion version = new ModelVersion(model, data.getDate(), data.getMetaModel());
        List<ModelVersion> versions = this.producer.loadModelVersions(model.getId());
        
        if (versions.contains(version)) throw new DuplicateModelVersionException();
        
        this.producer.saveModelVersion(model.getId(), version);        
        this.driver.saveVersionFile(model.getId(), version.getId(), versionFile);
        
        return model;        
    }

    @Override
    public List<Model> loadModels() {
        return this.producer.loadModels();        
    }

    @Override
    public Model loadModel(long id) {
        Model model = this.producer.loadModel(id);
        ModelData modelData = this.driver.load(id);        
        model.setData(modelData);        
        return model;        
    }

    @Override
    public void saveModel(Model model) {
        this.producer.saveModel(model);
        //this.driver.save(model);        
    }
    
    @Override
    public void saveModel(Model model, Image image) {
        this.producer.saveModel(model);
        //this.driver.save(model, image);        
    }    

    @Override
    public void deleteModel(long modelId) {
        this.producer.deleteModel(modelId);
        this.driver.delete(modelId);        
    }

    @Override
    public void deleteModels(long[] ids) {
        for (long id : ids) {
            this.deleteModel(id);
        }
    }

    @Override
    public ModelVersion saveModelVersion(long modelId, File versionFile) {
        ModelData data = this.reader.read(versionFile);
        
        Model model = this.producer.loadModel(modelId);        
        ModelVersion version = new ModelVersion(model, data.getDate(), data.getMetaModel());
        List<ModelVersion> versions = this.producer.loadModelVersions(modelId);
        
        if (versions.contains(version)) throw new DuplicateModelVersionException();
            
        this.producer.saveModelVersion(modelId, version);        
        this.driver.saveVersionFile(modelId, version.getId(), versionFile);
        
        return version;
    }
    
    @Override
    public ModelVersion loadModelVersion(long modelId, long id) {        
        return this.producer.loadModelVersion(modelId, id);
    }
    
    @Override
    public List<ModelVersion> loadModelVersions(long modelId) {        
        return this.producer.loadModelVersions(modelId);
    }

    @Override
    public List<ModelVersion> loadModelsVersions(long modelId, String metaModelVersion) {
        return this.producer.loadModelVersions(modelId, metaModelVersion);
    }        
    
    @Override
    public ModelVersion loadLatestModelVersion(long modelId) {
        return this.producer.loadLatestModelVersion(modelId);        
    }

    @Override
    public byte[] loadVersionFile(long modelId, long versionId) {
        return this.driver.loadVersion(modelId, versionId);        
    }                    
    
    @Override
    public byte[] loadModelImage(long modelId, String filename) {        
        byte[] bytes = this.driver.loadImage(modelId,  "/images/" + filename);
        if (bytes == null) {
            InputStream stream = Resources.getAsStream(ResourceConstants.FEDERATION_NO_IMAGE);
            bytes = this.filesystem.getBytesFromInputStream(stream); 
        }
        return bytes;
    }
 }