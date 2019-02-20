package org.monet.api.space.backservice.impl.model;

import org.jdom.Element;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Dimension extends BaseObject {

	public Dimension(String code) {
		super();
		this.setCode(code);

	}

	@Override
	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "dimension");

		serializer.attribute("", "code", this.code);
		serializer.attribute("", "name", this.name);

		serializer.endTag("", "dimension");
	}

	@Override
	public void deserializeFromXML(Element element) throws ParseException {
		if (element.getAttribute("code") != null) this.code = element.getAttributeValue("code");
		if (element.getAttribute("name") != null) this.name = element.getAttributeValue("name");
	}

}
