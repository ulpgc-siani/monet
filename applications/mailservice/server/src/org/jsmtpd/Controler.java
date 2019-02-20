/*
 * 
 * Jsmtpd, Java SMTP daemon
 * Copyright (C) 2005  Jean-Francois POUX, jf.poux@laposte.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package org.jsmtpd;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.config.ConfigErrorException;
import org.jsmtpd.config.ElementNotFoundException;
import org.jsmtpd.config.ModuleNotFoundException;
import org.jsmtpd.config.PluginLoader;
import org.jsmtpd.config.PluginLoaderException;
import org.jsmtpd.config.ReadConfig;
import org.jsmtpd.core.common.IGenericPlugin;
import org.jsmtpd.core.common.PluginStore;
import org.jsmtpd.core.receive.Receiver;
import org.jsmtpd.core.receive.SslReceiver;
import org.jsmtpd.core.send.DeliveryPicker;
import org.xml.sax.SAXException;

/**
 * Initialises config & plugins<br>
 * Starts and shutdown listening service and delivery service<br>
 * @author Jean-Francois POUX
 */
public class Controler extends Thread {
    private Log log = LogFactory.getLog(Controler.class);
    private Receiver receiver = null;    
    private SslReceiver sslReceiver = null;
    private DeliveryPicker delivery = null;

    private String configFile = "etc/jsmtpd.ini";
    private String xmlPluginFileName = "jsmtpd-plugin-config.xml";
    
