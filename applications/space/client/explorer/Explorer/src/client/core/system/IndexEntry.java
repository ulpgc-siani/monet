package client.core.system;

import client.core.model.Entity;
import client.core.model.factory.TypeFactory;

public class IndexEntry<T extends Entity> implements client.core.model.IndexEntry<T> {
	private String label;
	private T entity;
	public TypeFactory typeFactory;

	public void setTypeFactory(TypeFactory factory) {
		this.typeFactory = factory;
	}

	@Override
	public T getEntity() {
		return this.entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	@Override
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public final boolean equals(Object obj) {
		if (!(obj instanceof IndexEntry))
			return false;

		Entity entryEntity = ((IndexEntry) obj).getEntity();
		return !(getEntity() != null && entryEntity != null) || getEntity().equals(entryEntity);
	}
}
