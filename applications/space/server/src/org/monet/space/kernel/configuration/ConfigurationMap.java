package org.monet.space.kernel.configuration;

import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.exceptions.SystemException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigurationMap extends HashMap<String, String> {

    public static ConfigurationMap fromMap(Map<String, String> map) {
        ConfigurationMap result = new ConfigurationMap();
        for (Entry<String, String> entry : map.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
	}

    public static ConfigurationMap fromArgs(String[] args) {
        ConfigurationMap map = new ConfigurationMap();
        for (String arg : args) {
            if (!arg.contains("=")) continue;
            String key = arg.substring(0, arg.indexOf("="));
            String value = arg.substring(arg.indexOf("=") + 1);
            map.put(key, value);
        }
        return map;
    }

    public static ConfigurationMap fromProperties(Properties properties) {
        return new ConfigurationMap().propertiesToMap(properties);
    }

    public static ConfigurationMap fromXml(String sFilename) {
        InputStream isConfigFileContent;

        if (!AgentFilesystem.existFile(sFilename)) {
            throw new SystemException("Configuration file not found", sFilename);
        }

        isConfigFileContent = AgentFilesystem.getInputStream(sFilename);

        Properties properties = new Properties();
        try {
            properties.loadFromXML(isConfigFileContent);
            isConfigFileContent.close();
            return fromProperties(properties);
        } catch (IOException exception) {
            AgentLogger.getInstance().error(new Exception("Exception in 'Configuration' (" + sFilename + ") : " + exception.getMessage(), exception));
        }

        return null;
    }

    public static ConfigurationMap fromUserHome() {
        return fromXml(ConfigurationLoader.userHomeConfigurationFile());
    }

    private ConfigurationMap propertiesToMap(Properties properties) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            put(entry.getKey().toString(), entry.getValue().toString());
        }
        return this;
    }
}