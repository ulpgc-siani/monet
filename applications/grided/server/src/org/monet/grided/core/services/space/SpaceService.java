package org.monet.grided.core.services.space;

import java.io.InputStream;
import java.util.List;

import org.monet.grided.core.services.space.data.Service;

public interface SpaceService {     
    
    public String getVersion(String location);
    public List<Service> loadServices(String serverIP);
    public void updateModel(String url, InputStream stream);
    
}