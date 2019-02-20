package client.services.http.builders;

import client.core.model.List;
import client.core.system.IndexEntry;
import client.core.system.MonetList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public abstract class IndexEntryBuilder<T extends client.core.model.Entity, I extends client.core.model.IndexEntry<T>> implements Builder<I, List<I>> {

	@Override
	public I build(HttpInstance instance) {
		if (instance == null)
			return null;

		I entry = (I) new IndexEntry();
		initialize(entry, instance);
		return entry;
	}

	@Override
	public void initialize(I object, HttpInstance instance) {
		IndexEntry entry = (IndexEntry)object;
		EntityBuilder entityBuilder = getEntityBuilder();
		entry.setLabel(instance.getString("label"));
		entry.setEntity(entityBuilder.build(instance.getObject("entity")));
		entry.setTypeFactory(new EntityBuilder());
	}

	@Override
	public List<I> buildList(HttpList instance) {
		return new MonetList<I>();
	}

	protected abstract EntityBuilder getEntityBuilder();
}
