package org.monet.grided.control.guice.providers;

import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.services.deploy.DeployService;
import org.monet.grided.core.services.deploy.impl.DeployServiceImpl;

import com.google.inject.Inject;
import com.google.inject.Provider;


public class DeployServiceProvider implements Provider<DeployService> {

    private Configuration config;

    @Inject
    DeployServiceProvider(Configuration configuration) {
        this.config = configuration;
    }
    
    @Override
    public DeployService get() {
        String certificateFilename = this.config.getProperty(Configuration.CERTIFICATE_FILENAME);
        String certificatePassword = this.config.getProperty(Configuration.CERTIFICATE_PASSWORD);
        
        DeployService deployService = new DeployServiceImpl(certificateFilename, certificatePassword);
        return deployService;
    }
}

