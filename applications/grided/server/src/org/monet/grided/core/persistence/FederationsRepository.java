package org.monet.grided.core.persistence;

import java.util.List;

import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.Image;

public interface FederationsRepository {
    
    public Federation createFederation(long serverId, String federationName, String url);     
    public Federation createFederation(Federation federation, Image image);    
    
    public Federation loadFederation(long id);
    public List<Federation> loadFederations(long serverId);
    public List<Federation> loadAllFederations();
    
    public void saveFederation(Federation federation);
    public void saveFederation(Federation federation, Image image);
    
    public void deleteFederation(long id);    
    public void deleteFederations(long[] ids);
    
    public byte[] loadFederationImage(long serverId, long id, String filename);    
}

