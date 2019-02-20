package org.monet.grided.core.persistence.filesystem;

import java.io.File;
import java.util.List;

import org.monet.grided.core.model.Image;
import org.monet.grided.core.model.Model;
import org.monet.grided.core.model.SpaceVersion;
import org.monet.grided.core.serializers.xml.ModelData;

public interface ModelDataStoreDriver {    
    public void create(long id, ModelData data);
    public void create(long id, ModelData data, Image image);
    public ModelData load(long id);
    public void save(Model model);    
    public void save(Model model, Image image);
    public void delete(long modelId);    
    public List<SpaceVersion> getModelVersions(long modelId);
    public void saveVersionFile(long modelId, long versionId, File versionFile);
        
    public byte[] loadVersion(long modelId, long versionId);
    public byte[] loadImage(long modelId, String filename);
}

