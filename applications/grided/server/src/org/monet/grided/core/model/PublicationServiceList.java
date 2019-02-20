package org.monet.grided.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.monet.grided.core.services.space.data.ServiceType;

public class PublicationServiceList implements Iterable<PublicationService> {

    private List<PublicationService> services;

    public PublicationServiceList() {
        this.services = new ArrayList<PublicationService>();
    }
    
    public void add(PublicationService service) {
        this.services.add(service);
    }
    
    public void remove(PublicationService service) {
        this.services.remove(service);
    }    
    
    public Iterator<PublicationService> iterator() {
        return this.services.iterator();
    }
    
    public boolean containsItemWith(String name, ServiceType type) {
        PublicationService service = new PublicationService(name, type);
        return this.services.contains(service); 
    }
    
    public PublicationService getItemWith(String name, ServiceType type) {
        PublicationService aService = new PublicationService(name, type);
        for (PublicationService service : this.services) {
            if (service.equals(aService)) return service;
        }
        return null;        
    }
}

