package org.monet.deployservice_engine.utils;

import java.io.File;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.Files;
import org.monet.deployservice.utils.Shell;
//import org.monet.deployservice.utils.StringUtils;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.xml.DocServersXML;
import org.monet.deployservice_engine.xml.ServersXML;

public class RestartServers {

	private Logger logger;
	private boolean debug_monet;
	private boolean debug_docserver;

	public RestartServers() {
		force_not_debug();
		logger = Logger.getLogger(this.getClass());
	}

	public void force_not_debug() {
		debug_monet = false;
		debug_docserver = false;		
	}
	
	public boolean container_monet_start(String server, String container) throws Exception {
		boolean result = false;
		logger.info("Monet was debug: " + debug_monet);

		if (debug_monet) {
    	result = container_monet_start_debug(server, container);
    } else {
			ServersXML serversXML = new ServersXML();
			Item servers = serversXML.unserialize(Configuration.getServersConfig());
	
			String command = servers.getItem(server).getItem(container).getProperty("script-start");
	    Shell shell = new Shell();
	    Integer exitValue = shell.executeCommand(command, new File("."), 1); 
	    if (exitValue > 0) {
				String error = "Failed to execute command. Exit value: " + exitValue;
				logger.error(error);
				throw new Exception(error);    	
	    }
			
			result = true;
    }

		debug_monet = false;
		return result;
	}

	public boolean container_monet_start_debug(String server, String container) throws Exception {
		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());

		String command = servers.getItem(server).getItem(container).getProperty("script-start-debug");

		Shell shell = new Shell();
    Integer exitValue = shell.executeCommand(command, new File("."), 1); 
    if (exitValue > 0) {
			String error = "Failed to execute command. Exit value: " + exitValue;
			logger.error(error);
			throw new Exception(error);    	
    }
		
		return true;
	}
	
	public boolean container_monet_stop(String server, String container) throws Exception {
		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());

		debug_monet = IsDebug(servers.getItem(server).getItem(container).getProperty("user"));
		logger.info("Monet is debug: " + debug_monet);
		
		String command = servers.getItem(server).getItem(container).getProperty("script-stop");

		Shell shell = new Shell();
    Integer exitValue = shell.executeCommand(command, new File("."), 1); 
    if (exitValue > 0) {
			String error = "Failed to execute command. Exit value: " + exitValue;
			logger.error(error);
//			throw new Exception(error);    	
    }
		
		String webappsPath = servers.getItem(server).getItem(container).getProperty("path") + "/webapps";		
		Files files = new Files();
		String[] directories = files.dirList(webappsPath);
		for (int i = 0; i < directories.length; i++) {
			if (files.isDirectory(directories[i])) {
			  logger.info("Deleting dir: " + directories[i]);
		    files.removeDir(directories[i]);
			}
		}		
		
		return true;
	}

	public boolean container_docserver_start(String docserverServer, String docserverContainer) throws Exception {
		boolean result = false;
		logger.info("Docservice was debug: " + debug_docserver);
		
    if (debug_docserver) {
    	result = container_docserver_start_debug(docserverServer, docserverContainer);
    } else {
			DocServersXML docserversXML = new DocServersXML();
			Item docservers = docserversXML.unserialize(Configuration.getDocServersConfig());
	
			String command = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("script-start");
			if (!command.equals("")) {
		    Shell shell = new Shell();
		    Integer exitValue = shell.executeCommand(command, new File("."), 1); 
		    if (exitValue > 0) {
					String error = "Failed to execute command. Exit value: " + exitValue;
					logger.error(error);
					throw new Exception(error);    	
		    }
			}
			result = true;
    }

    debug_docserver = false;
		return result;
	}

	public boolean container_docserver_start_debug(String docserverServer, String docserverContainer) throws Exception {
		DocServersXML docserversXML = new DocServersXML();
		Item docservers = docserversXML.unserialize(Configuration.getDocServersConfig());

		String command = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("script-start-debug");

		Shell shell = new Shell();
    Integer exitValue = shell.executeCommand(command, new File("."), 1); 
    if (exitValue > 0) {
			String error = "Failed to execute command. Exit value: " + exitValue;
			logger.error(error);
			throw new Exception(error);    	
    }

		return true;
	}
	
	public boolean container_docserver_stop(String docserverServer, String docserverContainer) throws Exception {	
		DocServersXML docserversXML = new DocServersXML();
		Item docservers = docserversXML.unserialize(Configuration.getDocServersConfig());

		debug_docserver = IsDebug(docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("user"));		
		logger.info("Docservice is debug: " + debug_docserver);
		
		String command = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("script-stop");
		if (!command.equals("")) {
			Shell shell = new Shell();
	    Integer exitValue = shell.executeCommand(command, new File("."), 1); 
	    if (exitValue > 0) {
				String error = "Failed to execute command. Exit value: " + exitValue;
				logger.error(error);
//				throw new Exception(error);    	
	    }
		}
		
		String webappsPath = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("path") + "/webapps";		
		Files files = new Files();
		String[] directories = files.dirList(webappsPath);
		for (int i = 0; i < directories.length; i++) {
			if (files.isDirectory(directories[i])) {
			  logger.info("Deleting dir: " + directories[i]);
		    files.removeDir(directories[i]);
			}
		}				
		
		return true;
	}

	
	private boolean IsDebug(String user) {
		if (! Configuration.isWindows()) {
      Shell shell = new Shell();    
      return ! (shell.executeCommandWithResponse("ps -FC java |grep "+user+" |grep dt_socket", new File("."), 1)).equals("");
		}
		return false;
	}
	
}
