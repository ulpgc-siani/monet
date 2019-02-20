package org.monet.space.explorer.control.displays;

import com.google.gson.JsonElement;
import org.monet.space.explorer.control.displays.serializers.JsonElementSerializer;
import org.monet.space.explorer.control.displays.serializers.Serializer;

public class JsonElementDisplay extends HttpDisplay<JsonElement> {
	@Override
	protected Serializer getSerializer(JsonElement object) {
		return new JsonElementSerializer(createSerializerHelper());
	}
}
