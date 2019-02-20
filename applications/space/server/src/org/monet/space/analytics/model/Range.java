package org.monet.space.analytics.model;

public class Range {
	private String measureUnit;
	private int mode;
	private Double min;
	private Double max;

	public static final int ABSOLUTE = 0;
	public static final int RELATIVE = 1;

	public static int modeFromString(String mode) {
		if (mode.equalsIgnoreCase("absolute"))
			return ABSOLUTE;
		else if (mode.equalsIgnoreCase("relative"))
			return RELATIVE;
		return -1;
	}

	public Range(String measureUnit, int mode, Double min, Double max) {
		this.measureUnit = measureUnit;
		this.mode = mode;
		this.min = min;
		this.max = max;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public int getMode() {
		return mode;
	}

	public Double getMin() {
		return min;
	}

	public Double getMax() {
		return max;
	}

}
