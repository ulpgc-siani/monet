package org.monet.metamodel;

import net.minidev.json.JSONObject;
import org.monet.space.fms.core.model.Language;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.constants.Directory;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.model.DefinitionType;

public abstract class Definition extends DefinitionBase {
	private boolean disabled;

	public static final String FULLY_QUALIFIED_NAME_SEPARATOR = "\\.";

	public String getLabelString() {
		return Language.getInstance().getModelResource(this._label);
	}

	public String getLabelString(String codeLanguage) {
		return Language.getInstance().getModelResource(this._label, codeLanguage);
	}

	public String getDescriptionString() {
		return Language.getInstance().getModelResource(this._description);
	}

	public String getDescriptionString(String codeLanguage) {
		return Language.getInstance().getModelResource(this._description, codeLanguage);
	}

	public String getFileName() {
		return this.getClass().getName().replace(".", "/") + ".class";
	}

	public String getRelativeFileName() {
		return Directory.DEFINITIONS + Strings.BAR45 + this.getFileName();
	}

	public String getAbsoluteFileName() {
		return Configuration.getInstance().getBusinessModelClassesDir() + "/" + this.getFileName();
	}

	public JSONObject serializeToJSON() {
		JSONObject object = new JSONObject();

		object.put("code", this.getCode());
		object.put("label", this.getLabel());

		return object;
	}

	public void commit() {
	}

	public boolean isDisabled() {
		return this.disabled;
	}

	public void setDisabled(boolean value) {
		this.disabled = value;
	}

	public DefinitionType getType() {
		return null;
	}
}
