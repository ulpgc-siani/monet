package org.monet.deployservice_engine.xml;

import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.openssl.PEMReader;
import org.monet.deployservice.utils.Db;
import org.monet.deployservice.utils.ExceptionPrinter;
import org.monet.deployservice.utils.Files;
import org.monet.deployservice.utils.Network;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.utils.Federations;
import org.monet.deployservice_engine.utils.Spaces;
import org.monet.deployservice.utils.Md5;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

@SuppressWarnings("rawtypes")
public class ServersXML {

	private Logger logger;
	private String serialServer;
	private String serialContainer;

	public ServersXML() {
		logger = Logger.getLogger(this.getClass());
	}

	public Item unserialize(String xml) {
		Item servers = new Item();
		try {
			SAXBuilder builder = new SAXBuilder(false);
			StringReader reader = new StringReader(xml);
			Document doc = builder.build(reader);

			Element root = doc.getRootElement();

			String idServer;
			String idContainer;

			List lservers = root.getChildren("server");
			Iterator iservers = lservers.iterator();
			while (iservers.hasNext()) {
				Element eserver = (Element) iservers.next();

				serialServer = eserver.getAttributeValue("serial");
				idServer = getIdServer();

				Item server = new Item();
				server.setProperty("id", idServer);
				server.setProperty("serial", eserver.getAttributeValue("serial"));
				server.setProperty("name", eserver.getAttributeValue("name"));

				List lcontainers = eserver.getChildren("container");
				Iterator icontainers = lcontainers.iterator();
				while (icontainers.hasNext()) {
					Element econtainer = (Element) icontainers.next();

					Item container = new Item();

					serialServer = eserver.getAttributeValue("serial");
					idContainer = getIdContainer();

					container.setProperty("id", idContainer);
					container.setProperty("serial", econtainer.getAttributeValue("serial"));
					container.setProperty("name", econtainer.getAttributeValue("name"));
					container.setProperty("mayor-version", econtainer.getAttributeValue("mayor-version"));
					container.setProperty("minor-version", econtainer.getAttributeValue("minor-version"));
					container.setProperty("user", econtainer.getAttributeValue("user"));
					container.setProperty("group", econtainer.getAttributeValue("group"));
					container.setProperty("path", econtainer.getAttributeValue("path"));
					container.setProperty("user-path", econtainer.getAttributeValue("user-path"));
					container.setProperty("db-url", econtainer.getAttributeValue("db-url"));
					container.setProperty("db-user", econtainer.getAttributeValue("db-user"));
					container.setProperty("db-password", econtainer.getAttributeValue("db-password"));
					container.setProperty("db-prefix", econtainer.getAttributeValue("db-prefix"));
					container.setProperty("db-default-name", econtainer.getAttributeValue("db-default-name"));
					container.setProperty("db-default-user", econtainer.getAttributeValue("db-default-user"));
					container.setProperty("db-default-password", econtainer.getAttributeValue("db-default-password"));

					if (econtainer.getAttributeValue("db-create") != null && econtainer.getAttributeValue("db-create").equals("true"))
						container.setProperty("db-create", "true");
					else
						container.setProperty("db-create", "false");

					container.setProperty("docserver-server", econtainer.getAttributeValue("docserver-server"));
					container.setProperty("docserver-container", econtainer.getAttributeValue("docserver-container"));
					container.setProperty("frontserver-url", econtainer.getAttributeValue("frontserver-url"));
					container.setProperty("script-start", econtainer.getAttributeValue("script-start"));
					container.setProperty("script-start-debug", econtainer.getAttributeValue("script-start-debug"));
					container.setProperty("script-stop", econtainer.getAttributeValue("script-stop"));
					container.setProperty("reportservice-url", econtainer.getAttributeValue("reportservice-url"));
					container.setProperty("db-federation-prefix", econtainer.getAttributeValue("db-federation-prefix"));

					server.addItem(container);

					
					List lfederations = econtainer.getChildren("federation");
					Iterator ifederations = lfederations.iterator();
					while (ifederations.hasNext()) {
						Element efederation = (Element) ifederations.next();

						Item federation = new Item();
						federation.setProperty("type", "federation");
						federation.setProperty("id", efederation.getAttributeValue("id"));
						federation.setProperty("password-key", efederation.getAttributeValue("password-key"));
						federation.setProperty("password-ca", efederation.getAttributeValue("password-ca"));
						federation.setProperty("key", efederation.getAttributeValue("key"));
						federation.setProperty("certificate", efederation.getAttributeValue("certificate"));
						federation.setProperty("db-url", efederation.getAttributeValue("db-url"));
						federation.setProperty("db-user", efederation.getAttributeValue("db-user"));
						federation.setProperty("db-password", efederation.getAttributeValue("db-password"));
						
						String value = "";
						if (efederation.getAttributeValue("domain") != null) value = efederation.getAttributeValue("domain");
						federation.setProperty("domain", value);
						
						value = "";
						if (efederation.getAttributeValue("port") != null) value = efederation.getAttributeValue("port");
						federation.setProperty("port", value);
						
						container.addItem(federation);
					}
					
					
					Db db = new Db();
					String dbType = db.getDbTypeFromUrl(econtainer.getAttributeValue("db-url"));
					Spaces itemSpaces = new Spaces();
					try {
						Item spaces = itemSpaces.getSpaces(idContainer, server);
						Iterator ispaces = spaces.getItems().iterator();
						while (ispaces.hasNext()) {
							Item space = (Item) ispaces.next();

							space.setProperty("type", "space");
							
							if (space.getProperty("db-type").equals(dbType))
								container.addItem(space);
						}
					} catch (Exception e) {
						String error = "";
						String stackTrace = "";
						if (e != null) {
							error = e.getMessage();

							StringWriter sw = new StringWriter();
							PrintWriter pw = new PrintWriter(sw);
							e.printStackTrace(pw);
							stackTrace = sw.toString(); // stack trace as a string
						}

						logger.error("Fail to get spaces. Details: " + error + "\nStack Trace: " + stackTrace);
					}
					
					
				}
				servers.addItem(server);
			}
		} catch (Exception e) {
			String error = "";
			if (e != null)
				error = e.getMessage();
			logger.error("Fail to unserialize xml: " + xml + "\nDetails: " + error);
		}

		return servers;
	}

