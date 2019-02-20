package org.monet.space.kernel.exceptions;

public class CantContinueWithoutResponseException extends RuntimeException {

	private static final long serialVersionUID = 6247769570875853978L;

	public CantContinueWithoutResponseException(String msg) {
		super(msg);
	}

}
