package client.core.system;

public class Key implements client.core.model.Key {
	private String code;
	private String name;

	public Key() {
		this(null, null);
	}

	public Key(String code) {
		this(code, null);
	}

	public Key(String code, String name) {
		this.code = code;
		this.name = name;
	}

	@Override
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof String)
			return (getName() != null && getName().equals(obj)) || (getCode() != null && getCode().equals(obj));

		if (!(obj instanceof client.core.model.Key)) return false;

		client.core.model.Key obj1 = (client.core.model.Key) obj;

		if (obj1.getCode() != null && getCode() != null)
			return obj1.getCode().equals(getCode());

		return obj1.getName() != null && getName() != null && obj1.getName().equals(getName());

	}
}
