package org.monet.space.analytics.renders;

import org.monet.space.analytics.model.Language;

public class HomeRender extends DatawareHouseRender {

	public HomeRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
	}

	@Override
	protected void init() {
		loadCanvas("home");

		this.initLoading();
	}

	private void initLoading() {
		this.addCommonMarks(this.markMap);

		addMark("language", Language.getCurrent());
		addMark("operation", this.getParameterAsString("operation"));
		addMark("parameters", this.getParameterAsString("parameters"));
	}

}
