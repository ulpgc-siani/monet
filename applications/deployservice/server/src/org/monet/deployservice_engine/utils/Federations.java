package org.monet.deployservice_engine.utils;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.Db;
import org.monet.deployservice.utils.DbData;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.xml.ServersXML;
import org.monet.deployservice_engine.xml.UsersXML;

public class Federations {

	private Logger logger;

	public Federations() {
		logger = Logger.getLogger(this.getClass());
	}

	@SuppressWarnings("rawtypes")
  public Item getFederations(Item server, String containerId) {
		Item container = null;
		try {
			container = server.getItem(containerId);
		} catch (Exception e) {
			logger.error("Container Id: " + containerId + " not exists.");
			return null;
		}

		Item federations = new Item();
		
		List<Item> lfederations = container.getItems();
		Iterator ifederations = lfederations.iterator();
		while (ifederations.hasNext()) {
			Item federation = (Item) ifederations.next();
			
			if (federation.getProperty("type").equals("federation")) {
				federations.addItem(federation);
			}
		}
		
		return federations;
	}

//	public Item getFederation(String containerId, Item server, String federationName) {
	public Item getFederation(Item server, String containerId, String federationName) {
		Item federations = getFederations(server, containerId);
		return federations.getItem(federationName);
	}
	
	public Item getUsers(Item server, String containerId, String federationId) {
		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());
		
		String serverId = server.getProperty("id");
		
		Item federation = getFederation(server, containerId, federationId);

		DbData dbData = new DbData();
		dbData.url = servers.getItem(serverId).getItem(containerId).getProperty("db-url");
		dbData.user = servers.getItem(serverId).getItem(containerId).getProperty("db-user");
		dbData.password = servers.getItem(serverId).getItem(containerId).getProperty("db-password");

		String dbFederationPrefix = servers.getItem(serverId).getItem(containerId).getProperty("db-federation-prefix");
		if (dbFederationPrefix == null) dbFederationPrefix = "";
		
		String federationDbUrl = dbData.url;
		String federationDbName = dbFederationPrefix+federation.getProperty("id");
		String federationDbUser = dbData.user;
		String federationDbPassword = dbData.password;
		
		if ((federation.getProperty("db-url") != null) && (! federation.getProperty("db-url").equals(""))) {
			federationDbUrl = federation.getProperty("db-url");
			federationDbUser = federation.getProperty("db-user");
			federationDbPassword = federation.getProperty("db-password");
			federationDbName = "";
		}
		Db db = new Db(federationDbUrl, federationDbUser, federationDbPassword, federationDbName);
					
		String sentence = "SELECT username, is_mobile, last_use, space, node FROM fs$tokens";

		String xml = "";
    try {		
		  xml = db.executeSentenceXML(sentence);
		} catch (Exception e) {
		}
		
    UsersXML usersXML = new UsersXML();
    return usersXML.unserialize(xml);        
	}
}