	public String serializeReal(Item item) {
		String xml = "";
		try {
			Element root = new Element("servers");

			List<Item> servers = item.getItems();
			Iterator iservers = servers.iterator();
			while (iservers.hasNext()) {
				Item server = (Item) iservers.next();
				Element eserver = new Element("server");

				eserver.setAttribute("serial", server.getProperty("serial"));
				eserver.setAttribute("name", server.getProperty("name"));

				List<Item> containers = server.getItems();
				Iterator icontainers = containers.iterator();
				while (icontainers.hasNext()) {
					Item container = (Item) icontainers.next();
					Element econtainer = new Element("container");

					econtainer.setAttribute("name", container.getProperty("name"));
					econtainer.setAttribute("mayor-version", container.getProperty("mayor-version"));
					econtainer.setAttribute("minor-version", container.getProperty("minor-version"));
					econtainer.setAttribute("user", container.getProperty("user"));
					econtainer.setAttribute("group", container.getProperty("group"));
					econtainer.setAttribute("path", container.getProperty("path"));
					econtainer.setAttribute("user-path", container.getProperty("user-path"));
					econtainer.setAttribute("db-url", container.getProperty("db-url"));
					econtainer.setAttribute("db-user", container.getProperty("db-user"));
					econtainer.setAttribute("db-password", container.getProperty("db-password"));
					econtainer.setAttribute("db-prefix", container.getProperty("db-prefix"));
					econtainer.setAttribute("db-default-name", container.getProperty("db-default-name"));
					econtainer.setAttribute("db-default-user", container.getProperty("db-default-user"));
					econtainer.setAttribute("db-default-password", container.getProperty("db-default-password"));

					econtainer.setAttribute("db-create", container.getProperty("db-create"));
					econtainer.setAttribute("docserver-server", container.getProperty("docserver-server"));
					econtainer.setAttribute("docserver-container", container.getProperty("docserver-container"));
					econtainer.setAttribute("frontserver-url", container.getProperty("frontserver-url"));
					econtainer.setAttribute("script-start", container.getProperty("script-start"));
					econtainer.setAttribute("script-start-debug", container.getProperty("script-start-debug"));
					econtainer.setAttribute("script-stop", container.getProperty("script-stop"));
					econtainer.setAttribute("reportservice-url", container.getProperty("reportservice-url"));
					econtainer.setAttribute("db-federation-prefix", container.getProperty("db-federation-prefix"));
					
					
					List<Item> nodes = container.getItems();
					Iterator inodes = nodes.iterator();
					while (inodes.hasNext()) {
						Item node = (Item) inodes.next();
						
						if (node.getProperty("type").equals("federation")) {
						  Element efederation = new Element("federation");

						  efederation.setAttribute("id", node.getProperty("id"));

						  efederation.setAttribute("password-key", node.getProperty("password-key"));
						  efederation.setAttribute("password-ca", node.getProperty("password-ca"));
						  efederation.setAttribute("key", node.getProperty("key"));
						  efederation.setAttribute("certificate", node.getProperty("certificate"));
						  efederation.setAttribute("db-url", node.getProperty("db-url"));
						  efederation.setAttribute("db-user", node.getProperty("db-user"));
						  efederation.setAttribute("db-password", node.getProperty("db-password"));
						  efederation.setAttribute("domain", node.getProperty("domain"));
						  efederation.setAttribute("port", node.getProperty("port"));
						  
						  econtainer.addContent(efederation);
						}
												
					}
					eserver.addContent(econtainer);
				}
				root.addContent(eserver);
			}

			Document doc = new Document(root);
			XMLOutputter outputter = new XMLOutputter();
			
			Format format = Format.getRawFormat();
			format.setIndent("  ");
			format.setLineSeparator("\n");			
			outputter.setFormat(format);
			xml = outputter.outputString(doc);
		} catch (Exception e) {
			logger.error("Fail to serialize.");
		}

		return xml;
	}
	
