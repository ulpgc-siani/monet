package org.monet.space.office.presentation.user.renders;

import org.monet.space.kernel.model.UserRole;


public class RolePageRender extends OfficeRender {
	private UserRole role;

	public RolePageRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.role = (UserRole) target;
	}

	@Override
	protected void init() {
		loadCanvas("page.role");

		OfficeRender roleRender = this.rendersFactory.get(this.role, "preview.html?mode=view", this.renderLink, account);
		roleRender.setParameters(this.getParameters());

		addMark("label", this.role.getLabel());
		addMark("render(view.role)", roleRender.getOutput());
	}

}