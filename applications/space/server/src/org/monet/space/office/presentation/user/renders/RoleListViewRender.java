package org.monet.space.office.presentation.user.renders;

import java.util.HashMap;

public class RoleListViewRender extends OfficeRender {

	public RoleListViewRender() {
		super();
	}

	private String initRoleDefinitionTemplate() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		return block("view$roleDefinitionTemplate:client-side", map);
	}

	private String initRoleTemplate() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		return block("view$roleTemplate:client-side", map);
	}

	@Override
	protected void init() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		loadCanvas("view.rolelist");

		map.put("id", this.getParameterAsString("id"));
		map.put("roleDefinitionTemplate", this.initRoleDefinitionTemplate());
		map.put("roleTemplate", this.initRoleTemplate());

		addMark("view", block("view", map));
	}

}