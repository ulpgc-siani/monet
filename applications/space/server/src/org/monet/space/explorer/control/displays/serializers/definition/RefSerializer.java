package org.monet.space.explorer.control.displays.serializers.definition;

import com.google.gson.*;
import org.monet.metamodel.internal.Ref;
import org.monet.space.explorer.control.displays.serializers.AbstractSerializer;

import java.lang.reflect.Type;

public class RefSerializer extends AbstractSerializer<Ref> implements JsonSerializer<Ref> {

	public RefSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(Ref ref) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(Ref.class, this);
		return builder.create().toJsonTree(ref);
	}

	@Override
	public JsonElement serialize(Ref ref, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("value", ref.getValue());

		if (ref.getDefinition() != null)
			result.addProperty("definition", helper.getDictionary().getDefinitionCode(ref.getDefinition()));

		return result;
	}

}
