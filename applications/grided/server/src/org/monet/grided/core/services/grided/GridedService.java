package org.monet.grided.core.services.grided;

import java.util.Map;

import org.monet.grided.core.persistence.FederationsRepository;
import org.monet.grided.core.persistence.ModelsRepository;
import org.monet.grided.core.persistence.ServersRepository;
import org.monet.grided.core.persistence.SpacesRepository;

public interface GridedService extends ServersRepository, FederationsRepository, SpacesRepository, ModelsRepository {

    public byte[] loadImage(Map<String, String> parameters);  
    
}

