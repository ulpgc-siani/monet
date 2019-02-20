package org.monet.grided.core.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.monet.grided.core.services.space.data.ServiceType;

public class PublicationService {

    private String name;
    private ServiceType type;
    private boolean isPublished;

    public PublicationService(String name, ServiceType type) {
        this.name = name;
        this.type = type;
        this.isPublished = false;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ServiceType getType() {
        return this.type;
    }
    
    public void publish() {
        this.isPublished = true;
    }
    
    public void unPublish() {
        this.isPublished = false;
    }
    
    public boolean isPublished() {
        return this.isPublished;
    }
    
    @Override
    public boolean equals(Object object) {
      if (!(object instanceof PublicationService)) return false;
      if (object == this) return true;
      return equals((PublicationService) object);
    }
    
    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.name);
        builder.append(this.type);
        return builder.toHashCode();
    }
    
    private boolean equals(PublicationService statement) {
        return this.name.equals(statement.getName()) && this.type.equals(statement.getType());
    }
}

