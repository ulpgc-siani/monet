package org.monet.deployservice_engine.xml;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.monet.deployservice.xml.Item;

public class ResultXML {
	private Logger logger;

	public ResultXML() {
		logger = Logger.getLogger(this.getClass());
	}

	public String serialize(Item item) {
		String xml = "";
		try {
			Element result = new Element("result");

			result.setAttribute("id", item.getProperty("id"));

			Item status = item.getItem("status");
			Element estatus = new Element("status");
			estatus.setText("");			
			if (status != null) estatus.setText(status.getContent());
			result.addContent(estatus);
			
			Item caption = item.getItem("caption");
			Element ecaption = new Element("caption");
			ecaption.setText("");
			if (caption != null) ecaption.setText(caption.getContent());
			result.addContent(ecaption);

			Item content = item.getItem("content");
			Element econtent = new Element("content");
			econtent.setText("");
			if (content != null) econtent.setText(content.getContent().replaceAll("\n", "<br/>"));
			result.addContent(econtent);

			Document doc = new Document(result);
			XMLOutputter outputter = new XMLOutputter();

			xml = outputter.outputString(doc);
			xml = xml.replaceAll("\n", "");
			xml = xml.replaceAll("\r", "");

		} catch (Exception e) {
			logger.error("Fail to serialize.");
		}

		return xml;
	}

}
