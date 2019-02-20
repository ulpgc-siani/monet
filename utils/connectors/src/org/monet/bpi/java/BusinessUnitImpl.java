package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.bpi.BusinessUnit;

public class BusinessUnitImpl extends BusinessUnit {

	public static void init() {
		instance = new BusinessUnitImpl();
	}

	@Override
	protected String getLabelImpl() {
		throw new NotImplementedException();
	}

	@Override
	protected String getNameImpl() {
		throw new NotImplementedException();
	}

}
