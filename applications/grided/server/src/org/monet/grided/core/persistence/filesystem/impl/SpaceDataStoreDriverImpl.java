package org.monet.grided.core.persistence.filesystem.impl;

import java.io.File;

import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.Image;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.persistence.filesystem.DataStoreDriver;
import org.monet.grided.core.persistence.filesystem.SpaceDataStoreDriver;
import org.monet.grided.core.serializers.xml.space.SpaceData;

public class SpaceDataStoreDriverImpl implements SpaceDataStoreDriver {

    private DataStoreDriver<Space, SpaceData> driver;
    private SpaceStoreConfig spaceStoreConfig;
    private FederationStoreConfig federationStoreConfig;
    private ServerStoreConfig serverStoreConfig;
        
    public SpaceDataStoreDriverImpl(ServerStoreConfig serverStoreConfig, FederationStoreConfig federationStoreConfig, SpaceStoreConfig spaceStoreConfig, DataStoreDriver<Space, SpaceData> driver) {
        this.serverStoreConfig = serverStoreConfig;
        this.federationStoreConfig = federationStoreConfig;
        this.spaceStoreConfig= spaceStoreConfig;
        this.federationStoreConfig = federationStoreConfig;
        this.driver = driver;        
    }
    
    public void create(long serverId, long federationId, long id, SpaceData data) {
        this.driver.create(this.getDataPath(serverId, federationId, id), id, data);
    }
            
    public void create(long serverId, long federationId, long id, SpaceData data, Image image) {
        this.driver.create(this.getDataPath(serverId, federationId, id), id, data, image);
    }
    
    public SpaceData load(long serverId, long federationId, long id) {
        return this.driver.load(this.getDataPath(serverId, federationId, id), id);
    }
    
    public SpaceData load(File folder) {
        return this.driver.load(folder);
    }    
    
    public void save(Space space) {
        this.driver.save(this.getDataPath(space), space);
    }
    
    public void save(Space space, Image image) {
        this.driver.save(this.getDataPath(space), space, image);
    }
    
    public void save(Space space, File file) {
        this.driver.save(this.getDataPath(space), space, file);
    }
       
    public void delete(long serverId, long federationId, long id) {
        this.driver.delete(this.getDataPath(serverId, federationId, id));
    }

    @Override
    public byte[] loadImage(long serverId, long federationId, long id, String filename) {
        return this.driver.loadFile(this.getDataPath(serverId, federationId, id) + "/images/" + filename);        
    }
    
    
    private String getDataPath(Space space) {
        Federation federation = space.getFederation();
        long federationId = federation.getId();
        long serverId = space.getFederation().getServer().getId();
        
        return getDataPath(serverId, federationId, space.getId());
    }
    
    private String getDataPath(long serverId, long federationId, long spaceId) {
        return this.serverStoreConfig.getPrefix() + serverId + "/" + this.federationStoreConfig.getPrefix() + federationId + "/" + this.spaceStoreConfig.getPrefix() + spaceId;
    }
}

