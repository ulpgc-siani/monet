package client.services.http.builders;

import client.core.model.List;
import client.core.system.Index;
import client.core.system.MonetList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class IndexBuilder implements Builder<client.core.model.Index, List<client.core.model.Index>> {
	@Override
	public client.core.model.Index build(HttpInstance instance) {
		if (instance == null)
			return null;

		Index index = new Index();
		initialize(index, instance);
		return index;
	}

	@Override
	public void initialize(client.core.model.Index object, HttpInstance instance) {
		client.core.system.Index index = (client.core.system.Index)object;
		index.setId(instance.getString("id"));
	}

	@Override
	public List<client.core.model.Index> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
