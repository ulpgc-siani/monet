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
import org.monet.metamodel.GlossaryDefinition;
import org.monet.metamodel.SourceDefinition;
import org.monet.metamodel.ThesaurusDefinition;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;

public abstract class Source<T extends SourceDefinition> extends Entity<T> {
	private String partnerName;
	private String partnerLabel;
	private FeederUri uri;
	private String label;
	private TermList termList;
	private Date createDate;
	private Date updateDate;
	private boolean isEnabled;
	private SourceLink sourceLink;
	private boolean editable;

	public boolean isEditable() {
		SourceDefinition sourceDefinition = this.getDefinition();
		return (sourceDefinition instanceof ThesaurusDefinition && !((ThesaurusDefinition) sourceDefinition).isSelfGenerated()) || (sourceDefinition instanceof GlossaryDefinition);
	}

	public static enum Type {
		Thesaurus,
		Glossary
	}

	public Source() {
		this(null, null);
	}

	public Source(String code, SourceLink sourceLink) {
		super();
		this.code = code;
		this.termList = new TermList();
		this.createDate = null;
		this.updateDate = null;
		this.isEnabled = false;
		this.sourceLink = sourceLink;
	}

	@SuppressWarnings("unchecked")
	public static <S extends SourceDefinition> Source<S> createInstance(String code, SourceLink sourceLink) {
		SourceDefinition definition = Dictionary.getInstance().getSourceDefinition(code);
		Source<S> source = null;

		if (definition instanceof ThesaurusDefinition)
			source = (Source<S>) new Thesaurus(code, sourceLink);
		else if (definition instanceof GlossaryDefinition)
			source = (Source<S>) new Glossary(code, sourceLink);

		source.setCode(code);

		return source;
	}

	public String getKey() {
		return this.getCode() + "_" + this.getId();
	}

	@SuppressWarnings("unchecked")
	public T getDefinition() {
		Dictionary dictionary = BusinessUnit.getInstance().getBusinessModel().getDictionary();
		return (T) dictionary.getSourceDefinition(this.code);
	}

	public String getLabel() {
		return this.label;
	}

	public String getInstanceLabel() {
		return this.partnerLabel;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getDescription() {
		return this.getDefinition().getDescriptionString();
	}

	public static Type getType(String sourceCode) {
		Dictionary dictionary = BusinessUnit.getInstance().getBusinessModel().getDictionary();
		SourceDefinition definition = dictionary.getSourceDefinition(sourceCode);

		if (definition instanceof ThesaurusDefinition)
			return Type.Thesaurus;

		if (definition instanceof GlossaryDefinition)
			return Type.Glossary;

		return null;
	}

	public static Boolean isEditable(String sourceCode) {
		Dictionary dictionary = BusinessUnit.getInstance().getBusinessModel().getDictionary();
		SourceDefinition definition = dictionary.getSourceDefinition(sourceCode);

		if (!(definition instanceof ThesaurusDefinition))
			return false;

		return !((ThesaurusDefinition)definition).isSelfGenerated();
	}

	public Type getType() {
		return Source.getType(this.getDefinition().getCode());
	}

	public boolean isThesaurus() {
		return this.getType() == Type.Thesaurus;
	}

	public boolean isSelfGenerated() {
		if (!this.isThesaurus())
			return false;

		ThesaurusDefinition definition = (ThesaurusDefinition) this.getDefinition();
		return definition.isSelfGenerated();
	}

	public boolean isGlossary() {
		return this.getType() == Type.Glossary;
	}

	public String getPartnerName() {
		return this.partnerName;
	}

	public void setPartnerName(String name) {
		this.partnerName = name;
	}

	public String getPartnerLabel() {
		return this.partnerLabel;
	}

	public void setPartnerLabel(String label) {
		this.partnerLabel = label;
	}

	public FeederUri getUri() {
		return this.uri;
	}

	public void setUri(FeederUri uri) {
		this.uri = uri;
	}

	public TermList getTermList() {
		return this.termList;
	}

	public void setTermList(TermList termList) {
		this.termList = termList;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public void setEnabled(boolean value) {
		this.isEnabled = value;
	}

	@SuppressWarnings("unchecked")
	public void populate(String locationChain) {
		this.sourceLink.populate((Source<SourceDefinition>) this);
	}

	@SuppressWarnings("unchecked")
	public TermList loadTerms(DataRequest dataRequest, boolean onlyEnabled) {
		return this.sourceLink.loadTerms((Source<SourceDefinition>) this, dataRequest, onlyEnabled);
	}

	@SuppressWarnings("unchecked")
	public TermList loadTerms(boolean onlyEnabled) {
		return this.sourceLink.loadTerms((Source<SourceDefinition>) this, onlyEnabled);
	}

	@SuppressWarnings("unchecked")
	public TermList searchTerms(DataRequest dataRequest) {
		return this.sourceLink.searchTerms((Source<SourceDefinition>) this, dataRequest);
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		Boolean isPartialLoading = isPartialLoading();

		if (isPartialLoading) disablePartialLoading();

		result.put("id", getId());
		result.put("code", getCode());
		result.put("type", getType());
		result.put("label", getLabel());
		result.put("editable", isEditable());

		addJsonAttributes(result);

		if (isPartialLoading) enablePartialLoading();

		return result;
	}

	protected abstract void addJsonAttributes(JSONObject result);

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "source");
		serializer.attribute("", "id", id);
		serializer.attribute("", "name", name);
		serializer.attribute("", "code", code);

		termList.serializeToXML(serializer, depth);

		serializer.endTag("", "source");
	}

}