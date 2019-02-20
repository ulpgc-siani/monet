package org.monet.space.explorer.control.displays.serializers.definition.entity;

import com.google.gson.*;
import org.monet.metamodel.DocumentDefinition;

public class DocumentDefinitionSerializer extends NodeDefinitionSerializer<DocumentDefinition> {

	public DocumentDefinitionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(DocumentDefinition definition) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(DocumentDefinition.class, this);
		return builder.create().toJsonTree(definition);
	}

}
