package org.monet.space.kernel.configuration;

import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.constants.SiteFiles;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.model.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigurationLoader {

    public static void load(ConfigurationMap map) {
        new Configuration(map);
    }

    public static String userHomeConfigurationFile() {
        Context oContext = Context.getInstance();
        String sFrameworkName = oContext.getFrameworkName();
        String sFilename = System.getProperty("user.home") + "/." + sFrameworkName + "/configuration/monet" + SiteFiles.CONFIG;

        if (!AgentFilesystem.existFile(sFilename))
            sFilename = oContext.getFrameworkDir() + "/WEB-INF/" + SiteFiles.CONFIG;

        return sFilename;
    }

}