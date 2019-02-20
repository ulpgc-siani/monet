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
import java.util.LinkedHashSet;
import java.util.List;

public class Term extends BaseObject {
	private String sourceId;
	private String sourceCode;
	private HashMap<String, String> attributes;
	private LinkedHashSet<Term> terms;
	private String parent;
	private int type = Term.TERM;
	private boolean isEnable = true;
	private boolean isNew = false;
	private boolean isLeaf = false;
	private int ancestorLevel = 1;
	private LinkedHashSet<String> tagsSet;

	public static final int TERM = 0;
	public static final int SUPER_TERM = 1;
	public static final int CATEGORY = 2;

	public static final String CODE = "code";
	public static final String LABEL = "label";
	public static final String FLATTEN_LABEL = "flatten_label";

	public Term() {
		super();
		this.sourceId = null;
		this.attributes = new HashMap<String, String>();
		this.terms = new LinkedHashSet<Term>();
		this.tagsSet = new LinkedHashSet<String>();
	}

	public Term(String code, String label) {
		this();
		this.sourceId = null;
		this.attributes = new HashMap<String, String>();
		this.terms = new LinkedHashSet<Term>();
		this.setCode(code);
		this.setLabel(label);
		this.setFlattenLabel(label);
	}

	public void setSourceId(String id) {
		this.sourceId = id;
	}

	public String getSourceId() {
		return this.sourceId;
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

	@Override
	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
	}

	@Override
	public void deserializeFromXML(Element element) throws ParseException {

		for (org.jdom.Attribute attribute : (List<org.jdom.Attribute>) element.getAttributes())
			this.addAttribute(attribute.getName(), attribute.getValue());

		if (element.getAttribute("source") != null)
			this.sourceId = element.getAttributeValue("source");
		if (element.getAttribute("type") != null)
			this.type = Integer.valueOf(element.getAttributeValue("type"));
		if (element.getAttribute("depth") != null)
			this.ancestorLevel = Integer.valueOf(element.getAttributeValue("depth"));
		if (element.getAttribute("leaf") != null)
			this.isLeaf = Boolean.valueOf(element.getAttributeValue("leaf"));
		if (element.getAttribute("tags") != null)
			this.tagsSet = SerializerData.deserializeSet(element.getAttributeValue("tags"));
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

	public LinkedHashSet<String> getTags() {
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

	public void setTags(LinkedHashSet<String> tagsSet) {
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

	public LinkedHashSet<Term> getTermList() {
		return this.terms;
	}

	public void setTermList(LinkedHashSet<Term> terms) {
		this.terms = terms;
	}

}
