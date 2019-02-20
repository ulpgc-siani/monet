package org.monet.api.space.backservice.impl.model;

import org.jdom.Element;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class ExportItem extends BaseObject {
	private String type;
	private String owner;
	private String content;

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
