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

import com.google.inject.Injector;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.guice.InjectorFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

public abstract class BaseObject extends PartialLoader {
	private static final int MAX_DEPTH = 1000;
	protected String id;
	protected String code;
	protected String name;
	protected HashMap<String, Boolean> dirtyMap;

	public BaseObject() {
		this.id = Strings.UNDEFINED_ID;
		this.code = Strings.UNDEFINED_CODE;
		this.name = Strings.UNDEFINED_KEY;
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

	public BaseObject clone() {
		return null;
	}

	public String serializeToXML() {
		return this.serializeToXML(false, MAX_DEPTH);
	}

	public String serializeToXML(int depth) {
		return this.serializeToXML(false, depth);
	}

	public String serializeToXML(boolean addHeader) {
		return this.serializeToXML(addHeader, MAX_DEPTH);
	}

	public String serializeToXML(boolean addHeader, int depth) {
		StringWriter writer = new StringWriter();
		try {
			Injector injector = InjectorFactory.getInstance();
			XmlSerializer serializer = injector.getInstance(XmlSerializer.class);
			serializer.setOutput(writer);

			if (addHeader)
				serializer.startDocument("UTF-8", true);

			this.serializeToXML(serializer, depth);

			serializer.endDocument();
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
			throw new DataException(ErrorCode.SERIALIZE_TO_XML, null, e);
		}

		return writer.toString();
	}

	public abstract void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException;

	public void deserializeFromXML(String content, ModelChangesResolver resolver) {
		SAXBuilder oBuilder = new SAXBuilder();
		StringReader oReader;
		org.jdom.Document oDocument;
		Element node;
		try {
			oReader = new StringReader(content);
			oDocument = oBuilder.build(oReader);
			node = oDocument.getRootElement();
			this.unserializeFromXML(node);
		} catch (JDOMException oException) {
			throw new DataException(ErrorCode.UNSERIALIZE_NODE, content, oException);
		} catch (IOException oException) {
			throw new DataException(ErrorCode.UNSERIALIZE_NODE, content, oException);
		}
	}

	public void unserializeFromXML(Element element) {

	}

	public JSONObject toJson() {
		return new JSONObject();
	}

	public void fromJson(String data) throws ParseException {
	}

}
