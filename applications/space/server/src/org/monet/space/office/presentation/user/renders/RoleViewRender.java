package org.monet.space.office.presentation.user.renders;

import org.monet.space.kernel.model.UserRole;

import java.util.HashMap;

public class RoleViewRender extends OfficeRender {
	private UserRole role;

	public RoleViewRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.role = (UserRole) target;
	}

	private String initRoleTemplate() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		return block("view$roleTemplate:client-side", map);
	}

	@Override
	protected void init() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		loadCanvas("view.source");

		map.put("id", this.getParameterAsString("id"));
		map.put("code", this.role.getCode());
		map.put("label", this.role.getLabel());
		map.put("roleTemplate", this.initRoleTemplate());

		addMark("view", block("view", map));
	}

}