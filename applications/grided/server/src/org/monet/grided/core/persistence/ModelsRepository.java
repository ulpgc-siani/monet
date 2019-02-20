package org.monet.grided.core.persistence;

import java.io.File;
import java.util.List;

import org.monet.grided.core.model.Image;
import org.monet.grided.core.model.Model;
import org.monet.grided.core.model.ModelVersion;

public interface ModelsRepository {

    public Model createModel(String name, File versionFile);    
    public List<Model> loadModels();        
    public Model loadModel(long id);
    public void saveModel(Model model);
    public void saveModel(Model model, Image image);    
    public void deleteModel(long modelId);
    public void deleteModels(long[] ids);
    
    public ModelVersion loadModelVersion(long modelId, long id);    
    public List<ModelVersion> loadModelVersions(long modelId);
    public List<ModelVersion> loadModelsVersions(long modelId, String metaModelVersion);
    public ModelVersion loadLatestModelVersion(long modelId);
    public ModelVersion saveModelVersion(long modelId, File versionFile);
    
    public byte[] loadVersionFile(long modelId, long versionId);
    public byte[] loadModelImage(long modelId, String filename);
}