	public String serializeVirtual(Item item) {
		String xml = "";
		try {
			Element root = new Element("servers");

			Network network = new Network();
			root.setAttribute("deploy-server-ip", network.getCurrentEnvironmentNetworkIp(Configuration.getValue(Configuration.VAR_SendUDPInterface)));
			root.setAttribute("deploy-server-version", Configuration.CONST_Version);
			root.setAttribute("monet-version", Configuration.MonetVersionMayor() + "." + Configuration.MonetVersionMinor());

		  DateFormat dateFormat = new SimpleDateFormat("MMddHHmmyyyy");
		  Date date = new Date();
		  String currentDate = dateFormat.format(date);			
			root.setAttribute("time", currentDate);

			if (Configuration.MonetVersionMayor() > 2) {
			  Files files = new Files();
			  String federationDomain = "";
			  if (files.fileExists(Configuration.CONST_FileEtcFederationDomainOld))
			    federationDomain = files.loadTextFile(Configuration.CONST_FileEtcFederationDomainOld);			
			  root.setAttribute("federation-domain", federationDomain.trim());
			}

			List<Item> servers = item.getItems();
			Iterator iservers = servers.iterator();
			while (iservers.hasNext()) {
				Item server = (Item) iservers.next();
				Element eserver = new Element("server");

				eserver.setAttribute("id", server.getProperty("id"));
				eserver.setAttribute("name", server.getProperty("name"));

				List<Item> containers = server.getItems();
				Iterator icontainers = containers.iterator();
				while (icontainers.hasNext()) {
					Item container = (Item) icontainers.next();
					Element econtainer = new Element("container");
					econtainer.setAttribute("id", container.getProperty("id"));
					econtainer.setAttribute("name", container.getProperty("name"));
					econtainer.setAttribute("mayor-version", container.getProperty("mayor-version"));
					econtainer.setAttribute("minor-version", container.getProperty("minor-version"));

					List<Item> nodes = container.getItems();
					Iterator inodes = nodes.iterator();
          String certificateExpiration;
					while (inodes.hasNext()) {
						Item node = (Item) inodes.next();
						
						if (node.getProperty("type").equals("space")) {
						  Element espace = new Element("space");
						  espace.setAttribute("id", node.getProperty("id"));
						  espace.setAttribute("url", node.getProperty("url"));
						  espace.setAttribute("model", node.getProperty("model"));
						  espace.setAttribute("federation-url", node.getProperty("federation-url"));

              String certificateFile = item.getItem(server.getProperty("id")).getItem(container.getProperty("id")).getProperty("user-path") + "/" + item.getItem(server.getProperty("id")).getItem(container.getProperty("id")).getProperty("user") + "/." + node.getProperty("id") +"/certificates/businessunit-" + node.getProperty("id") + ".crt";
              FileReader fr = new FileReader(certificateFile);
              Security.addProvider(new BouncyCastleProvider());
              PEMReader pemReader = new PEMReader(fr);
              X509CertificateObject cert = (X509CertificateObject) pemReader.readObject();

              dateFormat = new SimpleDateFormat("MMddHHmmyyyy");
              certificateExpiration = dateFormat.format(cert.getNotAfter().getTime());
              espace.setAttribute("certificate-expiration", certificateExpiration);

						  econtainer.addContent(espace);
						}

						if (node.getProperty("type").equals("federation")) {
						  Element efederation = new Element("federation");
						  efederation.setAttribute("id", node.getProperty("id"));
						  efederation.setAttribute("domain", node.getProperty("domain"));					  
						  efederation.setAttribute("port", node.getProperty("port"));
						  
							Federations federations = new Federations();					  						  
							Item users = federations.getUsers(server, container.getProperty("id"), node.getProperty("id"));
							Iterator iusers = users.getItems().iterator();
							while (iusers.hasNext()) {
								Item user = (Item) iusers.next();
								
							  Element euser = new Element("user");
							  euser.setAttribute("name", user.getProperty("username"));
							  euser.setAttribute("is_mobile", user.getProperty("is_mobile"));
							  euser.setAttribute("last_use", user.getProperty("last_use"));
							  euser.setAttribute("space", user.getProperty("space"));
							  euser.setAttribute("node", user.getProperty("node"));
							  efederation.addContent(euser);								
							}
							
						  econtainer.addContent(efederation);
						}
												
					}
					eserver.addContent(econtainer);
				}
				root.addContent(eserver);
			}

			Document doc = new Document(root);
			XMLOutputter outputter = new XMLOutputter();
			
			Format format = Format.getRawFormat();
			format.setIndent("  ");
			format.setLineSeparator("\n");			
			outputter.setFormat(format);
			xml = outputter.outputString(doc);
		} catch (Exception e) {
			ExceptionPrinter exeptionPrinter = new ExceptionPrinter();					
			logger.error("Fail to serialize. Details: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e));
		}

		return xml;
	}

	public String serializeLite(Item item) {
		String xml = "";
		Element root = new Element("servers");

		List<Item> servers = item.getItems();
		Iterator iservers = servers.iterator();
		while (iservers.hasNext()) {
			Item server = (Item) iservers.next();
			Element eserver = new Element("server");

			eserver.setAttribute("serial", server.getProperty("serial"));
			eserver.setAttribute("name", server.getProperty("name"));

			List<Item> containers = server.getItems();
			Iterator icontainers = containers.iterator();
			while (icontainers.hasNext()) {
				Item container = (Item) icontainers.next();
				Element econtainer = new Element("container");
				econtainer.setAttribute("serial", container.getProperty("serial"));
				econtainer.setAttribute("name", container.getProperty("name"));
				econtainer.setAttribute("mayor-version", container.getProperty("mayor-version"));
				econtainer.setAttribute("minor-version", container.getProperty("minor-version"));
				econtainer.setAttribute("user", container.getProperty("user"));
				econtainer.setAttribute("group", container.getProperty("group"));
				econtainer.setAttribute("path", container.getProperty("path"));
				econtainer.setAttribute("user-path", container.getProperty("user-path"));
				econtainer.setAttribute("db-prefix", container.getProperty("db-prefix"));
				econtainer.setAttribute("db-url", container.getProperty("db-url"));
				econtainer.setAttribute("db-user", container.getProperty("db-user"));
				econtainer.setAttribute("db-password", container.getProperty("db-password"));
				econtainer.setAttribute("db-create", container.getProperty("db-create"));
				econtainer.setAttribute("docserver-server", container.getProperty("docserver-server"));
				econtainer.setAttribute("docserver-container", container.getProperty("docserver-container"));
				econtainer.setAttribute("frontserver-url", container.getProperty("frontserver-url"));
				econtainer.setAttribute("script-start", container.getProperty("script-start"));
				econtainer.setAttribute("script-stop", container.getProperty("script-stop"));
				econtainer.setAttribute("reportservice-url", container.getProperty("reportservice-url"));
				econtainer.setAttribute("db-federation-prefix", container.getProperty("db-federation-prefix"));

				eserver.addContent(econtainer);
			}
			root.addContent(eserver);
		}

		Document doc = new Document(root);

		Format format = Format.getRawFormat();
		format.setIndent(" ");
		format.setLineSeparator("\n");
		XMLOutputter outputter = new XMLOutputter(format);

		xml = outputter.outputString(doc);

		return xml;
	}

	public String getIdServer() {
		Network network = new Network();

		Md5 md5;
		try {
	    md5 = Md5.getInstance();
    } catch (NoSuchAlgorithmException e) {
    	return "";
    }
		String idServer = network.getCurrentEnvironmentNetworkIp(Configuration.getValue(Configuration.VAR_SendUDPInterface)) + serialServer;
		idServer = md5.hashData(idServer.getBytes());

		return idServer;
	}
	
	public String getIdContainer() {
		Network network = new Network();

		Md5 md5;
		try {
	    md5 = Md5.getInstance();
    } catch (NoSuchAlgorithmException e) {
    	return "";
    }
		String idContainer = network.getCurrentEnvironmentNetworkIp(Configuration.getValue(Configuration.VAR_SendUDPInterface)) + serialContainer;
		idContainer = md5.hashData(idContainer.getBytes());
		return idContainer;
	}
	
}
