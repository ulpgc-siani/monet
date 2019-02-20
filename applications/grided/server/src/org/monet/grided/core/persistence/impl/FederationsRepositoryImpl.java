package org.monet.grided.core.persistence.impl;

import java.io.InputStream;
import java.util.List;

import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.Image;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.persistence.FederationsRepository;
import org.monet.grided.core.persistence.database.impl.FederationsProducer;
import org.monet.grided.core.persistence.database.impl.ServersProducer;
import org.monet.grided.core.persistence.filesystem.FederationDataStoreDriver;
import org.monet.grided.core.serializers.xml.Organization;
import org.monet.grided.core.serializers.xml.federation.FederationData;
import org.monet.grided.core.util.ResourceConstants;
import org.monet.grided.core.util.Resources;

import com.google.inject.Inject;

public class FederationsRepositoryImpl implements FederationsRepository {
    
   private FederationsProducer federationsProducer;
   private ServersProducer serversProducer;
   private FederationDataStoreDriver driver;
   private Filesystem filesystem;
   

   @Inject
   public FederationsRepositoryImpl(FederationsProducer federationsProducer, ServersProducer serversProducer, FederationDataStoreDriver driver) {
       this.federationsProducer = federationsProducer;
       this.serversProducer = serversProducer;
       this.driver = driver;
   } 

   @Inject
   public void injectFilesystem(Filesystem filesystem) {
       this.filesystem = filesystem;
   }

    public Federation createFederation(long serverId, String federationName, String url) {                
        Server server = this.serversProducer.loadServer(serverId);
        Federation federation = new Federation(server);
        federation.setName(federationName);     
        
        FederationData data = new FederationData();
        Organization organization = new Organization(federation.getName());
        organization.setUrl(url);
        data.setOrganization(organization);        
        federation.setData(data);
                               
        this.federationsProducer.saveFederation(federation);
        this.driver.create(server.getId(), federation.getId(), federation.getData());
                      
        return federation;
    }
    
    public Federation createFederation(Federation federation, Image image) {                
        this.federationsProducer.saveFederation(federation);
        this.driver.create(federation.getServer().getId(), federation.getId(), federation.getData(), image);                      
        return federation;
    }

        
    @Override
    public Federation loadFederation(long id) {                
        Federation federation = this.federationsProducer.loadFederation(id);
        federation.setData(this.driver.load(federation.getServer().getId(), federation.getId()));
        return federation;
    }
    
    @Override
    public void saveFederation(Federation federation) {
        this.federationsProducer.saveFederation(federation);  
        this.driver.save(federation);
    }
    
    @Override
    public void saveFederation(Federation federation, Image image) {
        this.federationsProducer.saveFederation(federation);
        this.driver.save(federation, image);
    }

    @Override
    public void deleteFederation(long id) {
        long serverId = this.serversProducer.loadServerOfFederation(id);
        this.federationsProducer.deleteFederation(id);        
        this.driver.delete(serverId, id);
    }
    
    @Override
    public List<Federation> loadFederations(long serverId) {
        return this.federationsProducer.loadFederations(serverId);        
    }

    @Override
    public List<Federation> loadAllFederations() {
        return this.federationsProducer.loadAllFederations();
    }
    
    @Override
    public void deleteFederations(long[] ids) {                
        for (long id : ids) {
            this.deleteFederation(id);
        }
    } 
    
    @Override
    public byte[] loadFederationImage(long serverId, long federationId, String filename) {
        byte[] bytes = this.driver.loadImage(serverId, federationId, filename);
        if (bytes == null) {
            InputStream stream = Resources.getAsStream(ResourceConstants.FEDERATION_NO_IMAGE);
            bytes = this.filesystem.getBytesFromInputStream(stream);
        }
        return bytes;
    }            
}

