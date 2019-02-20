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
import org.monet.api.space.backservice.impl.library.LibraryPath;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class Attribute extends BaseObject implements Comparable<Attribute> {
	private String nodeId;
	private int order;
	private AttributeList attributeList;
	private IndicatorList indicatorList;

	public static final String OPTION = "option";

	public Attribute() {
		super();
		this.nodeId = "-1";
		this.order = -1;
		this.attributeList = new AttributeList();
		this.indicatorList = new IndicatorList();
	}

	private AttributeList getAttributeList(String path) {
		AttributeList attributeList = null;
		Attribute attribute;
		String[] paths;
		int pos = 0;

		paths = LibraryPath.splitAttributePath(path);
		attributeList = this.getAttributeList();
		while (pos < paths.length - 1) {
			attribute = (Attribute) attributeList.get(paths[pos]);
			if (attribute == null) return null;
			attributeList = attribute.getAttributeList();
			pos++;
		}

		return attributeList;
	}

	public String getIdNode() {
		return this.nodeId;
	}

	public void setIdNode(String id) {
		this.nodeId = id;
	}

	public int getOrder() {
		return this.order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public boolean isMultiple() {
		return (this.getAttributeList().getByCode(this.getCode()).size() > 0);
	}

	public AttributeList getAttributeList() {
		return this.attributeList;
	}

	public LinkedHashMap<String, Attribute> getAttributes(String path) {
		Attribute attribute = this.getAttribute(path);
		MonetHashMap<Attribute> attributes;
		LinkedHashMap<String, Attribute> resultMap = new LinkedHashMap<String, Attribute>();
		Iterator<String> iterator;

		if (attribute == null) return resultMap;

		attributes = attribute.getAttributeList().get();
		iterator = attributes.keySet().iterator();

		while (iterator.hasNext()) {
			String code = iterator.next();
			resultMap.put(code, (Attribute) attributes.get(code));
		}

		return resultMap;
	}

	public Attribute getFirstAttribute(String sPath) {
		LinkedHashMap<String, Attribute> attributesMap = this.getAttributes(sPath);
		Attribute result = new Attribute();
		Iterator<Attribute> iterator;

		iterator = attributesMap.values().iterator();
		if (!iterator.hasNext()) return result;

		return iterator.next();
	}

	public Attribute getAttribute(String path) {
		AttributeList attributeList = this.getAttributeList(path);
		HashMap<String, Attribute> attributesMap;
		Iterator<String> iterator;
		Attribute result = new Attribute();
		String codeAttribute;

		if (attributeList == null) return result;

		codeAttribute = LibraryPath.getAttributeCode(path);
		attributesMap = attributeList.getByCode(codeAttribute);
		iterator = attributesMap.keySet().iterator();

		if (iterator.hasNext()) {
			result = attributesMap.get(iterator.next());
		}

		return result;
	}

	public void setAttributeList(AttributeList attributeList) {
		this.attributeList = attributeList;
	}

	public IndicatorList getIndicatorList() {
		return this.indicatorList;
	}

	public MonetHashMap<Indicator> getIndicators() {
		return this.getIndicatorList().get();
	}

	public void setIndicatorList(IndicatorList indicatorList) {
		this.indicatorList = indicatorList;
	}

	public Indicator getIndicator(String path) {
		Attribute attribute = null;
		String attributePath = null, codeAttribute = null, codeIndicator;

		if (path.indexOf(LibraryPath.SEPARATOR) == -1) {
			attributePath = null;
			codeAttribute = null;
			codeIndicator = path;
		} else {
			attributePath = LibraryPath.getAttributePath(path);
			codeAttribute = LibraryPath.getAttributeCode(attributePath);
			codeIndicator = LibraryPath.getIndicatorCode(path);
		}

		if (codeAttribute == null) attribute = this;
		else attribute = this.getAttribute(attributePath);

		if (attribute == null) return null;

		return (Indicator) attribute.getIndicatorList().get(codeIndicator);
	}

	public String getIndicatorValue(String path) {
		Indicator indicator;
		Attribute attribute = null;
		String attributePath = null, codeAttribute = null, codeIndicator;

		if (path.indexOf(LibraryPath.SEPARATOR) == -1) {
			attributePath = null;
			codeAttribute = null;
			codeIndicator = path;
		} else {
			attributePath = LibraryPath.getAttributePath(path);
			codeAttribute = LibraryPath.getAttributeCode(attributePath);
			codeIndicator = LibraryPath.getIndicatorCode(path);
		}

		if (codeAttribute == null) attribute = this;
		else attribute = this.getAttribute(attributePath);

		if (attribute == null) return "";
		if ((indicator = (Indicator) attribute.getIndicatorList().get(codeIndicator)) == null) return "";

		return indicator.getData();
	}

	public void addOrSetIndicatorValue(String code, int order, String value) {
		Indicator indicator = this.getIndicator(code);
		if (indicator == null) {
			indicator = new Indicator(code, order, value);
			this.indicatorList.add(indicator);
		}
		indicator.setData(value);
	}

	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "attribute");
		serializer.attribute("", "code", this.code);
		serializer.attribute("", "order", String.valueOf(this.order));

		this.attributeList.serializeToXML(serializer);
		this.indicatorList.serializeToXML(serializer);

		serializer.endTag("", "attribute");
	}

	public void deserializeFromXML(Element element) throws ParseException {

		if (element.getAttribute("code") != null) this.code = element.getAttributeValue("code");
		if (element.getAttribute("order") != null) this.order = new Integer(element.getAttributeValue("order"));

		this.attributeList.clear();
		this.attributeList.deserializeFromXML(element.getChild("attributelist"));

		this.indicatorList.clear();
		this.indicatorList.deserializeFromXML(element.getChild("indicatorlist"));
	}

	public int compareTo(Attribute compareAttribute) {
		return (this.getOrder() - compareAttribute.getOrder());
	}

	public void merge(Attribute attribute) {
		this.code = attribute.getCode();
		this.order = attribute.getOrder();

		this.indicatorList.merge(attribute.getIndicatorList());
		this.attributeList.merge(attribute.getAttributeList());
	}

}
