package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import org.monet.metamodel.DatastoreDefinitionBase.DimensionProperty;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class Dimension extends BaseObject {
	protected DimensionProperty definition;
	protected Language language;

	public Dimension(String code) {
		super();
		this.setCode(code);
		this.language = Language.getInstance();
	}

	public String getLabel() {
		return this.language.getModelResource(this.definition.getLabel());
	}

	public DimensionProperty getDefinition() {
		return Dictionary.getInstance().getDimensionDefinition(this.getCode());
	}

	@Override
	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		result.put("code", this.getCode());
		result.put("name", this.getName());
		result.put("label", this.getLabel());
		return result;
	}

	@Override
	public void fromJson(String content) {
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "dimension");

		serializer.attribute("", "code", this.code);
		serializer.attribute("", "name", this.name);

		serializer.endTag("", "dimension");
	}

}
