package org.monet.bpi.java;

import org.monet.bpi.Setup;
import org.monet.space.kernel.model.Dictionary;

public class SetupImpl extends Setup {

	@Override
	public String getVariableImpl(String name) {
		return Dictionary.getInstance().getVariable(name);
	}

	public static void init() {
		instance = new SetupImpl();
	}

}
