package org.monet.space.explorer.model;

import org.monet.space.kernel.model.Entity;

public class Tab {
	private Entity entity;
	private Type type;
	private EntityView view;
	private boolean isActive;

	public enum Type { ENVIRONMENT, DASHBOARD, TASK_BOARD, TASK_TRAY, NEWS, ROLES, TRASH }

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public EntityView getView() {
		return view;
	}

	public void setView(EntityView view) {
		this.view = view;
	}

	public interface EntityView {

		public enum Type {
			NODE_VIEW, DASHBOARD_VIEW
		}

		public String getCode();
		public String getName();
		public Type getType();
		public String getLabel();
	}
}
