package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.api.space.backservice.impl.model.Attribute;
import org.monet.api.space.backservice.impl.model.AttributeList;
import org.monet.api.space.backservice.impl.model.Indicator;
import org.monet.bpi.Field;
import org.monet.metamodel.Dictionary;
import org.monet.metamodel.FieldProperty;
import org.monet.v3.BPIClassLocator;

public abstract class FieldImpl<V> implements Field<V> {

	String nodeId;
	FieldProperty fieldDefinition;
	Attribute attribute;
	protected BPIClassLocator bpiClassLocator;
	protected Dictionary dictionary;
	protected BackserviceApi api;

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	void setFieldDefinition(FieldProperty fieldDefinition) {
		this.fieldDefinition = fieldDefinition;
	}

	Attribute getAttribute(String code) {
		if (this.attribute == null) return null;
		return this.attribute.getAttributeList().get(code);
	}

	String getIndicatorValue(String code) {
		if (this.attribute == null) return "";
		return this.attribute.getIndicatorValue(code);
	}

	String getIndicatorValue(Attribute attribute, String code) {
		if (attribute == null) return "";
		return attribute.getIndicatorValue(code);
	}

	void setIndicatorValue(String code, String value) {
		Indicator indicator;

		if (this.attribute == null) return;

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
	public String getCode() {
		return this.fieldDefinition.getCode();
	}

	@Override
	public String getName() {
		return this.fieldDefinition.getName();
	}

	@Override
	public String getLabel() {
		return (String)this.fieldDefinition.getLabel();
	}

	@Override
	public <T extends FieldProperty> T getDefinition() {
		return (T) this.fieldDefinition;
	}

}
