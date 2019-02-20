package client.services.http.builders;

import client.core.model.List;
import client.core.system.Source;
import client.services.http.HttpInstance;

public class SourceBuilder extends EntityBuilder<Source, client.core.model.Source, List<client.core.model.Source>> implements Builder<client.core.model.Source, List<client.core.model.Source>> {

	@Override
	public client.core.model.Source build(HttpInstance instance) {
		if (instance == null)
			return null;

		client.core.system.Source source = new client.core.system.Source();
		initialize(source, instance);
		return source;
	}

}
