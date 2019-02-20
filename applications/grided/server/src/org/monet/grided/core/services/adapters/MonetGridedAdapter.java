package org.monet.grided.core.services.adapters;

import java.util.List;

import org.monet.grided.core.model.PublicationServiceList;
import org.monet.grided.core.services.space.data.Service;

public interface MonetGridedAdapter {
    public PublicationServiceList adapt(List<Service> services);
}

