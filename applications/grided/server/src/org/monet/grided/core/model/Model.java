package org.monet.grided.core.model;

import java.util.ArrayList;
import java.util.List;

import org.monet.grided.core.persistence.filesystem.HasData;
import org.monet.grided.core.serializers.xml.ModelData;

public class Model implements HasData<ModelData> {
    private long id;
    private String name;    
    private ModelVersion latestModelVersion;
    private List<ModelVersion> versions;
    private ModelData data;
    
    public Model(String name) {
        this(-1, name);
    }
        
    public Model(long id, String name) {
        this.id = id;
        this.name = name;        
        this.latestModelVersion = null;     
        this.versions = new ArrayList<ModelVersion>();
    }    
    
    public long getId() {
        return this.id;
    }    
    
    public void setId(long id) {
        this.id = id;      
    }
    
    public String getName() {
        return name;
    }
    
    public void setData(ModelData modelData) {
        this.data = modelData;        
    }
    
    public ModelData getData() {
        return this.data;
    }    
    
    public void addVersion(ModelVersion version) {
        this.versions.add(version);
    }
    
    public List<ModelVersion> getVersions() {
        return this.versions;
    }
    
    public ModelVersion getVersionById(long versionId) {        
        for (ModelVersion version : this.versions) {
            if (version.getId() == versionId) {
                return version;
            }
        }
        return null;
    }
    
    public ModelVersion getNextVersion(ModelVersion version) {
        long minDiff = version.getDate();
        ModelVersion nextVersion = this.getLatestVersion();
        
        for (ModelVersion v : this.versions) {
          long diff = v.getDate() - version.getDate();
          if (diff > 0 && diff < minDiff) {
              minDiff = diff;
              nextVersion = v;
          }
        }
        return nextVersion;
    }
        
    public ModelVersion getLatestVersion() {
        if (this.latestModelVersion == null) {
            this.latestModelVersion = this.findLatestModelVersion();
        }
        return this.latestModelVersion;
    }
    
    private ModelVersion findLatestModelVersion() {
        ModelVersion latestVersion = null;
        for (ModelVersion version : this.versions) {            
            if (latestVersion == null || latestVersion.getDate() > version.getDate()) {
                latestVersion = version;
            }
        }
        return latestVersion;
    }
}