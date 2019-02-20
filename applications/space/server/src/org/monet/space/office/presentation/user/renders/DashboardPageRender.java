package org.monet.space.office.presentation.user.renders;

import org.monet.space.kernel.model.Dashboard;

public class DashboardPageRender extends OfficeRender {
	private Dashboard dashboard;

	public DashboardPageRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.dashboard = (Dashboard) target;
	}

	@Override
	protected void init() {
		loadCanvas("page.dashboard");

		OfficeRender render = this.rendersFactory.get(this.dashboard, "preview.html?mode=view", this.renderLink, account);
		render.setParameters(this.getParameters());

		addMark("code", this.dashboard.getCode());
		addMark("label", this.dashboard.getLabel());
		addMark("render(view.dashboard)", render.getOutput());
	}

}