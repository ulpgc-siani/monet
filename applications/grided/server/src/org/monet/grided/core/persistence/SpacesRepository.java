package org.monet.grided.core.persistence;

import java.io.File;
import java.util.List;

import org.monet.grided.core.model.Image;
import org.monet.grided.core.model.ModelVersion;
import org.monet.grided.core.model.Space;

public interface SpacesRepository {

    public Space createSpace(long serverId, long federationId, String spaceName, String url, ModelVersion modelVersion);
    public Space createSpace(Space space, Image image);
    
    public Space loadSpace( long id);    
    public List<Space> loadFederationSpaces(long federationId);
    public List<Space> loadServerSpaces(long serverId);
    public List<Space> loadSpacesWithModel(long modelId);
    
    public void saveSpace(Space space, File folder);
    public void saveSpace(Space space);
    public void saveSpace(Space space, Image image);    
    
    public void deleteSpace(long id);
    public void deleteSpaces(long[] ids);
    
    public byte[] loadSpaceImage(long serverId, long federationId, long id, String filename);    
}

