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

package org.monet.api.space.backservice.impl.model;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.HashMap;

public abstract class BaseObject {
	protected String id;
	protected String code;
	protected String name;
	protected HashMap<String, Boolean> dirtyMap;

	public BaseObject() {
		this.id = "";
		this.code = "";
		this.name = "";
		this.dirtyMap = new HashMap<String, Boolean>();
	}

	public boolean isDirty(String code) {
		if (!this.dirtyMap.containsKey(code))
			return false;
		return this.dirtyMap.get(code);
	}

	public void setDirty(String code, Boolean value) {
		this.dirtyMap.put(code, value);
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void clone(BaseObject baseModel) {
	}

	public abstract void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException;

	public String serializeToXML() throws IllegalArgumentException, IllegalStateException, IOException, XmlPullParserException {
		StringWriter writer = new StringWriter();
		XmlSerializer serializer = org.xmlpull.v1.XmlPullParserFactory.newInstance().newSerializer();

		serializer.setOutput(writer);
		this.serializeToXML(serializer);

		return writer.toString();
	}

	public void deserializeFromXML(String content) throws JDOMException, IOException, ParseException {
		SAXBuilder builder = new SAXBuilder();
		StringReader reader;
		org.jdom.Document document;
		Element node;

		if (content.isEmpty())
			return;

		while (!content.substring(content.length() - 1).equals(">"))
			content = content.substring(0, content.length() - 1);

		content = content.trim();
		reader = new StringReader(content);

		document = builder.build(reader);
		node = document.getRootElement();
		this.deserializeFromXML(node);
	}

	public abstract void deserializeFromXML(Element element) throws ParseException;

}
