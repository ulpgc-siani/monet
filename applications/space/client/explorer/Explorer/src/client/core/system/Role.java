package client.core.system;

import client.core.model.definition.entity.RoleDefinition;

public abstract class Role<Definition extends RoleDefinition> extends Entity<Definition> implements client.core.model.Role<Definition> {
	private Type type;

	public Role() {
	}

	public Role(Type type) {
		this.type = type;
	}

	public Role(String id, String label, Type type) {
		super(id, label);
		this.type = type;
	}

	@Override
	public final boolean isForUser() {
		return getType() == Type.USER;
	}

	@Override
	public final boolean isForService() {
		return getType() == Type.SERVICE;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof String)
			return getId().equals(object);

		if (!(object instanceof Role))
			return false;

		return getId().equals(((Role) object).getId());
	}
}
