package client.services.http.builders.definition;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.definition.Ref;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;

public class RefBuilder implements Builder<client.core.model.definition.Ref, List<client.core.model.definition.Ref>> {

	@Override
	public client.core.model.definition.Ref build(HttpInstance instance) {
		if (instance == null)
			return null;

		Ref ref = new Ref();
		initialize(ref, instance);
		return ref;
	}

	@Override
	public void initialize(client.core.model.definition.Ref object, HttpInstance instance) {
		Ref ref = (Ref)object;
		ref.setDefinition(instance.getString("definition"));
		ref.setValue(instance.getString("value"));
	}

	@Override
	public List<client.core.model.definition.Ref> buildList(HttpList instance) {
		List<client.core.model.definition.Ref> result = new MonetList<>();

		for (int i = 0; i < instance.getItems().length(); i++)
			result.add(build(instance.getItems().get(i)));

		return result;
	}
}
