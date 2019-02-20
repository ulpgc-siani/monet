/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.jdom.Element;
import org.monet.space.kernel.constants.JSONField;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryHTML;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class Indicator extends BaseObject {
	private String nodeId;
	private String attributeId;
	private int order;
	private String data;

	public static final String CODE = "code";
	public static final String VALUE = "value";
	public static final String SUPER = "super";
	public static final String OTHER = "other";
	public static final String RESULT_TYPE = "resulttype";
	public static final String DESCRIPTOR = "descriptor";
	public static final String NODE_LINK = "nodelink";
	public static final String NODE = "node";
	public static final String INTERNAL = "internal";
	public static final String CHECKED = "checked";
	public static final String METRIC = "metric";
	public static final String DETAILS = "details";
	public static final String VALID = "valid";
	public static final String REASON = "reason";
	public static final String LOCATION = "location";
	public static final String CONTACT = "contact";
	public static final String DATE = "date";
	public static final String SOURCE = "source";
	public static final String FROM = "from";
	public static final String CONDITIONED = "conditioned";
	public static final String EXTENDED = "extended";

	public static String SUPER_DATA_PATTERN = "#sda:([^:]*):([^#]*)#";

	public static String getSuperDataAddress(String code, String superDataId) {
		return "#sda:" + code + ":" + superDataId + "#";
	}

	public Indicator(String code, int order, String data) {
		super();
		this.code = code;
		this.nodeId = Strings.UNDEFINED_ID;
		this.attributeId = Strings.UNDEFINED_ID;
		this.order = order;
		this.data = data;
	}

	public Indicator() {
		this(Strings.UNDEFINED_ID, 0, Strings.EMPTY);
	}

	public Indicator(Indicator indicator) {
		this.code = indicator.getCode();
		this.nodeId = indicator.getNodeId();
		this.attributeId = indicator.getAttributeId();
		this.order = indicator.getOrder();
		this.data = indicator.getData();
	}

	public String getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(String id) {
		this.nodeId = id;
	}

	public String getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(String id) {
		this.attributeId = id;
	}

	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "indicator");
		serializer.attribute("", "code", this.code);
		serializer.attribute("", "order", String.valueOf(this.order));
		if (this.data != null)
			serializer.text(this.data);
		serializer.endTag("", "indicator");
	}

	public void unserializeFromXML(Element element) {

		if (element.getAttribute("code") != null) this.code = element.getAttributeValue("code");
		if (element.getAttribute("order") != null) this.order = new Integer(element.getAttributeValue("order"));
		this.data = LibraryHTML.unescape(element.getText(), 0);

	}

	public void fromJson(String data) throws ParseException {
		JSONObject jsonData = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(data);

		if (jsonData.containsKey(JSONField.ID)) this.setId((String) jsonData.get(JSONField.ID));
		if (jsonData.containsKey(JSONField.ID_NODE)) this.setNodeId((String) jsonData.get(JSONField.ID_NODE));
		if (jsonData.containsKey(JSONField.CODE)) this.setCode((String) jsonData.get(JSONField.CODE));
		if (jsonData.containsKey(JSONField.VALUE)) this.setData((String) jsonData.get(JSONField.VALUE));
		if (jsonData.containsKey(JSONField.ORDER)) this.setOrder((Integer) jsonData.get(JSONField.ORDER));

	}
}
