package org.monet.space.analytics.renders;

import org.monet.space.analytics.configuration.Configuration;

public class LogoutRender extends DatawareHouseRender {

	public LogoutRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
	}

	@Override
	protected void init() {
		loadCanvas("logout");

		this.addCommonMarks(this.markMap);

		Configuration configuration = Configuration.getInstance();
		addMark("logoutAction", configuration.getUrl() + "?op=logout");
	}

}
