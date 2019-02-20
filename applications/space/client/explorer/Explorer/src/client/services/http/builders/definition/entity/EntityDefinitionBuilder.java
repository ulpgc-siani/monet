package client.services.http.builders.definition.entity;

import client.core.model.definition.entity.EntityDefinition.Type;
import client.core.system.definition.entity.EntityDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.DefinitionBuilder;

import java.util.HashMap;
import java.util.Map;

public class EntityDefinitionBuilder<T extends client.core.model.definition.entity.EntityDefinition> extends DefinitionBuilder<T> {

	private static final Map<Type, EntityDefinitionBuilder> builders = new HashMap<Type, EntityDefinitionBuilder>() {{
		put(Type.SERVICE, new ServiceDefinitionBuilder());
		put(Type.ACTIVITY, new ActivityDefinitionBuilder());
		put(Type.JOB, new JobDefinitionBuilder());

		put(Type.DESKTOP, new DesktopDefinitionBuilder());
		put(Type.CONTAINER, new ContainerDefinitionBuilder());
		put(Type.COLLECTION, new CollectionDefinitionBuilder());
		put(Type.CATALOG, new CatalogDefinitionBuilder());
		put(Type.FORM, new FormDefinitionBuilder());
		put(Type.DOCUMENT, new DocumentDefinitionBuilder());

		put(Type.INDEX, new IndexDefinitionBuilder());
	}};

	@Override
	public T build(HttpInstance instance) {
		if (instance == null)
			return null;

		Type type = Type.fromString(instance.getString("type"));
		if (builders.containsKey(type))
			return (T) builders.get(type).build(instance);

		EntityDefinition definition = new EntityDefinition();
		initialize((T)definition, instance);
		return (T)definition;
	}

}