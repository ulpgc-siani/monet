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

package org.monet.v2.metamodel.internal;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

public class SchemaDefinition {

	private static final String SCHEMA_NODE = "<schema xmlns:m=\"http://www.monetproject.com/schemas/model\">%s</schema>";

	private HashMap<String, String> attributesFields;
	private HashMap<String, String> fieldsAttributes;

	public SchemaDefinition() {
		this.attributesFields = new HashMap<String, String>();
		this.fieldsAttributes = new HashMap<String, String>();
	}

	public HashMap<String, String> getFieldsAttributesMap() {
		return this.fieldsAttributes;
	}

	public HashMap<String, String> getAttributesFieldsMap() {
		return this.attributesFields;
	}

	@SuppressWarnings("unchecked")
	public Boolean unserializeFromXML(String content) throws JDOMException {
		SAXBuilder saxBuilder = new SAXBuilder();
		StringReader stringReader;
		org.jdom.Document document;
		Element element;
		List<Element> attributes;

		try {
			stringReader = new StringReader(String.format(SCHEMA_NODE, content));
			document = saxBuilder.build(stringReader);
			element = document.getRootElement();

			this.attributesFields.clear();
			this.fieldsAttributes.clear();
			attributes = XPath.selectNodes(element, "*");
			for (Element attribute : attributes) {
				Element fieldValue = attribute.getChild("field-value", Namespace.getNamespace("http://www.monetproject.com/schemas/model"));
				if (fieldValue != null) {
					this.attributesFields.put(attribute.getName(), fieldValue.getAttributeValue("field"));
					this.fieldsAttributes.put(fieldValue.getAttributeValue("field"), attribute.getName());
				}
			}
		} catch (JDOMException exception) {
			throw exception;
		} catch (IOException e) {
		}

		return true;
	}

}
