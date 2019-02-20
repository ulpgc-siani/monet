package org.monet.space.office.presentation.user.renders;

public class ServicePageRender extends TaskPageRender {

	public ServicePageRender() {
		super();
	}

	@Override
	protected void init() {
		loadCanvas("page.task.service");
		super.init();
	}

}
