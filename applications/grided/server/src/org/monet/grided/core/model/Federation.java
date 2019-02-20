package org.monet.grided.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.monet.grided.core.persistence.filesystem.HasData;
import org.monet.grided.core.serializers.xml.federation.FederationData;

public class Federation implements HasData<FederationData>, Component {

    private long id;        
    private String name;
    private Server server;
    private List<Space> spaces;    
    private FederationData federationData;
    private State state;
    
    public Federation(Server server, long id) {
        super();
        this.id = id;
        this.server = server;
        this.spaces = new ArrayList<Space>();
        this.state = State.stopped();
    }
    
    public Federation(Server server) {
        this(server, -1);        
    }
    
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;        
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }    
                        
    public FederationData getData() {
        return this.federationData;
    }
    
    public void setData(FederationData federationData) {
        this.federationData = federationData;
    }    
    
    public Server getServer() {
        return this.server;
    }

    public void add(Space space) {
        this.spaces.add(space);        
    }
    
    public Iterator<Space> iterator() {
        return this.spaces.iterator();
    }

    @Override
    public void start() {
        this.state = State.running(System.currentTimeMillis());        
    }

    @Override
    public void stop() {
        this.state = State.stopped();        
    }

    @Override
    public boolean isRunning() {        
        return this.state.isRunning();
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }
    
    @Override
    public State getState() {
        return this.state;
    }    
}