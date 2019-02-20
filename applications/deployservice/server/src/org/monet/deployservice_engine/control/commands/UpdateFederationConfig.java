package org.monet.deployservice_engine.control.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.Files;
import org.monet.deployservice.utils.Shell;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.utils.Federations;
import org.monet.deployservice_engine.utils.Spaces;
import org.monet.deployservice_engine.xml.ServersXML;

public class UpdateFederationConfig implements ICommand {

	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;

	private String server;
	private String container;
	
	private String federationName;
	private String federationDomain;
	private String federationPort;
	
	private String homeDir;
	private String homeFederationDir;
	
	public UpdateFederationConfig() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.UpdateFederationConfig);
		caption = new Item();
		caption.setProperty("id", "caption");
		result.addItem(caption);
		status = new Item();
		status.setProperty("id", "status");
		status.setContent("ok");
		result.addItem(status);
	}

	private Boolean initialize(Item command) {
		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());

		try {
			server = command.getItem("server").getContent();
			if (server.equals(""))
				throw new Exception("Server name is empty.");
		} catch (Exception e) {
			String error = "Server not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidServer);
			caption.setContent("Error: " + error);
			return false;
		}

		try {
			container = command.getItem("container").getContent();
			if (container.equals(""))
				throw new Exception("Container name is empty.");
		} catch (Exception e) {
			String error = "Container not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidContainer);
			caption.setContent("Error: " + error);
			return false;
		}
		
		try {
			federationName = command.getItem("federation-name").getContent();
			if (federationName.equals(""))
				throw new Exception("Federation name is empty.");
		} catch (Exception e) {
			String error = "Federation name not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidFederation);
			caption.setContent("Error: " + error);
			return false;
		}

		try {
			federationDomain = command.getItem("federation-domain").getContent();
			if (federationDomain.equals(""))
				throw new Exception("Federation domain is empty.");
		} catch (Exception e) {
			String error = "Federation domain not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidFederation);
			caption.setContent("Error: " + error);
			return false;
		}

		try {
			federationPort = command.getItem("federation-port").getContent();
			if (federationPort.equals(""))
				throw new Exception("Federation port is empty.");
		} catch (Exception e) {
			String error = "Federation port not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidFederation);
			caption.setContent("Error: " + error);
			return false;
		}

		try {
			homeDir = servers.getItem(server).getItem(container).getProperty("user-path") + "/" + servers.getItem(server).getItem(container).getProperty("user");
			homeFederationDir = homeDir + "/." + federationName; 
		} catch (Exception e) {
			String error = "Federation directory not found. Details: "+homeFederationDir;
			logger.error(error);
			status.setContent(ResultIDs.InvalidFederation);
			caption.setContent("Error: " + error);
			return false;
		}
		
		return true;
	}
	
	private void updatekernelconfig(String space) {
		Files files = new Files();
		Pattern pattern = null;
		Matcher matcher = null;

		String fileName = homeDir + "/." + space + "/" + Configuration.getValue(Configuration.VAR_FileConfigMonetKernel).replaceAll(".dist", "");
		
		String kernelConfig = files.loadTextFile(fileName);
				
		pattern = Pattern.compile("<entry key=\"MONET_SERVICES_BASE_URL\">(.*)</entry>");
		matcher = pattern.matcher(kernelConfig);
		String Url = "";
		if (matcher.find()) {
			Url = matcher.group(1);
		}
						
		files.replaceTextInFile(fileName, "<entry key=\"MONET_SERVICES_BASE_URL\">"+Url+"</entry>", "<entry key=\"MONET_SERVICES_BASE_URL\">http://"+federationDomain+":"+federationPort+"/"+space+"</entry>");		
	}
	
	private void updateurldatabase(String space) {
		String fileTemp = Configuration.getTempDir() +"/monet_services.sql";
		
		logger.info("Update url database, file: " + fileTemp);
		InputStream inputStream = null;	 
		OutputStream out = null;
    try {
    	inputStream = getClass().getResourceAsStream("/resources/database/monet_services.sql");    	
	    out = new FileOutputStream(new File(fileTemp));
    } catch (FileNotFoundException e) {
	    e.printStackTrace();
    }
	 
		int read = 0;
		byte[] bytes = new byte[1024];
	 
		try {
	    while ((read = inputStream.read(bytes)) != -1) {
	    	out.write(bytes, 0, read);
	    }
			inputStream.close();
			out.flush();
			out.close();
    } catch (IOException e) {
	    e.printStackTrace();
    }

		Files files = new Files();
		files.replaceTextInFile(fileTemp, "domain:port", federationDomain + ":"+ federationPort);
		
		logger.info("Execute mysql file: " + fileTemp);

		String command = "mysql --default-character-set=utf8 --force --database=" + space + " < " + fileTemp + " 2>&1";
		logger.info(command);

		Shell shell = new Shell();
		shell.executeCommand(command, new File(new File(fileTemp).getParent()));
		
		files.remove(fileTemp);
	}

	private void update_redirect_http_port() {
		logger.info("Execute redirect_http_port.sh");

		String command = "/opt/redirect_http_port.sh";
		logger.info(command);

		Shell shell = new Shell();
		shell.executeCommand(command, new File(Configuration.getPath()));		
	}
	
	private boolean update() {
		Files files = new Files();
		Pattern pattern = null;
		Matcher matcher = null;
		
		String federationConfig = files.loadTextFile(homeFederationDir + "/federation.config");
				
		pattern = Pattern.compile("<entry key=\"Domain\">(.*)</entry>");
		matcher = pattern.matcher(federationConfig);
		String Domain = "";
		if (matcher.find()) {
			Domain = matcher.group(1);
		}
		
		pattern = Pattern.compile("<entry key=\"Port\">(.*)</entry>");
		matcher = pattern.matcher(federationConfig);
		String Port = "";
		if (matcher.find()) {
			Port = matcher.group(1);
		}
				
		files.replaceTextInFile(homeFederationDir + "/federation.config", "<entry key=\"Domain\">"+Domain+"</entry>", "<entry key=\"Domain\">"+federationDomain+"</entry>");
		files.replaceTextInFile(homeFederationDir + "/federation.config", "<entry key=\"Port\">"+Port+"</entry>", "<entry key=\"Port\">"+federationPort+"</entry>");

		files.saveTextFile(Configuration.CONST_FileEtcFederationPort, federationPort);
//		files.saveTextFile(Configuration.CONST_FileEtcFederationDomain, federationDomain);
		
    Item servers = null;
    ServersXML serversXML = new ServersXML();
    servers = serversXML.unserialize(Configuration.getServersConfig());
    
		Federations itemFederations = new Federations();
		Item itemServer = servers.getItem(server);	
		Item federation = itemFederations.getFederation(itemServer, container, federationName);
		federation.setProperty("domain", federationDomain);
		federation.setProperty("port", federationPort);
    
    String xmlServers = serversXML.serializeReal(servers);
    try {
	    Configuration.setServersConfig(xmlServers);
    } catch (IOException e) {
	    e.printStackTrace();
    }	
		
	  String command = "";
	  Shell shell = new Shell();
		
		logger.info("Set url spaces in federation.");
		
		Spaces itemSpaces = new Spaces();
		String spaceName = "";

		Item spaces = itemSpaces.getSpaces(container, itemServer);
		Iterator<Item> ispaces = spaces.getItems().iterator();
		while (ispaces.hasNext()) {
			Item space = (Item) ispaces.next();

			spaceName = space.getProperty("id");
			
			command = "mysql --skip-column-names --silent --database="+Configuration.getValue(Configuration.VAR_FederationDefault, Configuration.CONST_FederationDefault)+" --execute='UPDATE `fs$business_units` SET `uri`=\"http://"+federationDomain+":"+federationPort+"/"+spaceName+"\" WHERE `name`=\""+spaceName+"\"'";
			shell.executeCommand(command, new File(Configuration.getPath()));
			
			updateurldatabase(spaceName);
			updatekernelconfig(spaceName);
			update_redirect_http_port();
		}				
		return true;
	}

	public Item execute(Item command) {
		if (!initialize(command))
			return result;

		if (!update())
			return result;

		return result;
	}

}
