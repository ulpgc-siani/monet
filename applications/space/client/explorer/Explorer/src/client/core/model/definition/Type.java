package client.core.model.definition;

public class Type {
	private String code;
	private String label;

	public Type(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public String getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}

}
