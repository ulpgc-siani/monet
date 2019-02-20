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
import org.jdom.Element;
import org.monet.metamodel.TermProperty;
import org.monet.metamodel.ThesaurusDefinition;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.*;

public class TermList extends BaseModelList<Term> {
	private String language;

	public TermList() {
		super();
		this.totalCount = 0;
	}

	public TermList(int totalCount) {
		this();
		this.totalCount = totalCount;
	}

	public TermList(ArrayList<TermProperty> termDefinitions) {
		Language language = Language.getInstance();
		for (TermProperty termDefinition : termDefinitions) {
			Term term = new Term();
			term.setCode(termDefinition.getKey());
			term.setLabel(language.getModelResource(termDefinition.getLabel()));
			term.setTags(ThesaurusDefinition.getTags(termDefinition));
			term.setType(typeForTermDefinition(termDefinition));
			this.add(term);
		}
		this.setTotalCount(this.items.size());
	}

	private int typeForTermDefinition(TermProperty termDefinition) {
		if (termDefinition.isCategory()) {
			return Term.CATEGORY;
		}
		if (termDefinition.getTermPropertyList() != null && termDefinition.getTermPropertyList().size() > 0) {
			return Term.SUPER_TERM;
		}
		return Term.TERM;
	}

	public TermList(AttributeList attributeList) {
		for (Attribute attribute : attributeList) {
			Term term = new Term();
			term.setCode(attribute.getIndicatorValue(Indicator.CODE));
			term.setLabel(attribute.getIndicatorValue(Indicator.VALUE));
			term.setLeaf(true);
			term.setType(Term.TERM);
			this.add(term);
		}
		this.setTotalCount(this.items.size());
	}

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public MonetHashMap<Term> get() {
		return this.items;
	}

	public List<Term> getAll() {
		final List<Term> list = new ArrayList<>();
		for (Term term : items.values()) {
			list.add(term);
			list.addAll(getAll(term));
		}
		return list;
	}

	private List<Term> getAll(Term parent) {
		final List<Term> list = new ArrayList<>();
		for (Term term : parent.getTermList()) {
			list.add(term);
			if (!term.isLeaf()) list.addAll(getAll(term));
		}
		return list;
	}

	public Term get(String code) {
		if (!this.items.containsKey(code)) return null;
		return this.items.get(code);
	}

	public void add(Term term) {
		if (this.items.containsKey(term.getCode())) return;
		this.items.put(term.getCode(), term);
	}

	public Boolean setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
		return true;
	}

	public Term first() {
		Iterator<Term> iter = this.items.values().iterator();
		if (!iter.hasNext()) return null;
		return iter.next();
	}

	public TermList subList(Integer startPos, Integer limit) {
		TermList result = new TermList(this.totalCount);
		Iterator<String> iter = this.items.keySet().iterator();
		Integer pos = 0;
		Integer count = 0;

		while ((count < limit) && (iter.hasNext())) {
			String codeTerm = iter.next();
			pos++;
			if (pos >= startPos) {
				result.add(this.items.get(codeTerm));
				count++;
			}
		}

		return result;
	}

	public TermList subList(Integer startPos) {
		return this.subList(startPos, this.items.size());
	}

	public JSONObject toJson() {
		Iterator<String> iter = this.items.keySet().iterator();
		Vector<JSONObject> resultVector = new Vector<JSONObject>();
		JSONObject result = new JSONObject();

		while (iter.hasNext()) {
			Term term = this.items.get(iter.next());
			resultVector.add(term.toJSON());
		}

		result.put("nrows", String.valueOf(this.totalCount));
		result.put("rows", resultVector.toArray());

		return result;
	}

	@SuppressWarnings("unchecked")
	public void unserializeFromXML(Element element) {
		List<Element> terms;
		Iterator<Element> iter;

		if (element.getAttribute("language") != null) this.language = element.getAttributeValue("language");
		if (element.getAttribute("total-count") != null)
			this.totalCount = Integer.parseInt(element.getAttributeValue("total-count"));

		this.items.clear();
		terms = element.getChildren("term");
		iter = terms.iterator();
		while (iter.hasNext()) {
			Element termElement = iter.next();
			Term term = new Term();
			term.unserializeFromXML(termElement);
			this.items.put(term.getCode(), term);
		}
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "term-list");
		if (this.language != null)
			serializer.attribute("", "language", this.language);
		serializer.attribute("", "total-count", String.valueOf(this.totalCount));

		for (Term term : this.items.values())
			term.serializeToXML(serializer, depth);

		serializer.endTag("", "term-list");
	}

}
