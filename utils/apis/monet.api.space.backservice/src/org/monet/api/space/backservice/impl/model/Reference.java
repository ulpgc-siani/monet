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
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public class Reference extends BaseObject {
	private boolean isDirty;
	private HashMap<String, ReferenceAttribute> attributes;

	public Reference() {
		this("");
	}

	public Reference(String code) {
		super();
		this.code = code;
		this.attributes = new HashMap<String, ReferenceAttribute>();
	}

	public Reference(Reference reference) {
		this(reference.getCode());
		this.attributes.putAll(reference.getAttributes());
	}

	public boolean isDirty() {
		return this.isDirty;
	}

	public void setDirty(boolean value) {
		this.isDirty = value;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getIdNode() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_ID_NODE)) return -1;
		return Integer.valueOf(this.attributes.get(DescriptorDefinition.ATTRIBUTE_ID_NODE).getValue());
	}

	public Boolean isEditable() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_EDITABLE)) return false;
		return Boolean.valueOf(this.attributes.get(DescriptorDefinition.ATTRIBUTE_EDITABLE).getValue());
	}

	public void setEditable(Boolean editable) {
		this.attributes.put(DescriptorDefinition.ATTRIBUTE_EDITABLE, new ReferenceAttribute(DescriptorDefinition.ATTRIBUTE_EDITABLE, editable.toString()));
		this.isDirty = true;
	}

	public String getLabel() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_LABEL)) return "";
		return (String) this.attributes.get(DescriptorDefinition.ATTRIBUTE_LABEL).getValue();
	}

	public String getShortLabel(Integer length) {
		String label = this.getLabel();
		if (label.length() <= length) return label;
		return label.substring(0, length) + "...";
	}

	public String getShortLabel() {
		return this.getShortLabel(40);
	}

	public void setLabel(String label) {
		this.attributes.put(DescriptorDefinition.ATTRIBUTE_LABEL, new ReferenceAttribute(DescriptorDefinition.ATTRIBUTE_LABEL, label));
		this.isDirty = true;
	}

	public String getDescription() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_DESCRIPTION)) return "";
		return (String) this.attributes.get(DescriptorDefinition.ATTRIBUTE_DESCRIPTION).getValue();
	}

	public String getShortDescription(Integer length) {
		String description = this.getDescription();
		if (description.length() <= length) return description;
		return description.substring(0, length) + "...";
	}

	public String getShortDescription() {
		return this.getShortDescription(40);
	}

	public void setDescription(String description) {
		this.attributes.put(DescriptorDefinition.ATTRIBUTE_DESCRIPTION, new ReferenceAttribute(DescriptorDefinition.ATTRIBUTE_DESCRIPTION, description));
		this.isDirty = true;
	}

	public int getOrdering() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_ORDERING)) return -1;
		return Integer.valueOf(this.attributes.get(DescriptorDefinition.ATTRIBUTE_ORDERING).getValue());
	}

	public void setOrdering(Number ordering) {
		this.attributes.put(DescriptorDefinition.ATTRIBUTE_ORDERING, new ReferenceAttribute(DescriptorDefinition.ATTRIBUTE_ORDERING, String.valueOf(ordering)));
		this.isDirty = true;
	}

	public HashMap<String, ReferenceAttribute> getAttributes() {
		return this.attributes;
	}

	public Boolean existsAttribute(String code) {
		return this.attributes.containsKey(code);
	}

	public ReferenceAttribute getAttribute(String code) {
		if (!this.attributes.containsKey(code)) return null;
		return this.attributes.get(code);
	}

	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "reference");

		serializer.attribute("", "code", this.code);

		for (String code : this.attributes.keySet()) {
			String value = this.attributes.get(code).getValue();
			serializer.startTag("", "attribute");
			serializer.attribute("", "code", code);
			serializer.attribute("", "value", value);
			serializer.endTag("", "attribute");
		}

		serializer.startTag("", "reference");
	}

	@SuppressWarnings("unchecked")
	public void deserializeFromXML(Element node) throws ParseException {

		this.attributes.clear();

		if (node.getAttribute("code") != null)
			this.code = node.getAttributeValue("code");

		List<Element> nodes = node.getChildren("attribute");
		for (Element element : nodes) {
			ReferenceAttribute attribute = new ReferenceAttribute(element.getAttributeValue("code"));
			attribute.setValue(element.getAttributeValue("value"));
			this.attributes.put(attribute.getCode(), attribute);
		}
	}

}