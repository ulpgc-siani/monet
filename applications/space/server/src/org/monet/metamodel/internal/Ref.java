package org.monet.metamodel.internal;

public class Ref {

	private String definition;
	private String value;
	private String fullQualifiedName;

	public Ref(String value) {
		this.value = value;
	}

	public Ref(String value, String definition, String fullQualifiedName) {
		this.value = value;
		this.definition = definition;
		this.fullQualifiedName = fullQualifiedName;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFullQualifiedName() {
		return fullQualifiedName;
	}

	public void setFullQualifiedName(String fullQualifiedName) {
		this.fullQualifiedName = fullQualifiedName;
	}

}
