package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.JsonElement;

public class JsonElementSerializer extends AbstractSerializer<JsonElement> {

	public JsonElementSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(JsonElement object) {
		return object;
	}
}
