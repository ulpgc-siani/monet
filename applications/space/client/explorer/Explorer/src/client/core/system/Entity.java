package client.core.system;

import client.core.model.definition.entity.EntityDefinition;

public abstract class Entity<Definition extends EntityDefinition> implements client.core.model.Entity<Definition> {
	private String id;
	private client.core.model.Key key;
	protected Type type;
	private String label;
	private String description;
	private client.core.model.Entity owner;
	private client.core.model.List<client.core.model.Entity> ancestors = new MonetList<>();
	private Definition definition;

	public Entity() {
		this(null, null);
	}

	public Entity(String id, String label) {
		this.id = id;
		this.label = label;
		this.key = null;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public client.core.model.Key getKey() {

		if (key != null)
			return key;

		return new Key(getDefinition().getCode(), getDefinition().getName());
	}

	public void setKey(client.core.model.Key key) {
		this.key = key;
	}

	@Override
	public final Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getLabel() {
		return this.label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public client.core.model.Entity getOwner() {
		return this.owner;
	}

	@Override
	public void setOwner(client.core.model.Entity owner) {
		this.owner = owner;
	}

	@Override
	public client.core.model.List<client.core.model.Entity> getAncestors() {
		return this.ancestors;
	}

	@Override
	public void setAncestors(client.core.model.List<client.core.model.Entity> ancestors) {
		this.ancestors = ancestors;
	}

	@Override
	public Definition getDefinition() {
		return this.definition;
	}

	@Override
	public final void setDefinition(Definition definition) {
		this.definition = definition;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof client.core.model.Entity))
			return false;

		if (getId() == null)
			return false;

		return ((client.core.model.Entity) obj).getId() != null && ((client.core.model.Entity) obj).getId().equals(getId());

	}

}
