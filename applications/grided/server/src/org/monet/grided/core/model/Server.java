package org.monet.grided.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.monet.api.deploy.setupservice.impl.model.ServerState;

public class Server {
    private long id;
    private String name;
    private String ip;
    private boolean enabled;
    private List<Federation> federations;
    private ServerState serverState;    
    
    public Server(long id, String name, String ip) {        
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.federations = new ArrayList<Federation>();
        this.serverState = new ServerState();
    }

    public Server(String name, String ip) {
        this(-1, name, ip);
        this.enabled = false;
    }
    
    public Server() {
        this(-1, "No name", "");
    }    

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;        
    }          
    
    public String getIp() {
        return this.ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public void setEnabled(boolean value) {
        this.enabled = value;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setState(ServerState serverState) {
        this.serverState = serverState;       
    }
    
    public ServerState getServerState() {
        return this.serverState;
    }
        
    public void add(Federation federation) {
        this.federations.add(federation);        
    }
    
    public Iterator<Federation> iterator() {
        return this.federations.iterator();
    }
}
