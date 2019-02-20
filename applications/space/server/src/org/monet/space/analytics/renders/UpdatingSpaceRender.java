package org.monet.space.analytics.renders;

import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.office.configuration.Configuration;

public class UpdatingSpaceRender extends DatawareHouseRender {
	private BusinessUnit businessUnit;

	public UpdatingSpaceRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
	}

	@Override
	protected void init() {
		loadCanvas("updatingspace");

		Configuration configuration = Configuration.getInstance();

		if (this.businessUnit == null) return;

		this.addCommonMarks(this.markMap);

		addMark("reloadAction", configuration.getUrl());
	}

	public void setBusinessUnit(BusinessUnit businessUnit) {
		this.businessUnit = businessUnit;
	}

}
