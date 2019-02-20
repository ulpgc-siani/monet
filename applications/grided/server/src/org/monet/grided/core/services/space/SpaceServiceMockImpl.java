package org.monet.grided.core.services.space;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.monet.api.space.setupservice.SpaceSetupApi;
import org.monet.api.space.setupservice.impl.SpaceSetupApiImpl;
import org.monet.grided.core.services.space.data.Service;
import org.monet.grided.core.services.space.data.ServiceType;

import com.google.inject.Inject;

public class SpaceServiceMockImpl implements SpaceService {

    private String certificateFilename;
    private String certificatePassword;

    @Inject
    public SpaceServiceMockImpl(String certificateFilename, String certificatePassword) {
        this.certificateFilename = certificateFilename;
        this.certificatePassword = certificatePassword;       
    }
    
    public String getVersion(String location) {
        //SpaceSetupApi api = new SpaceSetupApiImpl(location, this.certificateFilename, this.certificatePassword);
        //return api.getVersion();
        
        String version = "2.0.0";
        if (location.contains("107")) version = "2.1.0";
        return version;
    }
    
    public void updateModel(String location, InputStream stream) {
      SpaceSetupApi api = new SpaceSetupApiImpl(location, this.certificateFilename, this.certificatePassword);
      api.updateModel(stream);
    }
    
    @Override
    public List<Service> loadServices(String ip) {
        @SuppressWarnings("serial")
        List<Service> services = new ArrayList<Service>() {{
          add(new Service("incidencias1", ServiceType.thesaurus));
          add(new Service("incidencias2", ServiceType.thesaurus));
          add(new Service("incidencias3", ServiceType.thesaurus));
          add(new Service("incidencias4", ServiceType.thesaurus));
          add(new Service("incidencias5", ServiceType.thesaurus));
                    
          add(new Service("coordinacion1", ServiceType.service));
          add(new Service("coordinacion2", ServiceType.service));
          add(new Service("coordinacion3", ServiceType.service));
          add(new Service("coordinacion4", ServiceType.service));
          add(new Service("coordinacion5", ServiceType.service));
        }};
        return services;
    }
}

