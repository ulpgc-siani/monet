package org.monet.space.frontservice.control.exceptions;

public class ServiceUseNotFoundException extends Exception {

	private static final long serialVersionUID = -3983537394055234343L;

	public ServiceUseNotFoundException(String id) {
		super(String.format("Service use with id '%s' not found in database.", id));
	}

}
