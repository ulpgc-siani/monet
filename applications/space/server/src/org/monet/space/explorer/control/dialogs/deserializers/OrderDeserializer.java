package org.monet.space.explorer.control.dialogs.deserializers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.monet.space.explorer.model.Order;

public class OrderDeserializer extends AbstractDeserializer<Order, JsonObject> {

	public OrderDeserializer(Helper helper) {
		super(helper);
	}

	@Override
	public Order deserialize(String content) {
		return deserialize((JsonObject) new JsonParser().parse(content));
	}

	@Override
	public Order deserialize(final JsonObject object) {
		return new Order() {
			@Override
			public String getName() {
				return object.get("name").getAsString();
			}

			@Override
			public String getMode() {
				return object.get("mode").getAsString();
			}
		};
	}
}
