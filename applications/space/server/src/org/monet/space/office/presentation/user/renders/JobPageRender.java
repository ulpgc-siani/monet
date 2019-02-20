package org.monet.space.office.presentation.user.renders;

public class JobPageRender extends TaskPageRender {

	public JobPageRender() {
		super();
	}

	@Override
	protected void init() {
		loadCanvas("page.task.job");
		super.init();
	}

}
