package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.space.explorer.model.Filter;

import java.lang.reflect.Type;

public class FilterOptionSerializer extends AbstractSerializer<Filter.Option> implements JsonSerializer<Filter.Option> {

	public FilterOptionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(Filter.Option option) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(Filter.Option.class, this);
		return builder.create().toJsonTree(option, Filter.Option.class);
	}

	@Override
	public JsonElement serialize(Filter.Option option, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("value", option.getValue());
		result.addProperty("label", option.getLabel());

		return result;
	}

}
