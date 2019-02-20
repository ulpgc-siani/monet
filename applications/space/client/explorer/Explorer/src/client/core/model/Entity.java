package client.core.model;

import client.core.model.definition.entity.EntityDefinition;

public interface Entity<Definition extends EntityDefinition> extends Instance {

	enum Type {
		DESKTOP, COLLECTION, FORM, CONTAINER, CATALOG, DOCUMENT,
		TASK, ACTIVITY, SERVICE, JOB,
		BOOLEAN, CHECK, COMPOSITE, DATE, FILE, LINK, MEMO, NODE, NUMBER, PICTURE, SELECT, SERIAL, SUMMATION, TEXT, URI, MULTIPLE_TEXT, MULTIPLE_DATE, MULTIPLE_FILE, MULTIPLE_PICTURE, MULTIPLE_COMPOSITE, MULTIPLE_SELECT, MULTIPLE_NUMBER, MULTIPLE_MEMO, MULTIPLE_LINK, MULTIPLE_SERIAL, MULTIPLE_URI,
		USER, FEEDER,
		DEFAULT, GROUP,
		MEMBER, PARTNER,
		DASHBOARD, TASK_TRAY, TASK_BOARD, NEWS, TRASH, ROLES,
		NODE_VIEW, DASHBOARD_VIEW;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

		public static Type fromString(String type) {
			return Type.valueOf(type.toUpperCase());
		}
	}

	String getId();
	Key getKey();
	Type getType();
	String getLabel();
	void setLabel(String label);
	String getDescription();

	<T extends Entity> T getOwner();
	void setOwner(Entity owner);

	List<Entity> getAncestors();
	void setAncestors(List<Entity> ancestors);

	Definition getDefinition();
	void setDefinition(Definition definition);

	boolean equals(Object obj);

}
