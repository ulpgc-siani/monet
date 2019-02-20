package client.core.system.definition.entity;

import client.core.system.definition.Definition;

import static client.core.model.Instance.ClassName;

public class EntityDefinition extends Definition implements client.core.model.definition.entity.EntityDefinition {
	private Type type;

	public EntityDefinition() {
	}

	public EntityDefinition(String code, String name, String label, String description) {
		super(code, name, label, description);
	}

	@Override
	public ClassName getClassName() {
		return client.core.model.definition.entity.EntityDefinition.CLASS_NAME;
	}

}
