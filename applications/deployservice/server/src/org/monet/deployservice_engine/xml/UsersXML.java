package org.monet.deployservice_engine.xml;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.monet.deployservice.xml.Item;

@SuppressWarnings("rawtypes")
public class UsersXML {
	private Logger logger;

	public UsersXML() {
		logger = Logger.getLogger(this.getClass());
	}

	public Item unserialize(String xml) {
		Item users = new Item();

		if (!xml.equals("")) {
			try {
				SAXBuilder builder = new SAXBuilder(false);
				StringReader reader = new StringReader(xml);
				Document doc = builder.build(reader);

				Element root = doc.getRootElement();

				List lrow = root.getChildren("Row");
				Iterator irow = lrow.iterator();
				while (irow.hasNext()) {
					Element erow = (Element) irow.next();
					Item user = new Item();

					user.setProperty("username", erow.getChildText("username"));
					user.setProperty("is_mobile", erow.getChildText("is_mobile"));
					user.setProperty("last_use", erow.getChildText("last_use"));
					user.setProperty("space", erow.getChildText("space"));
					user.setProperty("node", erow.getChildText("node"));
					users.addItem(user);
				}
			} catch (Exception e) {
				String error = "";
				if (e != null)
					error = e.getMessage();
				logger.error("Fail to unserialize xml: " + xml + "\nDetails: " + error);
			}
		}

		return users;
	}

}
