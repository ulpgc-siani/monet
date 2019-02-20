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

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.monet.bpi.types.Date;
import org.monet.bpi.types.Number;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.space.kernel.constants.Common;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class Reference extends BaseObject {
	private boolean isDirty;
	private HashMap<String, ReferenceAttribute<?>> attributes;

	public Reference(String code) {
		super();
		this.code = code;
		this.attributes = new HashMap<String, ReferenceAttribute<?>>();
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

	public IndexDefinition getDefinition() {
		Dictionary dictionary;

		if (this.code.equals(DescriptorDefinition.CODE)) return new DescriptorDefinition();

		dictionary = BusinessUnit.getInstance().getBusinessModel().getDictionary();

		return dictionary.getIndexDefinition(this.code);
	}

	public Number getIdNode() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_ID_NODE)) return new Number(-1);
		return (Number) this.attributes.get(DescriptorDefinition.ATTRIBUTE_ID_NODE).getValue();
	}

	public Boolean isEditable() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_EDITABLE)) return false;
		return (Boolean) this.attributes.get(DescriptorDefinition.ATTRIBUTE_EDITABLE).getValue();
	}

	public void setEditable(Boolean editable) {
		this.attributes.put(DescriptorDefinition.ATTRIBUTE_EDITABLE, new ReferenceAttribute<Boolean>(DescriptorDefinition.ATTRIBUTE_EDITABLE, editable));
		this.isDirty = true;
	}

	public Boolean isDeletable() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_DELETABLE)) return false;
		return (Boolean) this.attributes.get(DescriptorDefinition.ATTRIBUTE_DELETABLE).getValue();
	}

	public void setDeletable(Boolean deletable) {
		this.attributes.put(DescriptorDefinition.ATTRIBUTE_DELETABLE, new ReferenceAttribute<Boolean>(DescriptorDefinition.ATTRIBUTE_DELETABLE, deletable));
		this.isDirty = true;
	}

	public String getLabel() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_LABEL)) return "";
		return (String) this.attributes.get(DescriptorDefinition.ATTRIBUTE_LABEL).getValue();
	}

	public String getShortLabel(Integer length) {
		String label = this.getLabel();
		if (label == null || label.length() <= length) return label;
		return label.substring(0, length) + "...";
	}

	public String getShortLabel() {
		return this.getShortLabel(Common.Lengths.SHORT_LABEL);
	}

	public void setLabel(String label) {
		this.attributes.put(DescriptorDefinition.ATTRIBUTE_LABEL, new ReferenceAttribute<String>(DescriptorDefinition.ATTRIBUTE_LABEL, label));
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
		return this.getShortDescription(Common.Lengths.SHORT_DESCRIPTION);
	}

	public void setDescription(String description) {
		this.attributes.put(DescriptorDefinition.ATTRIBUTE_DESCRIPTION, new ReferenceAttribute<String>(DescriptorDefinition.ATTRIBUTE_DESCRIPTION, description));
		this.isDirty = true;
	}

	public String getColor() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_COLOR)) return "";
		return (String) this.attributes.get(DescriptorDefinition.ATTRIBUTE_COLOR).getValue();
	}

	public void setColor(String color) {
		this.attributes.put(DescriptorDefinition.ATTRIBUTE_COLOR, new ReferenceAttribute<String>(DescriptorDefinition.ATTRIBUTE_COLOR, color));
		this.isDirty = true;
	}

	public Number getOrdering() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_ORDERING)) return new Number(-1);
		return (Number) this.attributes.get(DescriptorDefinition.ATTRIBUTE_ORDERING).getValue();
	}

	public void setOrdering(Number ordering) {
		this.attributes.put(DescriptorDefinition.ATTRIBUTE_ORDERING, new ReferenceAttribute<Number>(DescriptorDefinition.ATTRIBUTE_ORDERING, ordering));
		this.isDirty = true;
	}

	public Date getCreateDate() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_CREATE_DATE)) return null;
		return (Date) this.attributes.get(DescriptorDefinition.ATTRIBUTE_CREATE_DATE).getValue();
	}

	public void setCreateDate(Date createDate) {
		this.attributes.put(DescriptorDefinition.ATTRIBUTE_CREATE_DATE, new ReferenceAttribute<Date>(DescriptorDefinition.ATTRIBUTE_CREATE_DATE, createDate));
		this.isDirty = true;
	}

	public Date getUpdateDate() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_UPDATE_DATE)) return null;
		return (Date) this.attributes.get(DescriptorDefinition.ATTRIBUTE_UPDATE_DATE).getValue();
	}

	public void setUpdateDate(Date updateDate) {
		this.attributes.put(DescriptorDefinition.ATTRIBUTE_UPDATE_DATE, new ReferenceAttribute<Date>(DescriptorDefinition.ATTRIBUTE_UPDATE_DATE, updateDate));
		this.isDirty = true;
	}

	public Date getDeleteDate() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_DELETE_DATE)) return null;
		return (Date) this.attributes.get(DescriptorDefinition.ATTRIBUTE_DELETE_DATE).getValue();
	}

	public void setDeleteDate(Date deleteDate) {
		this.attributes.put(DescriptorDefinition.ATTRIBUTE_DELETE_DATE, new ReferenceAttribute<Date>(DescriptorDefinition.ATTRIBUTE_DELETE_DATE, deleteDate));
		this.isDirty = true;
	}

	public Boolean isHighlighted() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_HIGHLIGHTED)) return false;
		return (Boolean) this.attributes.get(DescriptorDefinition.ATTRIBUTE_HIGHLIGHTED).getValue();
	}

	public void setHighlighted(Boolean highlighted) {
		this.attributes.put(DescriptorDefinition.ATTRIBUTE_HIGHLIGHTED, new ReferenceAttribute<Boolean>(DescriptorDefinition.ATTRIBUTE_HIGHLIGHTED, highlighted));
		this.isDirty = true;
	}

	public Boolean isPrototype() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_PROTOTYPE)) return false;
		return (Boolean) this.attributes.get(DescriptorDefinition.ATTRIBUTE_PROTOTYPE).getValue();
	}

	public void setPrototype(Boolean prototype) {
		this.attributes.put(DescriptorDefinition.ATTRIBUTE_PROTOTYPE, new ReferenceAttribute<Boolean>(DescriptorDefinition.ATTRIBUTE_PROTOTYPE, prototype));
		this.isDirty = true;
	}

	public Boolean isGeoReferenced() {
		if (!this.attributes.containsKey(DescriptorDefinition.ATTRIBUTE_GEOREFERENCED)) return false;
		Number value = (Number) this.attributes.get(DescriptorDefinition.ATTRIBUTE_GEOREFERENCED).getValue();
		return (value != null && value.intValue() == 1);
	}

	public void setGeoReferenced(Number value) {
		this.attributes.put(DescriptorDefinition.ATTRIBUTE_GEOREFERENCED, new ReferenceAttribute<Number>(DescriptorDefinition.ATTRIBUTE_GEOREFERENCED, value));
		this.isDirty = true;
	}

	public HashMap<String, ReferenceAttribute<?>> getAttributes() {
		return this.attributes;
	}

	public Boolean existsAttribute(String code) {
		return this.attributes.containsKey(code);
	}

	public ReferenceAttribute<?> getAttribute(String code) {
		if (!this.attributes.containsKey(code)) return null;
		return this.attributes.get(code);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttributeValue(String code) {
		ReferenceAttribute<T> attribute = (ReferenceAttribute<T>) this.getAttribute(code);

		if (attribute == null) return null;

		return attribute.getValue();
	}

	public String getAttributeValueAsString(String code) {
		ReferenceAttribute<?> attribute = (ReferenceAttribute<?>) this.getAttribute(code);
		if (attribute == null) return null;
		return attribute.getValueAsString();
	}

	@SuppressWarnings("unchecked")
	public <T> void setAttributeValue(String code, T value) {
		ReferenceAttribute<T> attribute = (ReferenceAttribute<T>) this.getAttribute(code);

		if (attribute == null) {
			attribute = new ReferenceAttribute<T>(code);
			this.attributes.put(attribute.getCode(), attribute);
		}

		attribute.setValue(value);
		this.isDirty = true;
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		JSONArray attributes = new JSONArray();
		Boolean bPartialLoading = this.isPartialLoading();
		Iterator<String> oIterator = this.attributes.keySet().iterator();

		if (bPartialLoading) this.disablePartialLoading();

		while (oIterator.hasNext()) {
			String code = oIterator.next();
			String value = this.getAttributeValueAsString(code);
			JSONObject attribute = new JSONObject();
			attribute.put("code", code);
			attribute.put("value", value);
			attributes.add(attribute);
		}

		result.put("attributes", attributes);

		if (bPartialLoading) this.enablePartialLoading();

		return result;
	}

	public Reference clone() {
		Reference reference = new Reference(this.code);

		reference.code = this.getCode();
		reference.isDirty = this.isDirty;

		for (ReferenceAttribute<?> attribute : this.getAttributes().values()) {
			ReferenceAttribute<?> newAttribute = attribute.clone();
			reference.attributes.put(attribute.getCode(), newAttribute);
		}

		return reference;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "reference");

		serializer.attribute("", "code", this.code);

		for (String code : this.attributes.keySet()) {
			String value = this.getAttributeValueAsString(code);
			serializer.startTag("", "attribute");
			serializer.attribute("", "code", code);
			serializer.attribute("", "value", value);
			serializer.endTag("", "attribute");
		}

		serializer.endTag("", "reference");
	}

}