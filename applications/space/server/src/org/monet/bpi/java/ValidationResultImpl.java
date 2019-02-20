package org.monet.bpi.java;

import org.monet.bpi.ValidationResult;


public class ValidationResultImpl implements ValidationResult {

	org.monet.space.kernel.machines.ttm.model.ValidationResult inner;

	void inject(org.monet.space.kernel.machines.ttm.model.ValidationResult inner) {
		this.inner = inner;
	}

	public void addError(String fieldCode, String message) {
		this.inner.addError(fieldCode, message);
	}

}
