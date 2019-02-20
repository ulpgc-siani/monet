package org.monet.space.explorer.model;

import java.util.Map;

public class Report {
	private Map<String, Integer> values;

	public Report() {
	}

	public Report(Map<String, Integer> values) {
		this.values = values;
	}

	public Map<String, Integer> getValues() {
		return values;
	}

	public void setValues(Map<String, Integer> values) {
		this.values = values;
	}
}
