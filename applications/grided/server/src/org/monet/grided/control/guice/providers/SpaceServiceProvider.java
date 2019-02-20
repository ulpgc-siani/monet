package org.monet.grided.control.guice.providers;

import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.services.space.SpaceService;
import org.monet.grided.core.services.space.SpaceServiceMockImpl;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class SpaceServiceProvider implements Provider<SpaceService> {

    private Configuration configuration;

    @Inject
    public SpaceServiceProvider(Configuration configuration) {
        this.configuration = configuration;
    }
    
    @Override
    public SpaceService get() {
        String certificateFilename = this.configuration.getProperty(Configuration.CERTIFICATE_FILENAME);
        String certificatePassword = this.configuration.getProperty(Configuration.CERTIFICATE_PASSWORD);
        
        SpaceService spaceService = new SpaceServiceMockImpl(certificateFilename, certificatePassword);
        return spaceService;        
    }
}

