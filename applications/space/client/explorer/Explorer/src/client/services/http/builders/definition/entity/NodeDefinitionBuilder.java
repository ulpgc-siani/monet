package client.services.http.builders.definition.entity;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.definition.entity.NodeDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.entity.views.ViewDefinitionBuilder;
import com.google.gwt.core.client.JsArray;

import java.util.HashMap;
import java.util.Map;

import static client.core.model.definition.entity.NodeDefinition.Type;

public class NodeDefinitionBuilder<T extends client.core.model.definition.entity.NodeDefinition> extends EntityDefinitionBuilder<T> {

	private static final Map<Type, NodeDefinitionBuilder> nodeDefinitionCreators = new HashMap<Type, NodeDefinitionBuilder>() {{
		put(Type.DESKTOP, new DesktopDefinitionBuilder());
		put(Type.CONTAINER, new ContainerDefinitionBuilder());
		put(Type.COLLECTION, new CollectionDefinitionBuilder());
		put(Type.CATALOG, new CatalogDefinitionBuilder());
		put(Type.FORM, new FormDefinitionBuilder());
		put(Type.DOCUMENT, new DocumentDefinitionBuilder());
	}};

	@Override
	public T build(HttpInstance instance) {
		if (instance == null)
			return null;

		Type type = Type.fromString(instance.getString("type"));
		return (T) nodeDefinitionCreators.get(type).build(instance);
	}

	@Override
	public void initialize(T object, HttpInstance instance) {
		super.initialize(object, instance);

		NodeDefinition definition = (NodeDefinition)object;
		definition.setOperations(getOperations(instance.getArray("operations")));
		definition.setViews(new ViewDefinitionBuilder().buildList(instance.getList("views")));
		definition.setReadonly(instance.getBoolean("readonly"));
	}

	private List<client.core.model.definition.entity.NodeDefinition.OperationDefinition> getOperations(JsArray<HttpInstance> instances) {
		List<client.core.model.definition.entity.NodeDefinition.OperationDefinition> operations = new MonetList<>();

		for (int i = 0; i < instances.length(); i++)
			operations.add(getOperation(instances.get(i)));

		return operations;
	}

	private client.core.model.definition.entity.NodeDefinition.OperationDefinition getOperation(HttpInstance instance) {
		NodeDefinition.OperationDefinition operation = new NodeDefinition.OperationDefinition();
		operation.setCode(instance.getString("code"));
		operation.setName(instance.getString("name"));
		operation.setLabel(instance.getString("label"));
		operation.setDescription(instance.getString("description"));
		operation.setEnabled(instance.getBoolean("enabled"));
		operation.setVisible(instance.getBoolean("visible"));
		return operation;
	}


}