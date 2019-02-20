package org.monet.bpi.java;

import org.monet.api.backservice.impl.model.Indicator;
import org.monet.bpi.BPIFieldNumber;
import org.monet.bpi.types.Number;
import org.monet.v2.metamodel.NumberFieldDeclaration;

import java.text.DecimalFormat;

public class BPIFieldNumberImpl extends BPIFieldImpl<org.monet.bpi.types.Number> implements BPIFieldNumber {

	@Override
	public Number get() {
		String internalValue = this.attribute.getIndicatorValue(Indicator.INTERNAL);
		String metricValue = this.attribute.getIndicatorValue(Indicator.METRIC);
		Double value = null;

		if (internalValue == null || internalValue.isEmpty()) { // Backward compatibility
			internalValue = this.attribute.getIndicatorValue(Indicator.VALUE);
			internalValue = internalValue.replace(".", "");
			internalValue = internalValue.replace(",", ".");
		}

		if (internalValue == null || internalValue.isEmpty())
			return null;

		try {
			value = Double.parseDouble(internalValue);
		} catch (NumberFormatException ex) {
			return null;
		}

		Number number = new Number(value, this.attribute.getIndicatorValue(Indicator.VALUE));
		number.setMetric(metricValue);
		return number;
	}

	@Override
	public void set(Number value) {
		Indicator valueIndicator = this.attribute.getIndicator(Indicator.VALUE);
		Indicator internalIndicator = this.attribute.getIndicator(Indicator.INTERNAL);
		Indicator metricIndicator = this.attribute.getIndicator(Indicator.METRIC);

		if (internalIndicator == null) {
			internalIndicator = new Indicator(Indicator.INTERNAL, -1, "");
			this.attribute.getIndicatorList().add(internalIndicator);
		}
		if (valueIndicator == null) {
			valueIndicator = new Indicator(Indicator.VALUE, -1, "");
			this.attribute.getIndicatorList().add(valueIndicator);
		}
		if (metricIndicator == null) {
			metricIndicator = new Indicator(Indicator.METRIC, -1, "");
			this.attribute.getIndicatorList().add(metricIndicator);
		}

		NumberFieldDeclaration.Format formatDeclaration = ((NumberFieldDeclaration) this.fieldDeclaration).getFormat();
		String format = formatDeclaration != null ? formatDeclaration.getValue() : "0.##";
		DecimalFormat formatter = new DecimalFormat(format);

		internalIndicator.setData(String.valueOf(value.doubleValue()));
		valueIndicator.setData(formatter.format(value.doubleValue()));
		if (value.getMetric() != null)
			metricIndicator.setData(value.getMetric());
	}

	@Override
	public boolean equals(Object value) {
		if (value instanceof java.lang.Number)
			return this.get().equals(value);
		else
			return false;
	}

	@Override
	public boolean isValid() {
		String internalValue = this.attribute.getIndicatorValue(Indicator.INTERNAL);

		if (internalValue == null || internalValue.isEmpty()) { // Backward compatibility
			internalValue = this.attribute.getIndicatorValue(Indicator.VALUE);
			internalValue = internalValue.replace(".", "");
			internalValue = internalValue.replace(",", ".");
		}

		if (internalValue == null || internalValue.isEmpty())
			return true;

		try {
			Double.parseDouble(internalValue);
		} catch (NumberFormatException ex) {
			return false;
		}

		return true;
	}

	@Override
	public void clear() {
		this.set(new Number(0));
	}

	@Override
	public String getFormattedValue() {
		return this.attribute.getIndicatorValue(Indicator.VALUE);
	}
}