package org.monet.grided.core.persistence.filesystem;

import java.io.File;

import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.Image;
import org.monet.grided.core.serializers.xml.federation.FederationData;

public interface FederationDataStoreDriver {
    
    public void create(long severId, long federationId, FederationData data);       
    public void create(long serverId, long federationId, FederationData data, Image image);
    public FederationData load(long serverId, long federationId);    
    public FederationData load(File folder);    
    public void save(Federation federation);    
    public void save(Federation federation, Image image);    
    public void save(Federation federation, File file);    
    public void delete(long serverId, long federationId);
    public byte[] loadImage(long serverId, long federationId, String filename);
}