    public synchronized void sleep() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            log.error(e);
        }
    }
    
    public void startup() {
        Runtime.getRuntime().addShutdownHook(this);
        if (!init()) {
            System.exit(-1);
            return;
        }

        if (!initReceiver()) {
            System.exit(-1);
            return;
        }
        if (!initDeliveryService()) {
            System.exit(-1);
            return;
        }
        log.info("Server running");
        
        sleep();
    }

    public boolean initDeliveryService() {
        try {
            delivery = new DeliveryPicker();
        } catch (Exception e) {
            return false;
        }
        log.info("Delivery service started");
        return true;
    }

    public boolean init() {
        try {
                ReadConfig.getInstance().loadConfig(configFile);
        } catch (ConfigErrorException e) {
            log.fatal("Could no load config file ",e);
            return false;
        }
        log.info("Jsmtpd " + ReadConfig.getInstance().getProperty("version") + ", Copyright (C) 2005 Jean-Francois Poux - jfp@jsmtpd.org");
        log.info("Jsmtpd comes with ABSOLUTELY NO WARRANTY;");
        log.info("This is free software, and you are welcome to redistribute it under certain conditions;");
        log.info("See the LICENCE file for details");
        log.info("Server starting ...");
                    
        // Init plugin Loader

        PluginLoader pluginLoader = new PluginLoader();
        pluginLoader.setXmlFileName(xmlPluginFileName);
        try {
            pluginLoader.init();
        } catch (SAXException e2) {
            log.fatal("Parse error in plugin-config.xml, " + e2.getMessage());
            return false;
        } catch (IOException e2) {
            log.fatal("IO Error while loading, " + e2.getMessage());
            return false;
        } catch (ParserConfigurationException e2) {
            log.fatal("Parser configuration error while loading plugin-config.xml, " + e2.getMessage());
            return false;
        } catch (ElementNotFoundException e2) {
            log.fatal("In plugin-config.xml, can't find " + e2.getElementName());
            return false;
        }

        try {
            pluginLoader.loadDNSService();
        } catch (PluginLoaderException e) {
            log.fatal("DNSService could not be loaded, " + e.getMessage(),e);
            return false;
        }
        log.info("DNS Service plugin loaded : " + PluginStore.getInstance().getResolver().getPluginName());

        try {
            pluginLoader.loadACL();
        } catch (PluginLoaderException e) {
            log.fatal("ACL plugin could not be loaded, " + e.getMessage(),e);
            return false;
        }
        log.info("ACL Service plugin loaded : " + PluginStore.getInstance().getAcl().getPluginName());

        try {
            pluginLoader.loadLocalDeliveryService();
        } catch (PluginLoaderException e) {
            log.fatal("LocalDeliveryService plugin could not be loaded, " + e.getMessage(),e);
            return false;
        }
        log.info("Local Delivery Service plugin loaded : " + PluginStore.getInstance().getLocalDeliveryService().getPluginName());

        try {
            pluginLoader.loadRemoteDeliveryService();
        } catch (PluginLoaderException e) {
            log.fatal("RemoteDeliveryService plugin could not be loaded, " + e.getMessage(),e);
            return false;
        }
        log.info("Remote Delivery Service plugin loaded : " + PluginStore.getInstance().getRemoteDeliveryService().getPluginName());

        try {
            pluginLoader.loadFilters();
        } catch (PluginLoaderException e) {
            log.fatal("Filter plugin could not be loaded, " + e.getMessage(),e);
            return false;
        }

        try {
            pluginLoader.loadSmtpExtensions();
        } catch (PluginLoaderException e) {
            log.fatal("Smtp extension plugin could not be loaded, " + e.getMessage(),e);
            return false;
        } 
        
        List<IGenericPlugin> tempList = PluginStore.getInstance().getFilterList();
        for (IGenericPlugin genericPlugin : tempList) {
        	log.info("Plugin loaded : " + genericPlugin.getPluginName());
		}

        try {
            pluginLoader.loadInputIPFilterChain();
        } catch (ModuleNotFoundException e4) {
            log.fatal( "Error while loading input IP filter chain , " + e4.getModName()
                    + " is declared in the inputIPFilter, but not configured in filtersetup");
        }
        log.info("Input ip filter chain loaded");
        
        try {
            pluginLoader.loadFilterTree();
        } catch (ModuleNotFoundException e1) {
            log.fatal("Error while loading filter tree, " + e1.getModName()
                    + " is declared in the filterchain tree, but not configured in filtersetup");
            return false;
        }
        return true;
    }

    public boolean initReceiver() {
        try {
            receiver = new Receiver(ReadConfig.getInstance().getIntegerProperty("rPort"), ReadConfig.getInstance().getIntegerProperty("rMaxInstances"));
            if ("on".equals(ReadConfig.getInstance().getProperty("ssl"))) {
            	int sslPort = Integer.parseInt(ReadConfig.getInstance().getProperty("sslPort"));
            	sslReceiver = new SslReceiver(sslPort, ReadConfig.getInstance().getIntegerProperty("rMaxInstances"));
            }
        } catch (IOException e1) {
            log.info("Network Listener is down");
            log.error(e1);
            return false;
        } catch (Exception e) {
        	log.error(e);
            return false;
        }
        return true;
    }

    private void shutdown() {
        if (receiver != null) {
            receiver.shutdown();
        }

        if (sslReceiver != null) {
        	sslReceiver.shutdown();
        }
        
        if (delivery != null) {
            delivery.shutdown();
        }

        List<IGenericPlugin> plugins = PluginStore.getInstance().getFilterList();
        for (IGenericPlugin module : plugins) {
        	log.info( "Shutting down plugin : " + module.getPluginName());
            module.shutdownPlugin();
		}
        IGenericPlugin module = PluginStore.getInstance().getResolver();
        log.debug("Shutting down plugin : " + module.getPluginName());
        module.shutdownPlugin();

        log.info("Jsmtpd shutdown");
    }

    public void run() {
        shutdown();
    }

    public String getXmlPluginFileName() {
        return xmlPluginFileName;
    }
    public void setXmlPluginFileName(String xmlPluginFileName) {
        this.xmlPluginFileName = xmlPluginFileName;
    }
    public String getConfigFile() {
        return configFile;
    }
    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }
}