package org.monet.bpi.java;

import org.monet.bpi.FieldNumber;
import org.monet.bpi.types.Number;
import org.monet.metamodel.NumberFieldProperty;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.model.Indicator;

import java.text.DecimalFormat;

public class FieldNumberImpl extends FieldImpl<Number> implements FieldNumber {

	@Override
	public Number get() {
		String internalValue = this.attribute.getIndicatorValue(Indicator.INTERNAL);
		String metricValue = this.attribute.getIndicatorValue(Indicator.METRIC);
		Double value = null;

		if (internalValue == null || internalValue.isEmpty()) { // Backward compatibility
			internalValue = this.attribute.getIndicatorValue(Indicator.VALUE);
			internalValue = internalValue.replace(".", "");
			internalValue = internalValue.replace(",", ".");
			internalValue = internalValue.replace(" ", "");
		}

		if (internalValue == null || internalValue.isEmpty())
			return null;

		try {
			value = Double.parseDouble(internalValue);
		} catch (NumberFormatException ex) {
			AgentLogger.getInstance().error(ex);
			return null;
		}

		Number number = new Number(value,
			this.attribute.getIndicatorValue(Indicator.VALUE));
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

		String format = ((NumberFieldProperty) this.fieldDefinition).getFormat();
		if (format == null)
			format = "0.##";
		DecimalFormat formatter = new DecimalFormat(format);

		internalIndicator.setData(String.valueOf(value.doubleValue()));
		valueIndicator.setData(formatter.format(value.doubleValue()));
		if (value.getMetric() != null)
			metricIndicator.setData(value.getMetric());
	}

	@Override
	public boolean equals(Object value) {
		if (value instanceof Number)
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
			internalValue = internalValue.replace(" ", "");
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

}