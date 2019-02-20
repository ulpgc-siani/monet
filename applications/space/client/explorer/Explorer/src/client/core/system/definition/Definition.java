package client.core.system.definition;

import client.core.model.Instance;

public class Definition implements client.core.model.definition.Definition {
	private String code;
	private String name;
	private String label;
	private String description;

	public Definition() {
	}

	public Definition(String code, String name, String label, String description) {
		this.code = code;
		this.name = name;
		this.label = label;
		this.description = description;
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
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Instance.ClassName getClassName() {
		return client.core.model.definition.Definition.CLASS_NAME;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
