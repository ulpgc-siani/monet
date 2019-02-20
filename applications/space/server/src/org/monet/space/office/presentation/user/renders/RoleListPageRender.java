package org.monet.space.office.presentation.user.renders;

import org.monet.space.kernel.model.RoleList;

public class RoleListPageRender extends OfficeRender {

	public RoleListPageRender() {
		super();
	}

	@Override
	protected void init() {
		loadCanvas("page.rolelist");

		OfficeRender roleListRender = this.rendersFactory.get(new RoleList(), "preview.html?mode=view", this.renderLink, account);
		roleListRender.setParameters(this.getParameters());

		addMark("render(view.rolelist)", roleListRender.getOutput());
	}

}