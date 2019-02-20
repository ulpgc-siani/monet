package org.monet.space.kernel.model;

public class DatastoreTransaction {
	private int id;
	private final String code;
	private final Type type;
	private BaseObject item;

	public enum Type {
		FACT, DIMENSION_COMPONENT;

		public static Type fromString(String type) {
			return Type.valueOf(type.toUpperCase());
		}
	}

	public DatastoreTransaction(int id, Type type, String code, BaseObject item) {
		this.id = id;
		this.type = type;
		this.code = code;
		this.item = item;
	}

	public String getCode() {
		return code;
	}

	public boolean isFact() {
		return type == Type.FACT;
	}

	public boolean isDimensionComponent() {
		return type == Type.DIMENSION_COMPONENT;
	}

	public int getId() {
		return id;
	}

	public <T extends BaseObject> T getItem() {
		return (T) item;
	}

}
