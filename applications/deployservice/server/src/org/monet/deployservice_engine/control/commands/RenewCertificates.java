package org.monet.deployservice_engine.control.commands;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.Shell;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.utils.Federations;
import org.monet.deployservice_engine.utils.Spaces;
import org.monet.deployservice_engine.xml.ServersXML;

public class RenewCertificates implements ICommand {

	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;

	private String server;
	private String container;

	private String days;
	private Item federation;
	
	
	public RenewCertificates() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.RenewCertificates);
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
			days = command.getItem("days").getContent();
			if (days.equals(""))
				throw new Exception("Days is empty.");
		} catch (Exception e) {
			String error = "Days not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidDays);
			caption.setContent("Error: " + error);
			return false;
		}

		String federationName = Configuration.getValue(Configuration.VAR_FederationDefault, Configuration.CONST_FederationDefault);
		try {
			federationName = command.getItem("federation").getContent();			
		} catch (Exception e) {
			String error = "I can not get federation name.";
			logger.warn(error);
		}		
		Item itemServer = servers.getItem(server);
		Federations itemFederations = new Federations();
		try {
			federation = itemFederations.getFederation(itemServer, container, federationName);
		} catch (Exception e) {
			String error = "I can not get federation '"+federationName+"'.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidFederation);
			caption.setContent("Error: " + error);
			return false;			
		}
		
		return true;
	}

	private boolean reset() {

		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());

		Item itemServer = servers.getItem(server);
		Spaces itemSpaces = new Spaces();

		Item spaces = itemSpaces.getSpaces(container, itemServer);
		Iterator<Item> ispaces = spaces.getItems().iterator();
		while (ispaces.hasNext()) {
			Item space = (Item) ispaces.next();

			String spaceName = space.getProperty("id");
			logger.info("Renew certificate in " + spaceName + " from server: " + servers.getItem(server).getProperty("name") + ", container: " + servers.getItem(server).getItem(container).getProperty("name"));

			String homeDir = servers.getItem(server).getItem(container).getProperty("user-path") + "/" + servers.getItem(server).getItem(container).getProperty("user") + "/." + spaceName;
			
			String FileNameSSL = "businessunit-" + spaceName;
//			if (Configuration.getValue(Configuration.VAR_MonetVersion).equals("3.1")) {
			if (((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() > 0)) || (Configuration.MonetVersionMayor() > 3)) {
			  FileNameSSL = homeDir + "/certificates/businessunit-" + spaceName;
			}
			
			String command = "";
			Shell shell = new Shell();
			
			logger.info("Signing certificate");
			command = "openssl x509 -req -passin pass:"+federation.getProperty("password-ca")+" -days "+days+" -CA " + federation.getProperty("certificate") + " -CAkey " + federation.getProperty("key") + " -set_serial 01 -in " + FileNameSSL + ".csr -out " + FileNameSSL + ".crt";
			shell.executeCommand(command, new File(new File(federation.getProperty("certificate")).getParent()));

			logger.info("Export certificate");
			command = "openssl pkcs12 -export -passin pass:"+federation.getProperty("password-key")+" -passout pass:"+federation.getProperty("password-key")+" -out " + FileNameSSL + ".p12 -inkey " + FileNameSSL + ".key -in " + FileNameSSL + ".crt -certfile " + federation.getProperty("certificate");
			shell.executeCommand(command, new File(new File(federation.getProperty("certificate")).getParent()));					
		}

		return true;
	}

	public Item execute(Item command) {
		if (!initialize(command))
			return result;
		if (!reset())
			return result;

		return result;
	}

}
