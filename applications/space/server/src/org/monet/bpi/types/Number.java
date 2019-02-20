package org.monet.bpi.types;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class Number {

	public static final String INTERNAL = "internal";
	public static final String METRIC = "metric";

	@Attribute(name = INTERNAL)
	private Double value;
	@Attribute(name = METRIC, required = false)
	private String metric;
	@Text
	private String formattedValue;

	public Number() {
	}

	public Number(Number number) {
		this.value = new Double(number.value);
		this.formattedValue = new String(number.formattedValue);
	}

	public Number(double value) {
		this.value = value;
		this.formattedValue = String.valueOf(value);
	}

	public Number(String value) {
		this(value != null && !value.isEmpty() ? Double.parseDouble(value) : 0);
		this.formattedValue = value;
	}

	public Number(double value, String formattedValue) {
		this.value = value;
		this.formattedValue = formattedValue;
	}

	public Number(String value, String formattedValue) {
		this(Double.parseDouble(value), formattedValue);
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public void setValue(Integer value) {
		this.value = value.doubleValue();
	}

	public void setValue(String value) {
		this.value = Double.valueOf(value != null && !value.isEmpty() ? Double.parseDouble(value) : 0);
	}

	public double doubleValue() {
		return value;
	}

	public int intValue() {
		return value.intValue();
	}

	public String textValue() {
		return String.valueOf(value);
	}

	public void setFormattedValue(String formattedValue) {
		this.formattedValue = formattedValue;
	}

	public String formattedValue() {
		return formattedValue != null ? formattedValue : String.valueOf(value);
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public String getMetric() {
		return metric;
	}

	@Override
	public String toString() {
		return this.formattedValue();
	}

}
