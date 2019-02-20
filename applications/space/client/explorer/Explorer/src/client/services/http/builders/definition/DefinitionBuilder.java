package client.services.http.builders.definition;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.definition.Definition;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;

public class DefinitionBuilder<T extends client.core.model.definition.Definition> implements Builder<T, List<T>> {

	@Override
	public T build(HttpInstance instance) {
		if (instance == null)
			return null;

		Definition definition = new Definition();
		initialize((T)definition, instance);
		return (T)definition;
	}

	@Override
	public void initialize(T object, HttpInstance instance) {
		Definition definition = (Definition)object;
		definition.setCode(instance.getString("code"));
		definition.setName(instance.getString("name"));
		definition.setLabel(instance.getString("label"));
		definition.setDescription(instance.getString("description"));
	}

	@Override
	public List<T> buildList(HttpList instance) {
		List<T> result = new MonetList<>();

		for (int i = 0; i < instance.getItems().length(); i++)
			result.add(build(instance.getItems().get(i)));

		return result;
	}

	protected static Object getObject(HttpInstance instance, String name) {

		if (instance == null)
			return null;

		if (instance.isString(name))
			return instance.getString(name);

		return new RefBuilder().build(instance.getObject(name));
	}

	protected static Object getArrayObject(HttpInstance instance, String arrayName, int pos) {

		if (instance.isItemOfTypeString(arrayName, pos))
			return instance.getItemOfTypeString(arrayName, pos);

		return new RefBuilder().build(instance.getItemOfTypeObject(arrayName, pos));
	}

}
