package org.monet.bpi.java;

import org.monet.bpi.FieldSerial;
import org.monet.api.space.backservice.impl.model.Indicator;

public class FieldSerialImpl extends FieldImpl<String> implements FieldSerial {

	@Override
	public String get() {
		return this.getIndicatorValue(Indicator.VALUE);
	}

	@Override
	public void set(String value) {
		this.setIndicatorValue(Indicator.VALUE, value);
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