package client.services.http.builders.definition.entity;

import client.services.http.HttpInstance;

import java.util.HashMap;
import java.util.Map;

import static client.core.model.definition.entity.TaskDefinition.Type;

public class TaskDefinitionBuilder<T extends client.core.model.definition.entity.TaskDefinition> extends EntityDefinitionBuilder<T> {

	private static final Map<Type, TaskDefinitionBuilder> taskDefinitionCreators = new HashMap<Type, TaskDefinitionBuilder>() {{
		put(Type.SERVICE, new ServiceDefinitionBuilder());
		put(Type.ACTIVITY, new ActivityDefinitionBuilder());
		put(Type.JOB, new JobDefinitionBuilder());
	}};

	@Override
	public T build(HttpInstance instance) {
		if (instance == null)
			return null;

		Type type = Type.fromString(instance.getString("type"));
		return (T) taskDefinitionCreators.get(type).build(instance);
	}

}