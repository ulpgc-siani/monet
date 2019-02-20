package org.monet.bpi.java;

import org.monet.bpi.BusinessUnit;

public class BusinessUnitImpl extends BusinessUnit {

	public static void init() {
		instance = new BusinessUnitImpl();
	}

	@Override
	protected String getLabelImpl() {
		return org.monet.space.kernel.model.BusinessUnit.getInstance().getLabel();
	}

	@Override
	protected String getNameImpl() {
		return org.monet.space.kernel.model.BusinessUnit.getInstance().getName();
	}

}
