package org.monet.space.explorer.model;

public interface BusinessUnit {

	public enum Type {
		MEMBER, PARTNER
	}

	public String getName();
	public Type getType();
	public String getUrl();
	public boolean isActive();
	public boolean isDisabled();

}
