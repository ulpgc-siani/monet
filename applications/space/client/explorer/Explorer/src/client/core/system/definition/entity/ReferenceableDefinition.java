package client.core.system.definition.entity;

public class ReferenceableDefinition implements client.core.model.definition.entity.ReferenceableDefinition {
	private String code;
	private String name;

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
}
