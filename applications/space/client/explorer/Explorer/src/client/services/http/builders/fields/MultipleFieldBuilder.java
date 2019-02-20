package client.services.http.builders.fields;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.fields.MultipleField;
import client.services.http.HttpInstance;
import client.services.http.builders.EntityBuilder;
import com.google.gwt.core.client.JsArray;

public class MultipleFieldBuilder<T extends client.core.model.MultipleField, F extends client.core.model.Field> extends FieldBuilder<T> {
	public static final String VALUE = "value";

	@Override
	public void initialize(T object, HttpInstance instance) {
		super.initialize(object, instance);

		MultipleField field = (MultipleField)object;
		field.setEntityFactory(new EntityBuilder<>());
		field.setFields(buildList(instance, instance.getArray(VALUE)));
	}

	public List<F> buildList(HttpInstance instance, JsArray instances) {
		List<F> result = new MonetList<>();

		for (int i = 0; i < instances.length(); i++) {
			HttpInstance item = createItemInstanceAtPosition(instance, i);
			if (!item.getString(VALUE).isEmpty())
				result.add((F) new FieldBuilder().build(item));
		}

		return result;
	}

	private HttpInstance createItemInstanceAtPosition(HttpInstance context, int position) {
		HttpInstance item = HttpInstance.createInstance();
		item.setString("code", context.getString("code"));
		item.setString("type", context.getString("type"));
		item.setString("label", context.getString("label"));
		item.setArrayValue(context, VALUE, position);
		return item;
	}
}
