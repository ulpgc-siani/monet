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

import com.google.common.collect.Iterables;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.monet.metamodel.CompositeFieldProperty;
import org.monet.metamodel.FieldProperty;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.JSONField;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryField;
import org.monet.space.kernel.library.LibraryJSON;
import org.monet.space.kernel.library.LibraryPath;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class Attribute extends BaseObject implements Comparable<Attribute> {
	private String nodeId;
	private int order;
	private AttributeList attributeList;
	private IndicatorList indicatorList;
	private String parentCode = null;

	public static final String OPTION = "option";
	public static final String SIGNATURES = "signatures";
	public static final String SIGNATURES_COUNT = "signatures_count";
	public static final String FILTER_PARAMETERS = "filter-parameters";

	public Attribute() {
		super();
		this.nodeId = Strings.UNDEFINED_ID;
		this.order = -1;
		this.attributeList = new AttributeList();
		this.indicatorList = new IndicatorList();
	}

	public Attribute(FieldProperty fieldDefinition) {
		this();
		this.code = fieldDefinition.getCode();

		if (fieldDefinition.isMultiple())
			this.attributeList = new AttributeList();
		else {
			if (fieldDefinition instanceof CompositeFieldProperty) {
				CompositeFieldProperty compositeDefinition = (CompositeFieldProperty) fieldDefinition;
				this.attributeList = new AttributeList(compositeDefinition.getAllFieldPropertyList());
			} else {
				this.attributeList = new AttributeList();
			}
		}
	}

	private AttributeList getAttributeList(String path) {
		AttributeList attributeList;
		Attribute attribute;
		String[] paths;
		int pos = 0;

		paths = LibraryPath.splitAttributePath(path);
		attributeList = this.getAttributeList();
		while (pos < paths.length - 1) {
			attribute = attributeList.get(paths[pos]);
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
		return !this.isMultipleChild() && this.getIndicatorList().getCount() == 0;
	}

	public boolean isMultipleChild() {
		return this.parentCode != null && this.parentCode.equals(this.getCode());
	}

	public AttributeList getAttributeList() {
		onLoad(this, Node.ATTRIBUTELIST);
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
		this.addLoadedAttribute(Node.ATTRIBUTELIST);
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

		if (!path.contains(LibraryPath.SEPARATOR)) {
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

		if (attribute == null) return Strings.EMPTY;
		if ((indicator = attribute.getIndicatorList().get(codeIndicator)) == null) return Strings.EMPTY;

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

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "attribute");
		serializer.attribute("", "code", this.code);
		serializer.attribute("", "order", String.valueOf(this.order));

		this.attributeList.serializeToXML(serializer, depth);
		this.indicatorList.serializeToXML(serializer, depth);

		serializer.endTag("", "attribute");
	}

	public void unserializeFromXML(Element element, ModelChangesResolver resolver) {

		if (element.getAttribute("code") != null) this.code = element.getAttributeValue("code");
		if (element.getAttribute("order") != null) this.order = new Integer(element.getAttributeValue("order"));

		this.attributeList.setCode(this.getCode());
		this.attributeList.deserializeFromXML(element.getChild("attributelist"), resolver);
		this.indicatorList.unserializeFromXML(element.getChild("indicatorlist"));

		this.fillParentCode();

		if (resolver != null)
			resolver.resolve(this);
	}

	private void fillParentCode() {
		for (Attribute attribute : this.attributeList) {
			String parentCode = this.getCode();
			attribute.setParentCode(parentCode);
		}
	}

	public void deserializeFromXML(String content, ModelChangesResolver resolver) {
		SAXBuilder oBuilder = new SAXBuilder();
		StringReader reader;
		org.jdom.Document document;
		Element element;

		if (content.equals(Strings.EMPTY)) return;
		reader = new StringReader(content);

		this.attributeList.clear();
		this.indicatorList.clear();

		try {
			document = oBuilder.build(reader);
			element = document.getRootElement();
			this.unserializeFromXML(element, resolver);
		} catch (JDOMException exception) {
			throw new DataException(ErrorCode.UNSERIALIZE_DEFINITION_FROM_XML, content, exception);
		} catch (IOException exception) {
			throw new DataException(ErrorCode.UNSERIALIZE_DEFINITION_FROM_XML, content, exception);
		}
	}

	public void fromJson(String data) throws ParseException {
		JSONObject jsonAttribute = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(data);
		String key, value, codeIndicator;

		this.indicatorList.clear();

		try {

			key = (String) jsonAttribute.get(JSONField.CODE);
			if (key == null) key = (String) jsonAttribute.get(JSONField.ID);
			if (key == null) key = (String) jsonAttribute.get(JSONField.NAME);

			value = (String) jsonAttribute.get(JSONField.VALUE);

			if (LibraryJSON.isArray(value)) {
				this.indicatorList.fromJson(value);
			} else {
				Indicator indicator = new Indicator();
				codeIndicator = LibraryField.getIndicatorCode(key);
				if ((codeIndicator == null) && (key != null)) codeIndicator = key;
				indicator.setCode(codeIndicator);
				indicator.setData(value);
				this.indicatorList.add(indicator);
			}

		} catch (Exception ex) {
			AgentLogger.getInstance().error(ex);
			throw new DataException(ErrorCode.UNDEFINED, null);
		}
	}

	public int compareTo(Attribute compareAttribute) {
		return (this.getOrder() - compareAttribute.getOrder());
	}

	public void merge(Attribute attribute) {
		this.merge(attribute, false);
	}

	public void merge(Attribute attribute, boolean onlyNotEmpty) {
		this.code = attribute.getCode();
		this.order = attribute.getOrder();

		this.indicatorList.merge(attribute.getIndicatorList(), onlyNotEmpty);
		this.attributeList.merge(attribute.getAttributeList(), onlyNotEmpty);
	}

	private void resolveMultipleable() {
		Dictionary dictionary = Dictionary.getInstance();

		if (!dictionary.existsFieldDefinition(this.code))
			return;

		FieldProperty fieldDefinition = dictionary.getFieldDefinition(this.code);
		if (fieldDefinition.isMultiple() && !this.isMultiple()) {
			if (this.indicatorList.getCount() > 0) {
				Attribute attribute = new Attribute();
				attribute.setCode(this.getCode());
				attribute.setIndicatorList(new IndicatorList(this.indicatorList));
				this.attributeList.clear();
				this.attributeList.add(attribute);
			}
		}
		else if (!fieldDefinition.isMultiple() && this.isMultiple()) {
			Attribute attribute = Iterables.get(this.attributeList.get().values(), 0);
			this.setIndicatorList(new IndicatorList(attribute.getIndicatorList()));
			//this.setAttributeList(new AttributeList());
		}
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
}
