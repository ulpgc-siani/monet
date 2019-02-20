package org.monet.grided.core.persistence.impl;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.Image;
import org.monet.grided.core.model.ModelVersion;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.persistence.SpacesRepository;
import org.monet.grided.core.persistence.database.impl.FederationsProducer;
import org.monet.grided.core.persistence.database.impl.SpacesProducer;
import org.monet.grided.core.persistence.filesystem.SpaceDataStoreDriver;
import org.monet.grided.core.serializers.xml.Organization;
import org.monet.grided.core.serializers.xml.space.SpaceData;
import org.monet.grided.core.util.ResourceConstants;
import org.monet.grided.core.util.Resources;

import com.google.inject.Inject;

public class SpacesRepositoryImpl implements SpacesRepository {

    private SpacesProducer spacesProducer;
    private SpaceDataStoreDriver driver;    
    private FederationsProducer federationsProducer;
    private Filesystem filesystem;
    
    @Inject
    public SpacesRepositoryImpl(SpacesProducer producer, FederationsProducer federationsProducer, SpaceDataStoreDriver driver) {
        this.spacesProducer = producer;
        this.federationsProducer = federationsProducer;        
        this.driver = driver;
    }
    
    @Inject
    public void injectFilesystem(Filesystem filesystem) {
        this.filesystem = filesystem;
    }
    
    @Override
    public Space createSpace(long serverId, long federationId, String spaceName, String url, ModelVersion modelVersion) {
        Federation federation = this.federationsProducer.loadFederation(federationId);
        Space space = new Space(federation);
        space.setName(spaceName);
        space.setModelVersion(modelVersion);
        
        SpaceData data = new SpaceData();
        Organization organization = new Organization(space.getName());
        organization.setUrl(url);
        data.setOrganization(organization);        
        space.setData(data);        
        
        this.spacesProducer.saveSpace(space);
        this.driver.create(serverId, federationId, space.getId(), data);
                
        return space;
    }

    @Override
    public Space createSpace(Space space, Image image) {
        this.spacesProducer.saveSpace(space);
        this.driver.create(space.getFederation().getServer().getId(), space.getFederation().getId() ,space.getId(), space.getData(), image);
        return space;
    }
       
    
    @Override
    public Space loadSpace(long id) {
        Space space = this.spacesProducer.loadSpace(id);
        space.setData(this.driver.load(space.getFederation().getServer().getId(), space.getFederation().getId(), id));
        return space;        
    }
    
    public List<Space> loadFederationSpaces(long federationId) {
        return this.spacesProducer.loadFederationSpaces(federationId);
    }

    public List<Space> loadServerSpaces(long serverId) {
        return this.spacesProducer.loadServerSpaces(serverId);
    }
    
    @Override
    public List<Space> loadSpacesWithModel(long modelId) {
        List<Space> spaces = this.spacesProducer.loadSpacesWithModel(modelId);
        for (Space space : spaces) {
            Federation federation = space.getFederation();
            SpaceData data = this.driver.load(federation.getServer().getId(), federation.getId(), space.getId());
            space.setData(data);
        }
        return spaces;
    }
    
    public List<Space> loadSpacesByModelVersion(long modelVersionId) {
        List<Space> spaces = this.spacesProducer.loadSpacesByModelVersion(modelVersionId);
        return spaces;
    }
    
    @Override
    public void saveSpace(Space space, File folder) {
        this.spacesProducer.saveSpace(space);
        this.driver.save(space, folder);        
    }

    @Override
    public void saveSpace(Space space, Image image) {
        this.spacesProducer.saveSpace(space);
        this.driver.save(space, image);        
    }
    
    @Override
    public void saveSpace(Space space) {
        this.spacesProducer.saveSpace(space);     
        this.driver.save(space);
    }

    @Override
    public void deleteSpace(long id) {
        Federation federation = this.federationsProducer.loadFederationOfSpace(id);   
        this.spacesProducer.deleteSpace(id);
        this.driver.delete(federation.getServer().getId(), federation.getId(), id);
    }
    
    @Override
    public void deleteSpaces(long[] ids) {
        for (long id : ids) {
            this.deleteSpace(id);
        }
    }
       
    @Override
    public byte[] loadSpaceImage(long serverId, long federationId, long id, String filename) {        
        byte[] bytes = this.driver.loadImage(serverId, federationId, id,  filename);
        if (bytes == null) {
            InputStream stream = Resources.getAsStream(ResourceConstants.FEDERATION_NO_IMAGE);
            bytes = this.filesystem.getBytesFromInputStream(stream); 
        }
        return bytes;
    }            
}