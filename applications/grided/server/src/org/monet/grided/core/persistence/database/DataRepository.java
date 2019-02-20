package org.monet.grided.core.persistence.database;


import java.io.File;
import java.util.List;

import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.model.Space;


public interface DataRepository {
    
    public List<Server> loadServers();
    public Server loadServer(long serverId);
    public void saveServer(Server server);
    public void deleteServer(long serverId);    
    
    //public List<Federation> loadFederations(long serverId);
    public Federation createFederation(long serverId, File file);
    public Federation loadFederation(long serverId, long id);
    public void saveFederation(Federation federation, File file);
    public void deleteFederation(long serverId, long id);    
    
    public List<Space> loadSpaces(long serverId);
    public Space loadSpace(long serverId, long federationId, long id);
    public void saveSpace(Space space);
    public void deleteSpace(long serverId, long federationId, long id);
   
}
