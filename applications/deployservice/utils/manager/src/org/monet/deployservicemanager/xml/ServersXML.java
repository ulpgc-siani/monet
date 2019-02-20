package org.monet.deployservicemanager.xml;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class ServersXML {

	@SuppressWarnings("rawtypes")
	public Item unserializelite(String xml) {
		xml = xml.replaceAll("<br/>", "");		
		
		Item servers = new Item();
		try {
			SAXBuilder builder = new SAXBuilder(false);
			StringReader reader = new StringReader(xml);
			Document doc = builder.build(reader);

			Element root = doc.getRootElement();
			servers.setProperty("deploy-server-version", root.getAttributeValue("deploy-server-version"));
			servers.setProperty("monet-version-config", root.getAttributeValue("monet-version-config"));

			List lservers = root.getChildren("server");
			Iterator iservers = lservers.iterator();
			while (iservers.hasNext()) {
				Element eserver = (Element) iservers.next();

				Item server = new Item();

				server.setProperty("id", eserver.getAttributeValue("id"));
				server.setProperty("name", eserver.getAttributeValue("name"));

				List lcontainers = eserver.getChildren("container");
				Iterator icontainers = lcontainers.iterator();
				while (icontainers.hasNext()) {
					Element econtainer = (Element) icontainers.next();

					Item container = new Item();

					container.setProperty("id", econtainer.getAttributeValue("id"));
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
					container.setProperty("docserver-server", econtainer.getAttributeValue("docserver-server"));
					container.setProperty("docserver-container", econtainer.getAttributeValue("docserver-container"));
					container.setProperty("frontserver-url", econtainer.getAttributeValue("frontserver-url"));
					container.setProperty("script-start", econtainer.getAttributeValue("script-start"));
					container.setProperty("script-stop", econtainer.getAttributeValue("script-stop"));
					
					List lspaces = econtainer.getChildren("space");
					Iterator ispaces = lspaces.iterator();
					while (ispaces.hasNext()) {
						Element espace = (Element) ispaces.next();
						Item space = new Item();
						
						space.setProperty("id", espace.getAttributeValue("id"));

						container.addItem(space);
					}
					
					server.addItem(container);					
				}

				servers.addItem(server);
			}
		} catch (Exception e) {
			String error = "";
			if (e != null)
				error = e.getMessage();
			System.out.println("Fail to unserialize xml: " + xml + "\nDetails: " + error);
		}

		return servers;
	}

}
