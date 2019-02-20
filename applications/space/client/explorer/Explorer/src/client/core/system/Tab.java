package client.core.system;

import client.core.model.Entity;
import client.core.model.Space;
import client.core.model.View;

public class Tab implements Space.Tab {
	private String label;
	private boolean active;
	private Type type;
	private View view;
	private Entity entity;

	public Tab() {
	}

	public Tab(Entity entity, View view) {
		this.entity = entity;
		this.view = view;
	}

	@Override
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	public View getView() {
		return this.view;
	}

	public void setView(View view) {
		this.view = view;
	}
}
