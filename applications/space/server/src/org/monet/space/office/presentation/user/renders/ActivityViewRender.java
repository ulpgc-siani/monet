package org.monet.space.office.presentation.user.renders;

public class ActivityViewRender extends ProcessViewRender {

	public ActivityViewRender() {
		super();
	}

	@Override
	protected void init() {
		loadCanvas("view.task.activity");
		super.init();
	}

}
