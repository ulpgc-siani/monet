package org.monet.deployservicemanager.xml;

import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class ResultXML {

	public Item unserializelite(String xml) {
		xml = xml.replaceAll("<br/>", "");
		
		Item result = new Item();
		try {
			SAXBuilder builder = new SAXBuilder(false);
			StringReader reader = new StringReader(xml);
			Document doc = builder.build(reader);

			Element eresult = doc.getRootElement();
			
//			List lresult = root.getChildren("result");
//			Iterator iresult = lresult.iterator();
//			while (iresult.hasNext()) {

//			Element eresult = root.getChild("result");
			
			result.setProperty("id", eresult.getAttributeValue("id"));

			Item status = new Item();
			status.setProperty("id", "status");
			status.setContent(eresult.getChildText("status"));
			result.addItem(status);
			
			Item caption = new Item();
			caption.setProperty("id", "caption");
			caption.setContent(eresult.getChildText("caption"));
			result.addItem(caption);
			
			Item content = new Item();
			content.setProperty("id", "content");
			content.setContent(eresult.getChildText("content"));
			result.addItem(content);
			
/*			
			List lresult = root.getChildren("result");
			Iterator iresult = lresult.iterator();
			while (iresult.hasNext()) {
				Element eserver = (Element) iresult.next();

				Item result = new Item();

				result.setProperty("id", eserver.getAttributeValue("id"));

				List lcontainers = eserver.getChildren("status");
				Iterator icontainers = lcontainers.iterator();
				while (icontainers.hasNext()) {
					Element econtainer = (Element) icontainers.next();

					Item container = new Item();

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
					container.setProperty("docserver-server", econtainer.getAttributeValue("docserver-server"));
					container.setProperty("docserver-container", econtainer.getAttributeValue("docserver-container"));
					container.setProperty("frontserver-url", econtainer.getAttributeValue("frontserver-url"));
					container.setProperty("script-start", econtainer.getAttributeValue("script-start"));
					container.setProperty("script-stop", econtainer.getAttributeValue("script-stop"));

					server.addItem(container);					
				}

				servers.addItem(server);
			}
*/			
		} catch (Exception e) {
			String error = "";
			if (e != null)
				error = e.getMessage();
			System.out.println("Fail to unserialize xml: " + xml + "\nDetails: " + error);
		}

		return result;
	}
	
}
