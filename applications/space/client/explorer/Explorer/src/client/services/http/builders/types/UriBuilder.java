package client.services.http.builders.types;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.types.Uri;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;

public class UriBuilder implements Builder<client.core.model.types.Uri, List<client.core.model.types.Uri>> {
	@Override
	public client.core.model.types.Uri build(HttpInstance instance) {
		if (instance == null)
			return null;

		Uri uri = new Uri();
		initialize(uri, instance);
		return uri;
	}

	@Override
	public void initialize(client.core.model.types.Uri object, HttpInstance instance) {
		Uri uri = (Uri)object;
		uri.setValue(instance.getString("value"));
	}

	@Override
	public List<client.core.model.types.Uri> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
