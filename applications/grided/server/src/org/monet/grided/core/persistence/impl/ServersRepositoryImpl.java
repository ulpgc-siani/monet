package org.monet.grided.core.persistence.impl;

import java.util.List;

import org.monet.grided.core.model.Server;
import org.monet.grided.core.persistence.ServersRepository;
import org.monet.grided.core.persistence.database.impl.ServersProducer;

import com.google.inject.Inject;

public class ServersRepositoryImpl  implements ServersRepository {

    private ServersProducer producer;
    
    @Inject
    public ServersRepositoryImpl(ServersProducer producer) {
        this.producer = producer;    
    }
    
    public Server createServer(String name, String ip, boolean enabled) {
        Server server = new Server(name, ip);        
        server.setEnabled(enabled);
        this.producer.saveServer(server);        
        return server;
    }
    
    @Override
    public List<Server> loadServers() {        
        return this.producer.loadServers();        
    }

    @Override
    public Server loadServer(long id) {        
        Server server = this.producer.loadServer(id);                
        return server;
    }

    @Override
    public void save(Server server) {
        this.producer.saveServer(server);          
    }

    @Override
    public void deleteServer(long serverId) {
        this.producer.deleteServer(serverId);          
    }

    @Override
    public void deleteServers(long[] serverIds) {
        this.producer.deleteServers(serverIds);          
    }    
}

