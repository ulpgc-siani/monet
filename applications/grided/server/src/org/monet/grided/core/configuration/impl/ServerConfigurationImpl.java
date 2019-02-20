package org.monet.grided.core.configuration.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.PropertyConfigurator;
import org.monet.grided.core.configuration.ServerConfiguration;
import org.monet.grided.core.util.StreamHelper;
import org.monet.grided.exceptions.ConfigurationException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ServerConfigurationImpl implements ServerConfiguration {

    private String userPath;
    private String appDataPath;
    private String resourcesPath;

    @Inject
    public ServerConfigurationImpl(ServletContext context) {
        this.userPath      = System.getProperty("user.home") + File.separator + "." + context.getServletContextName();
        this.appDataPath   = context.getRealPath("WEB-INF/app_data");
        this.resourcesPath = context.getRealPath("resources");

        File fUserFolder = new File(this.userPath);
        if (!fUserFolder.exists()) {
            throw new ConfigurationException(String.format("Fatal error: No configuration directory found (%s)", this.userPath));
        }

        String configurationPath = this.userPath + File.separator + "log4j.grided.config";
        configure(configurationPath);
    }

    @Override
    public String getUserPath() {
        return this.userPath;
    }

    @Override
    public String getAppDataPath() {
        return this.appDataPath;
    }

    @Override
    public String getResourcePath() {
        return this.resourcesPath;
    }

    private void configure(String configurationPath) {
        FileInputStream configurationFileStream = null;
        try {
            Properties properties = new Properties();
            configurationFileStream = new FileInputStream(configurationPath);
            properties.loadFromXML(configurationFileStream);
            PropertyConfigurator.configure(properties);
        } catch (Exception e) {
            throw new ConfigurationException("Error loading log4j configuration file", e);
        } finally {
            StreamHelper.close(configurationFileStream);
        }
    }

}
