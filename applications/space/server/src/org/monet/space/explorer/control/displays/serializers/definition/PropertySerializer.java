package org.monet.space.explorer.control.displays.serializers.definition;

import com.google.gson.*;
import org.monet.metamodel.AttributeProperty;
import org.monet.metamodel.ReferenceableProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.space.explorer.control.displays.serializers.AbstractSerializer;
import org.monet.space.explorer.control.displays.serializers.definition.entity.AttributeDefinitionSerializer;

import java.lang.reflect.Type;

public class PropertySerializer<T extends ReferenceableProperty> extends AbstractSerializer<T> implements JsonSerializer<T> {

	public PropertySerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(T definition) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeHierarchyAdapter(ReferenceableProperty.class, this);
		builder.registerTypeHierarchyAdapter(AttributeProperty.class, new AttributeDefinitionSerializer(helper));
		return builder.create().toJsonTree(definition);
	}

	@Override
	public JsonElement serialize(T definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("code", definition.getCode());
		result.addProperty("name", definition.getName());

		return result;
	}

	protected JsonElement serializeObject(Object element, JsonSerializationContext jsonSerializationContext) {

		if (element instanceof String)
			return new JsonPrimitive((String)element);

		if (element instanceof Ref)
			return jsonSerializationContext.serialize(element, Ref.class);

		return null;
	}
}
