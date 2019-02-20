package client.core.system.definition;

public class Ref implements client.core.model.definition.Ref {
	private String definition;
	private String value;

	public Ref() {
	}

	public Ref(String value) {
		this(null, value);
	}

	public Ref(String definition, String value) {
		this.definition = definition;
		this.value = value;
	}

	@Override
	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	@Override
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
