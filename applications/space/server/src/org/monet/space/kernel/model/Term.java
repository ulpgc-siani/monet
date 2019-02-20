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
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class Term extends BaseObject {
	private String sourceId;
	private String sourceCode;
	private Map<String, String> attributes;
	private Set<Term> terms;
	private String parent;
	private int type = Term.TERM;
	private boolean isEnable = true;
	private boolean isNew = false;
	private boolean isLeaf = false;
	private int ancestorLevel = 1;
	private Set<String> tagsSet;

	public static final int TERM = 0;
	public static final int SUPER_TERM = 1;
	public static final int CATEGORY = 2;

	public static final String CODE = "code";
	public static final String LABEL = "label";
	public static final String FLATTEN_LABEL = "flatten_label";

	public Term() {
		this.sourceId = null;
		this.attributes = new HashMap<>();
		this.terms = new LinkedHashSet<>();
		this.tagsSet = new LinkedHashSet<>();
	}

	public Term(String code, String label) {
		this();
		setCode(code);
		setLabel(label);
		setFlattenLabel(label);
	}

	public void setSourceId(String id) {
		this.sourceId = id;
	}

	public void setSourceCode(String code) {
		this.sourceCode = code;
	}

	public String getCode() {
		return this.getAttribute(Term.CODE);
	}

	public void setCode(String code) {
		this.addAttribute(Term.CODE, code);
	}

	public String getLabel() {
		return this.getAttribute(Term.LABEL);
	}

	public void setLabel(String label) {
		this.addAttribute(Term.LABEL, label);
	}

	public String getFlattenLabel() {
		return this.getAttribute(Term.FLATTEN_LABEL);
	}

	public void setFlattenLabel(String flattenLabel) {
		this.addAttribute(Term.FLATTEN_LABEL, flattenLabel);
	}

	public Set<String> getTags() {
		return this.tagsSet;
	}

	public String getTag(String name) {

		for (String tag : this.tagsSet) {
			String[] tagArray = tag.split("=");
			if (tagArray.length >= 2 && tagArray[0] == name)
				return tagArray[1];
		}

		return null;
	}

	public void addTag(String value) {
		this.tagsSet.add(value);
	}

	public void setTags(Set<String> tagsSet) {
		this.tagsSet.clear();
		this.tagsSet.addAll(tagsSet);
	}

	public String getParent() {
		return this.parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public void setEnabled(boolean isEnable) {
		this.isEnable = isEnable;
	}

	public boolean isEnabled() {
		return this.isEnable;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean isNew() {
		return this.isNew;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public boolean isLeaf() {
		return this.isLeaf;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

	public boolean isTerm() {
		return this.type == Term.TERM;
	}

	public boolean isSuperTerm() {
		return this.type == Term.SUPER_TERM;
	}

	public boolean isCategory() {
		return this.type == Term.CATEGORY;
	}

	public void setAncestorLevel(int ancestorLevel) {
		this.ancestorLevel = ancestorLevel;
	}

	public int getAncestorLevel() {
		return this.ancestorLevel;
	}

	public String getAttribute(String code) {
		if (!this.attributes.containsKey(code))
			return "";
		return this.attributes.get(code);
	}

	public void addAttribute(String code, String value) {
		this.attributes.put(code, value);
	}

	public Set<Term> getTermList() {
		return this.terms;
	}

	public void setTermList(LinkedHashSet<Term> terms) {
		this.terms = terms;
	}

	@SuppressWarnings("unchecked")
	public void unserializeFromXML(Element element) {

		for (org.jdom.Attribute attribute : (List<org.jdom.Attribute>) element.getAttributes())
			addAttribute(attribute.getName(), attribute.getValue());

		if (element.getAttribute("type") != null)
			type = Integer.valueOf(element.getAttributeValue("type"));
		if (element.getAttribute("depth") != null)
			ancestorLevel = Integer.valueOf(element.getAttributeValue("depth"));
		if (element.getAttribute("leaf") != null)
			isLeaf = Boolean.valueOf(element.getAttributeValue("leaf"));
		if (element.getAttribute("tags") != null)
			tagsSet = SerializerData.deserializeSet(element.getAttributeValue("tags"));

		this.terms.clear();
        List<Element> terms = element.getChildren("term");

        for (Element termElement : terms) {
            Term term = new Term();
            term.unserializeFromXML(termElement);
	        term.setParent(getCode());
            this.terms.add(term);
        }
    }

	@Override
	public void fromJson(String data) throws ParseException {
		JSONObject jsonObject = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(data);
		this.setCode((String) jsonObject.get("code"));
		this.setParent((String) jsonObject.get("parent"));
		this.setLabel((String) jsonObject.get("label"));
		this.setEnabled((Boolean) jsonObject.get("enabled"));
		this.setNew((Boolean) jsonObject.get("new"));
		this.setTags(SerializerData.<LinkedHashSet<String>>deserializeSet((String) jsonObject.get("tags")));
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "term");
		for (Entry<String, String> attribute : this.attributes.entrySet()) {
			String key = attribute.getKey() != null ? attribute.getKey() : "";
			String value = attribute.getValue() != null ? attribute.getValue() : "";
			serializer.attribute("", key, value);
		}

		serializer.attribute("", "type", String.valueOf(this.type));
		serializer.attribute("", "depth", String.valueOf(this.ancestorLevel));
		serializer.attribute("", "leaf", String.valueOf(this.isLeaf));
		serializer.attribute("", "tags", SerializerData.serializeSet(this.tagsSet));

		if (!this.isEnable)
			serializer.attribute("", "enable", "false");

		if (!this.isNew)
			serializer.attribute("", "isNew", "false");

		for (Term term : this.terms)
			term.serializeToXML(serializer, depth);
		serializer.endTag("", "term");
	}

	public JSONObject toJSON() {
		JSONObject result = new JSONObject();

        for (String code : attributes.keySet()) {
            result.put(code, attributes.get(code));
        }

		result.put("id", attributes.get("code"));
		result.put("idSource", sourceId);
		result.put("codeSource", sourceCode);
		result.put("typeSource", Source.getType(sourceCode));
		result.put("isEditableSource", Source.isEditable(sourceCode));
		result.put("type", String.valueOf(type));
		result.put("enable", isEnable);
		result.put("isNew", isNew);
		result.put("parent", parent);
		result.put("tags", SerializerData.serializeSet(tagsSet));

		return result;
	}

}
