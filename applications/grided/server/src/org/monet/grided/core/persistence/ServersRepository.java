package org.monet.grided.core.persistence;

import java.util.List;

import org.monet.grided.core.model.Server;

public interface ServersRepository {

    public Server createServer(String name, String ip, boolean enabled);    
    public List<Server> loadServers();        
    public Server loadServer(long id);
    public void save(Server server);
    public void deleteServer(long serverId);
    public void deleteServers(long[] serverIds);

}

