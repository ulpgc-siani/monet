package client.services.http.builders.fields;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.fields.Field;
import client.core.system.fields.MultipleField;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;
import client.services.http.builders.EntityBuilder;

import static client.core.model.Field.Type;

public class FieldBuilder<T extends client.core.model.Field> extends EntityBuilder<Field, T, List<T>> implements Builder<T, List<T>> {

	@Override
	public void initialize(T object, HttpInstance instance) {
		super.initialize(object, instance);

		Field field = (Field)object;
		field.setCode(instance.getString("code"));
		field.setType(Type.fromString(instance.getString("type")));
		field.setLabel(instance.getString("label"));

		if (field instanceof MultipleField)
			((MultipleField) field).setEntityFactory(new EntityBuilder<>());
	}

	@Override
	public List<T> buildList(HttpList instance) {
		List<T> result = new MonetList<>();

		for (int i = 0; i < instance.getItems().length(); i++)
			result.add(build(instance.getItems().get(i)));

		return result;
	}
}
