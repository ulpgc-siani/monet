package client.core.system;

public class BusinessUnit extends Entity implements client.core.model.BusinessUnit {
	private String name;
	private String url;
	private boolean active;
	private boolean disabled;

	public BusinessUnit() {
	}

	public BusinessUnit(String name, String label, Type type, String url) {
		super(null, label);
		this.name = name;
		this.type = type;
		this.url = url;
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.BusinessUnit.CLASS_NAME;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	@Override
	public boolean isMember() {
		return getType() == Type.MEMBER;
	}

	@Override
	public boolean isPartner() {
		return getType() == Type.PARTNER;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof String)
			return getName().equals(object);

		if (!(object instanceof BusinessUnit))
			return false;

		return getName().equals(((BusinessUnit) object).getName());
	}
}
