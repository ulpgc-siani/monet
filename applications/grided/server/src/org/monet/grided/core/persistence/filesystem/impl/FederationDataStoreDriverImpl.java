package org.monet.grided.core.persistence.filesystem.impl;

import java.io.File;

import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.Image;
import org.monet.grided.core.persistence.filesystem.DataStoreDriver;
import org.monet.grided.core.persistence.filesystem.FederationDataStoreDriver;
import org.monet.grided.core.serializers.xml.federation.FederationData;

public class FederationDataStoreDriverImpl implements FederationDataStoreDriver {

    private DataStoreDriver<Federation, FederationData> driver;
    private ServerStoreConfig serverStoreConfig;
    private FederationStoreConfig federationStoreConfig;
    
    public FederationDataStoreDriverImpl(ServerStoreConfig serverStoreConfig, FederationStoreConfig federationStoreConfig, DataStoreDriver<Federation, FederationData> driver) {
        this.serverStoreConfig = serverStoreConfig;
        this.federationStoreConfig = federationStoreConfig;
        this.driver = driver;        
    }
    
    public void create(long serverId, long federationId, FederationData data) {
        this.driver.create(this.getDataPath(serverId, federationId), federationId, data);
    }
            
    public void create(long serverId, long federationId, FederationData data, Image image) {
        this.driver.create(this.getDataPath(serverId, federationId), federationId, data, image);
    }
    
    public FederationData load(long serverId, long federationId) {
        return this.driver.load(this.getDataPath(serverId, federationId), federationId);
    }
    
    public FederationData load(File folder) {
        return this.driver.load(folder);
    }    
    
    public void save(Federation federation) {
        this.driver.save(this.getDataPath(federation), federation);
    }
    
    public void save(Federation federation, Image image) {
        this.driver.save(this.getDataPath(federation), federation, image);
    }
    
    public void save(Federation federation, File file) {
        this.driver.save(this.getDataPath(federation), federation, file);
    }
            
    public void delete(long serverId, long id) {
        this.driver.delete(this.getDataPath(serverId, id));
    }

    public byte[] loadImage(long serverId, long federationId, String filename) {
        return this.driver.loadFile(this.getDataPath(serverId, federationId) + "/images/" + filename);        
    }
    
    
    private String getDataPath(Federation federation) {
        return this.getDataPath(federation.getServer().getId(), federation.getId());
    }    
        
    private String getDataPath(long serverId, long federationId) {
        return this.serverStoreConfig.getPrefix() + serverId + "/" + this.federationStoreConfig.getPrefix() + federationId;
    }
}

