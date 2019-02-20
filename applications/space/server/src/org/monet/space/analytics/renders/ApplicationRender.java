package org.monet.space.analytics.renders;

public class ApplicationRender extends DatawareHouseRender {

	public ApplicationRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
	}

	@Override
	protected void init() {
		loadCanvas("application");
		this.addCommonMarks(this.markMap);
		addMark("command", this.getParameterAsString("command"));
	}

}
