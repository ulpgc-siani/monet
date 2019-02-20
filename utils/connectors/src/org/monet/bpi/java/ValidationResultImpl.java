package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.bpi.ValidationResult;


public class ValidationResultImpl implements ValidationResult {

	@Override
	public void addError(String fieldCode, String message) {
		throw new NotImplementedException();
	}
}
