package org.monet.api.space.backservice.impl.model;

import org.jdom.Element;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Location extends BaseObject {

	@Attribute(required = false)
	private java.util.Date timestamp;

	@Attribute(required = false)
	private String label;

	@Text
	private String wkt;

	public java.util.Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(java.util.Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getWkt() {
		return wkt;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return this.label;
	}

	@Override
	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
	}

	public void deserializeFromXML(Element location) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");

		if (location.getAttribute("label") != null) this.label = location.getAttributeValue("label");
		if (location.getAttribute("wkt") != null) this.wkt = location.getAttributeValue("wkt");
		if (location.getAttribute("createDate") != null) this.timestamp = dateFormat.parse(location.getAttributeValue("createDate"));
	}

}
