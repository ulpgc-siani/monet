package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.space.kernel.model.MonetLink;

import java.lang.reflect.Type;

public class MonetLinkSerializer extends AbstractSerializer<MonetLink> implements JsonSerializer<MonetLink> {

	public MonetLinkSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(MonetLink link) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(MonetLink.class, this);
		return builder.create().toJsonTree(link);
	}

	@Override
	public JsonElement serialize(MonetLink link, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("value", link.toString());
		result.addProperty("label", link.getLabel());

		return result;
	}

}
