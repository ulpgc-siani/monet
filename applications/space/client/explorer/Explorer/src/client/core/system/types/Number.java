package client.core.system.types;

import client.core.model.factory.TypeFactory;

public class Number<Type extends java.lang.Number> implements client.core.model.types.Number<Type> {
	private Type value;
	private TypeFactory factory;

	public Number() {
	}

	public Number(Type value) {
		this.value = value;
	}

	public void setTypeFactory(TypeFactory factory) {
		this.factory = factory;
	}

	@Override
	public Type getValue() {
		return value;
	}

	public void setValue(Type value) {
		this.value = value;
	}

	@Override
	public final void increment() {
		Type value = getValue();

		if (value instanceof Long)
			incrementLong((Number<Long>) factory.createNumber(1l));
		else if (value instanceof Double)
			incrementDouble(minValue());
	}

	public final void increment(java.lang.Number number) {
		Type value = getValue();

		if (value instanceof Long)
			incrementLong((Number<Long>) factory.createNumber(number.longValue()));
		else if (value instanceof Double)
			incrementDouble((Number<Double>)factory.createNumber(number.doubleValue()));
	}

	@Override
	public final void decrement() {
		Type value = getValue();

		if (value instanceof Long)
			decrementLong((Number<Long>)factory.createNumber(1l));
		else if (value instanceof Double)
			decrementDouble(minValue());
	}

	public final void decrement(java.lang.Number number) {
		Type value = getValue();

		if (value instanceof Long)
			decrementLong((Number<Long>)factory.createNumber(number.longValue()));
		else if (value instanceof Double)
			decrementDouble((Number<Double>)factory.createNumber(number.doubleValue()));
	}

	@Override
	public final String toString() {
		return getValue().toString();
	}

	@Override
	public final boolean equals(Object object) {
		return isNumber(object) && Double.valueOf(toString()).equals(Double.valueOf(object.toString()));
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	private boolean isNumber(Object object) {
		return !(object == null || !(object instanceof Number));
	}

	private Number<Double> minValue() {
		Type value = getValue();

		String stringValue = String.valueOf(value);
		if (!stringValue.contains(".")) return (Number<Double>)factory.createNumber((double) 1);

		return (Number<Double>)factory.createNumber(calculateMinValue(stringValue.split("\\.")[1]));
	}

	private Double calculateMinValue(String s) {
		final int numberOfDecimals = s.length();
		String minValue = "0.";
		for (int i = 0; i < numberOfDecimals - 1; i++)
			minValue += "0";
		minValue += "1";
		return Double.valueOf(minValue);
	}

	private void incrementLong(Number<Long> increment) {
		setValue((Type) factory.createNumber(value.longValue() + increment.getValue()).getValue());
	}

	private void incrementDouble(Number<Double> increment) {
		double numberOfDecimals = getNumberOfDecimals(increment.toString());
		double value = Math.round((this.value.doubleValue() + increment.getValue()) * numberOfDecimals) / numberOfDecimals;
		setValue((Type) factory.createNumber(value).getValue());
	}

	private void decrementLong(Number<Long> decrement) {
		setValue((Type) factory.createNumber(value.longValue() - decrement.getValue()).getValue());
	}

	private void decrementDouble(Number<Double> decrement) {
		double numberOfDecimals = getNumberOfDecimals(decrement.toString());
		double value = Math.round((this.value.doubleValue() - decrement.getValue()) * numberOfDecimals) / numberOfDecimals;
		setValue((Type) factory.createNumber(value).getValue());
	}

	private double getNumberOfDecimals(String value) {
		return Math.pow(10, value.substring(value.lastIndexOf(".") + 1).length());
	}
}
