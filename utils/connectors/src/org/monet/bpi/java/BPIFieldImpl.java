package org.monet.bpi.java;

import org.monet.api.backservice.BackserviceApi;
import org.monet.api.backservice.impl.model.Attribute;
import org.monet.api.backservice.impl.model.AttributeList;
import org.monet.api.backservice.impl.model.Indicator;
import org.monet.bpi.BPIField;
import org.monet.v2.BPIClassLocator;
import org.monet.v2.metamodel.FieldDeclaration;
import org.monet.v2.model.Dictionary;

public abstract class BPIFieldImpl<V> implements BPIField<V> {

	String nodeId;
	FieldDeclaration fieldDeclaration;
	Attribute attribute;
	BackserviceApi api;
	BPIClassLocator bpiClassLocator;
	Dictionary dictionary;

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	void setFieldDeclaration(FieldDeclaration fieldDeclaration) {
		this.fieldDeclaration = fieldDeclaration;
	}

	Attribute getAttribute(String code) {
		if (this.attribute == null) return null;
		return this.attribute.getAttributeList().get(code);
	}

	protected String getIndicatorValue(String code) {
		if (this.attribute == null) return "";
		return this.attribute.getIndicatorValue(code);
	}

	String getIndicatorValue(Attribute attribute, String code) {
		if (attribute == null) return "";
		return attribute.getIndicatorValue(code);
	}

	protected void setIndicatorValue(String code, String value) {
		Indicator indicator;

		if (this.attribute == null)
			return;

		indicator = this.attribute.getIndicator(code);

		if (indicator == null) {
			indicator = new Indicator(code, -1, value);
			this.attribute.getIndicatorList().add(indicator);
		}

		indicator.setData(value);
	}

	void setAttribute(String code, Attribute attribute) {
		AttributeList attributeList;

		if (this.attribute == null) return;

		attributeList = this.attribute.getAttributeList();
		attributeList.add(attribute);
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public String getCode() {
		return this.fieldDeclaration.getCode();
	}

	@Override
	public String getName() {
		return this.fieldDeclaration.getName();
	}

	@Override
	public String getLabel() {
		return this.fieldDeclaration.getLabel();
	}
}
