package client.core.system;

import client.core.model.definition.entity.ServiceDefinition;

public class Service<Definition extends ServiceDefinition> extends Process<Definition> implements client.core.model.Service<Definition> {

	public Service() {
	}

	public Service(String id, String label) {
		super(id, label);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.Service.CLASS_NAME;
	}

}
