package org.monet.grided.core.services.adapters.impl;

import java.util.List;

import org.monet.grided.core.model.PublicationService;
import org.monet.grided.core.model.PublicationServiceList;
import org.monet.grided.core.services.adapters.MonetGridedAdapter;
import org.monet.grided.core.services.space.data.Service;

public class MonetGridedAdapterImpl implements MonetGridedAdapter {

    @Override
    public PublicationServiceList adapt(List<Service> services) {
        PublicationServiceList publicationServices = new PublicationServiceList();
        
        for (Service service : services) {
            PublicationService publicationService = new PublicationService(service.getName(), service.getType());
            publicationServices.add(publicationService);
        }
        return publicationServices;
    }    
}

