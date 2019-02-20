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
public class UpdatesXML {
	private Logger logger;
	
	public UpdatesXML() {
		logger = Logger.getLogger(this.getClass());
	}
	
  public Item unserialize(String xml) {
		Item updates = new Item();
		try {
			SAXBuilder builder = new SAXBuilder(false);
			StringReader reader = new StringReader(xml);
			Document doc = builder.build(reader);

			Element eupdates = doc.getRootElement();

			List lapplications = eupdates.getChildren("application");
			Iterator iapplications = lapplications.iterator();
			while (iapplications.hasNext()) {
				Element eapplication = (Element) iapplications.next();
				
				Item application = new Item();
				application.setProperty("id", eapplication.getAttributeValue("id"));
			  
				
				List lversions = eapplication.getChildren("version");
				Iterator iversions = lversions.iterator();
				while (iversions.hasNext()) {
					Element eversion = (Element) iversions.next();
					
					Item version = new Item();
					version.setProperty("id", eversion.getAttributeValue("id"));
					version.setProperty("mayor", eversion.getAttributeValue("mayor"));
					version.setProperty("minor", eversion.getAttributeValue("minor"));
										
					application.addItem(version);
				}
				
			  updates.addItem(application);								
			}
			
		} catch (Exception e) {
			logger.error("Fail to unserialize xml: \n" + xml + "\n\nDetails: " + e.getMessage());
		}
		return updates;	
	}

}
