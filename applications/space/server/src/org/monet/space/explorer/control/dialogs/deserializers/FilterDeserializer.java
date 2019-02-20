package org.monet.space.explorer.control.dialogs.deserializers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.monet.space.explorer.model.Filter;

import java.util.List;

public class FilterDeserializer extends AbstractDeserializer<Filter, JsonObject> {

	public FilterDeserializer(Helper helper) {
		super(helper);
	}

	@Override
	public Filter deserialize(String content) {
		return deserialize((JsonObject) new JsonParser().parse(content));
	}

	@Override
	public Filter deserialize(final JsonObject object) {
		return new Filter() {
			@Override
			public String getName() {
				return object.get("name").getAsString();
			}

			@Override
			public List<Filter.Option> getOptions() {
				return new FilterOptionDeserializer(helper).deserializeList(object.getAsJsonArray("options"));
			}
		};
	}
}
