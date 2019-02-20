package org.monet.grided.core.configuration.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.monet.grided.control.log.Logger;
import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.configuration.ServerConfiguration;
import org.monet.grided.core.util.StreamHelper;
import org.monet.grided.exceptions.ConfigurationException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ConfigurationImpl implements Configuration {

    private Logger logger;
    private ServerConfiguration serverConfiguration;
    private Properties properties;
    private Properties language;

    @Inject
    public ConfigurationImpl(Logger logger, ServerConfiguration serverConfiguration) {
        this.logger = logger;
        this.serverConfiguration = serverConfiguration;
        this.properties = new Properties();
        this.language   = new Properties();

        readConfigurationProperties(serverConfiguration);
        //readLanguageProperties(serverConfiguration);
    }

    @Override
    public String getImagesPath() {
        return null;
    }

        
    @Override
    public String getProperty(String key) {
        String property = this.properties.getProperty(key);
        if (property == null) {
            this.logger.error(String.format("property '%s' is missing in config file", key));
        }        
        return property;
    }
    
    @Override
    public String getLabel(String key) {
        String label = this.language.getProperty(key);
        if (label == null) {
            this.logger.error(String.format("label %s is missing in language file", key));
        }
        return label;
    }

    private void readConfigurationProperties(ServerConfiguration serverConfiguration) {        
        FileInputStream configurationFileStream = null;

        try {
            String configurationPath = this.serverConfiguration.getUserPath() + File.separator + CONFIGFILE;
            configurationFileStream = new FileInputStream(configurationPath);
            this.properties.loadFromXML(configurationFileStream);

        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new ConfigurationException("Error loading configuration file", e);
        } finally {
            if (configurationFileStream != null) StreamHelper.close(configurationFileStream);
        }                
    }
    
    private void readLanguageProperties(ServerConfiguration serverConfiguration) {
        FileInputStream languageFileStream = null;
        try {
            String languageFile = this.properties.getProperty(Configuration.LANGUAGE) + ".lang";
            String languagePath = this.serverConfiguration.getUserPath() + File.separator +  this.getProperty(Configuration.LANGUAGE_FOLDER) + File.separator + languageFile;
            languageFileStream = new FileInputStream(languagePath);
            this.language.load(languageFileStream);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new ConfigurationException("Error loading language file", e);
        } finally {
            if (languageFileStream != null)  StreamHelper.close(languageFileStream);
        }    
    }
    
}
