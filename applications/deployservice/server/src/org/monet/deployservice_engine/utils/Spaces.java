package org.monet.deployservice_engine.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.Db;
import org.monet.deployservice.utils.Files;
//import org.monet.deployservice.utils.Files;
import org.monet.deployservice.utils.Zip;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;

public class Spaces {

	private Logger logger;

	public Spaces() {
		logger = Logger.getLogger(this.getClass());
	}

	public Item getSpaces(String containerId, Item server) {
		Item container = null;
		try {
			container = server.getItem(containerId);
		} catch (Exception e) {
			logger.error("Container Id: " + containerId + " not exists.");
			return null;
		}

		String containerPath = container.getProperty("path") + "/webapps";
		String containerPathUser = container.getProperty("user-path") + "/" + container.getProperty("user");

		File dir = new File(containerPath);
		String[] webapps = dir.list();

		Files files = new Files();
		Zip zip = new Zip();
		Pattern pattern = null;
		Matcher matcher = null;

		Item spaces = new Item();

		if (webapps == null)
			logger.warn("Container Path not exists: " + containerPath);
		else {
			String fileZip = "";

			
			for (int x = 0; x < webapps.length; x++) {
				fileZip = containerPath + "/" + webapps[x];
 				if ((! files.isDirectory(fileZip)) && zip.fileExists(fileZip, "WEB-INF/web.xml")) {
					String WebXML = zip.unCompressOnlyFile(fileZip, "WEB-INF/web.xml");

					pattern = Pattern.compile("<display-name>(.*)</display-name>");
					matcher = pattern.matcher(WebXML);
					String spaceId = "";
					if (matcher.find()) {
						spaceId = matcher.group(1);
					}

					String fileDeployServerConfigName = containerPathUser + "/." + spaceId + "/configuration/" + Configuration.CONST_CONFIG_FILE;															
					if ((Configuration.MonetVersionMayor() == 2) || ((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() == 0))) {
						fileDeployServerConfigName = containerPathUser + "/." + spaceId + "/" + Configuration.CONST_CONFIG_FILE;
					}										
					
					String fileMonetConfigName = containerPathUser + "/." + spaceId + "/"
					    + Configuration.getValue(Configuration.VAR_FileConfigMonetKernel).replaceAll(".dist", "");

					Properties properties = new Properties();
					FileInputStream is;
					try {
						Item space = new Item();

						if (files.fileExists(fileZip.replace(".war", "")))
							space.setProperty("active", "true");
						else
							space.setProperty("active", "false");
						
						is = new FileInputStream(fileDeployServerConfigName);
						try {
							properties.loadFromXML(is);

							String url = properties.getProperty("URL");
							if (url == null)
								url = "";
							String dsServer = properties.getProperty("DOCSERVICE_SERVER");
							if (dsServer == null)
								dsServer = "";
							String dsContainer = properties.getProperty("DOCSERVICE_CONTAINER");
							if (dsContainer == null)
								dsContainer = "";
							
							String installSaiku = "false";
							try {
							  installSaiku = properties.getProperty("SAIKU_INSTALLED").toLowerCase();
							if (installSaiku == null)
								installSaiku = "true";
							} catch (Exception e) {
								installSaiku = "true";
							}
							space.setProperty("install-saiku", installSaiku);
								
							String dbMonetNameUrl = properties.getProperty("MONET_DB_URL").toLowerCase();
							if (dbMonetNameUrl == null)
							  dbMonetNameUrl = "";
							String dbDocServiceNameUrl = properties.getProperty("DOCSERVICE_DB_URL").toLowerCase();
							if (dbDocServiceNameUrl == null)
							  dbDocServiceNameUrl = "";							

							String spaceLabelES = properties.getProperty("SPACE_LABEL_ES");
							if (spaceLabelES == null)
								spaceLabelES = "";
							String spaceURI = properties.getProperty("SPACE_URI");
							if (spaceURI == null)
								spaceURI = "";
							String spaceSecret = properties.getProperty("SPACE_SECRET");
							if (spaceSecret == null)
								spaceSecret = "";

							String federationName = properties.getProperty("FEDERATION_NAME");
							if (federationName == null)
								federationName = Configuration.getValue(Configuration.VAR_FederationDefault, Configuration.CONST_FederationDefault);

							String federationPort = files.loadTextFile(Configuration.CONST_FileEtcFederationPort).trim();
							String federationDomain = files.loadTextFile(Configuration.CONST_FileEtcFederationDomainOld).trim();																				
							Federations itemFederations = new Federations();
							Item federation = itemFederations.getFederation(server, containerId, federationName);
							if (federation == null)
								logger.warn("I can't load federation '"+federationName+"' in container '"+containerId+"'");
							
							if ((federation != null) && (federation.getProperty("id") != null)) {
  							if (! federation.getProperty("domain").equals("")) federationDomain = federation.getProperty("domain"); 
	  						if (! federation.getProperty("port").equals("")) federationPort = federation.getProperty("port");
							}
							
							String federationURL = "http://"+federationDomain+":"+federationPort+"/"+federationName;							
							if (federationPort.equals("80")) {
								federationURL = "http://" + federationDomain +"/"+ federationName;
							} else {
								if (federationPort.equals("443")) {
									federationURL = "https://" + federationDomain +"/"+ federationName;									
								}
							}
								
							space.setProperty("id", spaceId);
							space.setProperty("url", url);
							space.setProperty("docserver-server", dsServer);
							space.setProperty("docserver-container", dsContainer);
							space.setProperty("space-label-es", spaceLabelES);
							space.setProperty("space-uri", spaceURI);
							space.setProperty("space-secret", spaceSecret);
							space.setProperty("federation", federationName);
							space.setProperty("federation-url", federationURL);

							// TODO: Los datos de la base de datos deben ser obtenidos del contexto.							
							Db db = new Db();
							String dbName = "";
							dbName = db.getDbNameFromUrl(dbMonetNameUrl);
							space.setProperty("dbname-monet", dbName);
							
							dbName = db.getDbNameFromUrl(dbDocServiceNameUrl);
							space.setProperty("dbname-docserver", dbName);							
							
							is.close();
						} catch (IOException e) {
							logger.error("The config file '" + fileDeployServerConfigName + "' exists but i can not read.");
						}

						is = new FileInputStream(fileMonetConfigName);
						try {
							properties.loadFromXML(is);

							String baseUrl = properties.getProperty("MONET_SERVICES_BASE_URL");

							String dbType = "";
							if ((Configuration.MonetVersionMayor() == 2) || ((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() < 2))) {
								dbType = properties.getProperty("MONET_BUSINESS_UNIT_DATABASE_TYPE").toLowerCase();
							} else {
								dbType = properties.getProperty("Jdbc.Type").toLowerCase();
							}
														
							space.setProperty("base-url", baseUrl);
							space.setProperty("db-type", dbType);

							pattern = Pattern.compile(".*://.*/(.*)");
							matcher = pattern.matcher(baseUrl);
							String pathUrl = "";
							if (matcher.find()) {
								pathUrl = matcher.group(1);
							}
							space.setProperty("base-url-path", pathUrl);

							is.close();
						} catch (IOException e) {
							logger.error("The config file '" + fileMonetConfigName + "' exists but i can not read.");
						}

						String fileManifest = containerPathUser + "/." + spaceId + "/businessmodel/MANIFEST";
						if (files.fileExists(fileManifest)) {
						  String textManifest = files.loadTextFile(fileManifest);
						  
							pattern = Pattern.compile("Location: (.*)\\..*\\..*");
							matcher = pattern.matcher(textManifest);
							String model = "";
							if (matcher.find()) {
								model = matcher.group(1);
							}
							space.setProperty("model", model);						  
						}
						
						spaces.addItem(space);

					} catch (FileNotFoundException e) {
						//logger.info("The config file '"+fileDeployServerConfigName+"' not exists as not is a space.");
					}
				}
			}
		}

		return spaces;
	}

	public Item getSpace(String containerId, Item server, String spaceName) {
		Item spaces = getSpaces(containerId, server);
		return spaces.getItem(spaceName);
	}
}
