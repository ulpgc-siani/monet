package org.monet.bpi;

public abstract class BusinessUnit {

	protected static BusinessUnit instance;

	public static String getName() {
		return instance.getNameImpl();
	}

	public static String getLabel() {
		return instance.getLabelImpl();
	}

	protected abstract String getNameImpl();

	protected abstract String getLabelImpl();

}
