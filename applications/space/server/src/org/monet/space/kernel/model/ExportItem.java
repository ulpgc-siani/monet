package org.monet.space.kernel.model;

import com.google.inject.Injector;
import org.jdom.Element;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.guice.InjectorFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;

public class ExportItem {
	private String id;
	private String type;
	private String owner;
	private String content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String serializeToXML() {
		StringWriter writer = new StringWriter();

		try {
			Injector injector = InjectorFactory.getInstance();
			XmlSerializer serializer = injector.getInstance(XmlSerializer.class);
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);

			this.serializeToXML(serializer);

			serializer.endDocument();
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
			throw new DataException(ErrorCode.SERIALIZE_TO_XML, null, e);
		}

		return writer.toString();
	}

	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "item");

		serializer.attribute("", "id", this.getId());
		serializer.attribute("", "type", this.getType());
		serializer.attribute("", "owner", this.getOwner());

		serializer.text(this.getContent());

		serializer.endTag("", "item");
	}

	public void deserializeFromXML(Element element) {

		if (element.getAttribute("id") != null)
			this.setId(element.getAttributeValue("id"));
		if (element.getAttribute("type") != null)
			this.setType(element.getAttributeValue("type"));
		if (element.getAttribute("owner") != null)
			this.setOwner(element.getAttributeValue("owner"));

		this.setContent(element.getText());
	}

}
