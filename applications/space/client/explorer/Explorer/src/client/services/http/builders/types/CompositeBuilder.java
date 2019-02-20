package client.services.http.builders.types;

import client.core.model.Field;
import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.types.Composite;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;
import client.services.http.builders.fields.FieldBuilder;
import com.google.gwt.core.client.JsArray;

public class CompositeBuilder implements Builder<client.core.model.types.Composite, List<client.core.model.types.Composite>> {
	@Override
	public client.core.model.types.Composite build(HttpInstance instance) {
		if (instance == null)
			return null;

		Composite composite = new Composite();
		initialize(composite, instance);
		return composite;
	}

	@Override
	public void initialize(client.core.model.types.Composite object, HttpInstance instance) {
		Composite composite = (Composite)object;
		JsArray<HttpInstance> fields = instance.getArray("fields");

		for (int i = 0; i < fields.length(); i++)
			composite.add((Field) new FieldBuilder().build(fields.get(i)));
	}

	@Override
	public client.core.model.List<client.core.model.types.Composite> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
