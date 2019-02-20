package client.core.system.definition.entity;

public class ContainerDefinition extends NodeDefinition implements client.core.model.definition.entity.ContainerDefinition {
	private boolean environment;

	@Override
	public boolean isEnvironment() {
		return environment;
	}

	public void setEnvironment(boolean environment) {
		this.environment = environment;
	}
}
