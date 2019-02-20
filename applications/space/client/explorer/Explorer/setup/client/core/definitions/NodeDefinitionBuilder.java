package client.core.definitions;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.ContainerDefinition;
import client.core.model.definition.entity.EntityDefinition;
import client.core.model.definition.entity.FormDefinition;
import client.core.model.definition.views.NodeViewDefinition;

public class NodeDefinitionBuilder {

	public static ContainerDefinition buildContainer(final String label, final String description, final boolean isEnvironment) {
		return new ContainerDefinition() {
			@Override
			public List<OperationDefinition> getOperations() {
				return null;
			}

			@Override
			public List getViews() {
				return null;
			}

			@Override
			public NodeViewDefinition getView(String key) {
				return null;
			}

			@Override
			public boolean isReadonly() {
				return false;
			}

			@Override
			public boolean isEnvironment() {
				return isEnvironment;
			}

			@Override
			public String getCode() {
				return null;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getLabel() {
				return label;
			}

			@Override
			public String getDescription() {
				return description;
			}

			@Override
			public Instance.ClassName getClassName() {
				return ContainerDefinition.CLASS_NAME;
			}
		};
	}

	public static EntityDefinition buildEnvironmentContainer(String label, String description) {
		return buildContainer(label, description, true);
	}

	public static FormDefinition buildForm(final String label, final String description, final boolean isEnvironment) {
		client.core.system.definition.entity.FormDefinition definition = new client.core.system.definition.entity.FormDefinition();
		definition.setLabel(label);
		definition.setDescription(description);
		definition.setEnvironment(isEnvironment);
		return definition;
	}
}
