package org.monet.space.office.presentation.user.renders;

public class ActivityPageRender extends TaskPageRender {

	public ActivityPageRender() {
		super();
	}

	@Override
	protected void init() {
		loadCanvas("page.task.activity");
		super.init();
	}

}
