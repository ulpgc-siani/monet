package client.services.http.builders;

import client.core.system.UserRole;

import client.services.http.HttpInstance;

public class UserRoleBuilder extends RoleBuilder<client.core.model.UserRole> {

	@Override
	public client.core.model.UserRole build(HttpInstance instance) {
		if (instance == null)
			return null;

		UserRole role = new UserRole();
		initialize(role, instance);
		return role;
	}

}
