package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.bpi.Setup;

public class SetupImpl extends Setup {

	@Override
	public String getVariableImpl(String name) {
		throw new NotImplementedException();
	}

	public static void init() {
		instance = new SetupImpl();
	}

}
