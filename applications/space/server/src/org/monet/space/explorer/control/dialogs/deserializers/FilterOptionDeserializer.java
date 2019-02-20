package org.monet.space.explorer.control.dialogs.deserializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.monet.space.explorer.model.Filter;

public class FilterOptionDeserializer extends AbstractDeserializer<Filter.Option, JsonElement> {

	public FilterOptionDeserializer(Helper helper) {
		super(helper);
	}

	@Override
	public Filter.Option deserialize(String content) {
		return deserialize(new JsonParser().parse(content));
	}

	@Override
	public Filter.Option deserialize(JsonElement content) {
		final JsonObject element = (JsonObject)content;

		return new Filter.Option() {
			@Override
			public String getValue() {
				return element.get("value").getAsString();
			}

			@Override
			public String getLabel() {
				return element.get("label").getAsString();
			}
		};
	}
}
