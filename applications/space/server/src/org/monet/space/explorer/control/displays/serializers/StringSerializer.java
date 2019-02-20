package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class StringSerializer extends AbstractSerializer<String> {

	public StringSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(String object) {
		return new JsonPrimitive(object);
	}
}
