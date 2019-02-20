package client.core.system;

import client.core.model.definition.entity.RoleDefinition;

public class ServiceRole extends Role<RoleDefinition> implements client.core.model.ServiceRole<RoleDefinition> {

	public ServiceRole() {
		super(Type.SERVICE);
	}

	public ServiceRole(String id, String label) {
		super(id, label, Type.SERVICE);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.ServiceRole.CLASS_NAME;
	}

}
