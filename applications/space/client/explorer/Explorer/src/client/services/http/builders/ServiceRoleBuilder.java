package client.services.http.builders;

import client.core.system.ServiceRole;

import client.services.http.HttpInstance;

public class ServiceRoleBuilder extends RoleBuilder<client.core.model.ServiceRole> {

	@Override
	public client.core.model.ServiceRole build(HttpInstance instance) {
		if (instance == null)
			return null;

		ServiceRole role = new ServiceRole();
		initialize(role, instance);
		return role;
	}

}
