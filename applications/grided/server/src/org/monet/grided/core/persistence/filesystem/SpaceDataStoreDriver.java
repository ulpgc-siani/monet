package org.monet.grided.core.persistence.filesystem;

import java.io.File;

import org.monet.grided.core.model.Image;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.serializers.xml.space.SpaceData;

public interface SpaceDataStoreDriver {
    
    public void create(long serverId, long federationId, long id, SpaceData data);       
    public void create(long serverId, long federationId, long id, SpaceData data, Image image);
    public SpaceData load(long serverId, long federationId, long id);
    public SpaceData load(File folder);    
    public void save(Space space);    
    public void save(Space space, Image image);    
    public void save(Space space, File file);    
    public void delete(long serverId, long federationId, long id);
    public byte[] loadImage(long serverid, long federationId, long id, String filename);
}

