package org.monet.metamodel;

import org.apache.commons.lang.NotImplementedException;

public abstract class Definition extends DefinitionBase {
	private boolean disabled;

	public static final String FULLY_QUALIFIED_NAME_SEPARATOR = "\\.";

	public String getLabelString() {
		return this._label.toString();
	}

	public String getLabelString(String codeLanguage) {
		return this._label.toString();
	}

	public String getDescriptionString() {
		return this._description.toString();
	}

	public String getDescriptionString(String codeLanguage) {
		return this._description.toString();
	}

	public String getFileName() {
		return this.getClass().getName().replace(".", "/") + ".class";
	}

	public String getRelativeFileName() {
		throw new NotImplementedException();
	}

	public String getAbsoluteFileName() {
		throw new NotImplementedException();
	}

	public void commit() {

	}

	public boolean isDisabled() {
		return this.disabled;
	}

	public void setDisabled(boolean value) {
		this.disabled = value;
	}

}
