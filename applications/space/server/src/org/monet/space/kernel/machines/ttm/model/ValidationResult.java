package org.monet.space.kernel.machines.ttm.model;

import java.util.LinkedHashMap;

public class ValidationResult {

	private LinkedHashMap<String, String> errors = new LinkedHashMap<String, String>();

	public void addError(String fieldCode, String message) {
		this.errors.put(fieldCode, message);
	}

	public boolean isValid() {
		return this.errors.size() == 0;
	}

	public LinkedHashMap<String, String> getErrors() {
		return this.errors;
	}

}
