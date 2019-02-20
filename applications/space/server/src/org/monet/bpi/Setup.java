package org.monet.bpi;

public abstract class Setup {

	protected static Setup instance;

	public static String getVariable(String name) {
		return instance.getVariableImpl(name);
	}

	protected abstract String getVariableImpl(String name);

}