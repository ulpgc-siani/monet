package org.monet.deployservice_engine.xml;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.monet.deployservice.xml.Item;

@SuppressWarnings("rawtypes")
public class DocServersXML {

	private Logger logger;

	public DocServersXML() {
		logger = Logger.getLogger(this.getClass());
	}
		
  public Item unserialize(String xml) {
		Item servers = new Item();
		try {
			SAXBuilder builder = new SAXBuilder(false);
			StringReader reader = new StringReader(xml);
			Document doc = builder.build(reader);
			
			Element root = doc.getRootElement();

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
					container.setProperty("url", econtainer.getAttributeValue("url"));
					container.setProperty("db-prefix", econtainer.getAttributeValue("db-prefix"));
					container.setProperty("script-start", econtainer.getAttributeValue("script-start"));
					container.setProperty("script-start-debug", econtainer.getAttributeValue("script-start-debug"));
					container.setProperty("script-stop", econtainer.getAttributeValue("script-stop"));
					container.setProperty("db-default-name", econtainer.getAttributeValue("db-default-name"));
					container.setProperty("db-default-user", econtainer.getAttributeValue("db-default-user"));
					container.setProperty("db-default-password", econtainer.getAttributeValue("db-default-password"));
																									
					server.addItem(container);
				}

				servers.addItem(server);
			}
		} catch (Exception e) {
			logger.error("Fail to unserialize xml: " + xml + "\nDetails: " + e.getMessage());
		}

		return servers;
	}
	
	public String serialize(Item item) {
		String xml = "";
		try {
			Element root = new Element("servers");

			List<Item> servers = item.getItems();
			Iterator iservers = servers.iterator();
			while (iservers.hasNext()) {
				Item server = (Item) iservers.next();
				Element eserver = new Element("server");

				eserver.setAttribute("id", server.getProperty("id"));

				List<Item> containers = server.getItems();
				Iterator icontainers = containers.iterator();
				while (icontainers.hasNext()) {
					Item container = (Item) icontainers.next();
					Element econtainer = new Element("container");

					econtainer.setAttribute("id", container.getProperty("id"));					
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
					econtainer.setAttribute("url", container.getProperty("url"));					
					econtainer.setAttribute("script-start", container.getProperty("script-start"));
					econtainer.setAttribute("script-stop", container.getProperty("script-stop"));
					
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
		} catch (Exception e) {
			logger.error("Fail to serialize.");
		}

		return xml;
	}
	
}
