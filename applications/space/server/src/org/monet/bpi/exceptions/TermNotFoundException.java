package org.monet.bpi.exceptions;

import org.monet.bpi.Field;

public class TermNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5625114889350557456L;

	private Field<?> bpiField;

	public TermNotFoundException(Field<?> bpiField) {
		this.bpiField = bpiField;
	}

	public Field<?> getField() {
		return this.bpiField;
	}

}
