package org.monet.bpi.java;

import org.monet.api.space.backservice.impl.model.Attribute;
import org.monet.api.space.backservice.impl.model.Indicator;
import org.monet.bpi.FieldMemo;
import org.monet.metamodel.MemoFieldProperty;

public class FieldMemoImpl extends FieldImpl<String> implements FieldMemo {

	@Override
	public String get() {
		return this.getIndicatorValue(Indicator.VALUE);
	}

	@Override
	public void set(String value) {
		this.setIndicatorValue(Indicator.VALUE, value);
	}

	public static void set(Attribute attribute, MemoFieldProperty fieldDefinition, String value) {
		FieldMemoImpl fieldMemo = new FieldMemoImpl();
		fieldMemo.attribute = attribute;
		fieldMemo.fieldDefinition = fieldDefinition;
		fieldMemo.set(value);
	}

	@Override
	public boolean equals(Object value) {
		if (value instanceof String)
			return this.get().equals(value);
		else
			return false;
	}

	@Override
	public void clear() {
		this.set("");
	}

}