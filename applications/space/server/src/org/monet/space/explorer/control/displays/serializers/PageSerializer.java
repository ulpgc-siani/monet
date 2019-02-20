package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.space.kernel.model.Page;

import java.lang.reflect.Type;

public class PageSerializer extends AbstractSerializer<Page> implements JsonSerializer<Page> {

	public PageSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(Page page) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(Page.class, this);
		return builder.create().toJsonTree(page, Page.class);
	}

	@Override
	public JsonElement serialize(Page page, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("content", page.getContent());

		return result;
	}

}
