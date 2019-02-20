package org.monet.deployservice_engine.xml;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.monet.deployservice.xml.Item;

public class CommandXML {
	private Logger logger;
	
	public CommandXML() {
		logger = Logger.getLogger(this.getClass());
	}
		
  @SuppressWarnings("rawtypes")
  public Item unserialize(String xml) {
  	xml = xml.replaceAll("  ", "");
  	
		Item command = new Item();
		try {
			SAXBuilder builder = new SAXBuilder(false);			
			StringReader reader = new StringReader(xml);
			Document doc = builder.build(reader);

			Element ecommand = doc.getRootElement();
			command.setProperty("id", ecommand.getAttributeValue("id"));

			List lparameters = ecommand.getContent();
			Iterator iparameters = lparameters.iterator();
			while (iparameters.hasNext()) {
				Element eparameter = (Element) iparameters.next();
				
				Item parameter = new Item();
				parameter.setProperty("id", eparameter.getAttributeValue("id"));
			  parameter.setContent(eparameter.getText());
			  
			  command.addItem(parameter);								
			}
			
		} catch (Exception e) {
			logger.error("Fail to unserialize xml: "  + e.getMessage() + "\n" + xml);
		}
		return command;	
	}

}
