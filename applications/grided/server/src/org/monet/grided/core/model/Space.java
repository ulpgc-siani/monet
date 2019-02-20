package org.monet.grided.core.model;

import org.monet.grided.core.persistence.filesystem.HasData;
import org.monet.grided.core.serializers.xml.space.SpaceData;

public class Space implements HasData<SpaceData>, Component {
    
    public long id;
    private String name;
    private Federation federation;
    private SpaceData data;    
    private ModelVersion modelVersion;
    private State state;
    
    public Space(Federation federation, long id) {
        this.federation = federation;
        this.id = id;                        
        this.state = State.stopped();        
    }
    
    public Space(Federation federation) {
        this(federation, -1);        
    }
    
    public Space(long id) {
        this(null, id);
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
    
    public Federation getFederation() {
        return this.federation;
    }
    
    public void setModelVersion(ModelVersion modelVersion) {
        this.modelVersion = modelVersion;
    }
    
    public ModelVersion getModelVersion() {
        return this.modelVersion;
    }
    
    @Override
    public SpaceData getData() {
        return this.data;
    }

    @Override
    public void setData(SpaceData data) {
        this.data = data;        
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