package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.space.explorer.model.Tab;
import org.monet.space.kernel.model.Entity;

import java.lang.reflect.Type;

public class TabSerializer extends AbstractSerializer<Tab> implements JsonSerializer<Tab> {

	public TabSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(Tab view) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(Entity.class, new EntitySerializer(helper));
		builder.registerTypeHierarchyAdapter(Tab.class, this);
		return builder.create().toJsonTree(view, Tab.class);
	}

	@Override
	public JsonElement serialize(Tab tab, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.add("entity", jsonSerializationContext.serialize(tab.getEntity(), Entity.class));
		result.add("view", serializeView(tab));
		result.addProperty("type", tab.getType().toString());
		result.addProperty("active", tab.isActive());

		return result;
	}

	public JsonElement serializeView(Tab tab) {
		Tab.EntityView view = tab.getView();

		if (view == null)
			return null;

		JsonObject result = new JsonObject();

		result.addProperty("code", view.getCode());
		result.addProperty("name", view.getName());
		result.addProperty("type", view.getType().toString());
		result.addProperty("label", view.getLabel());

		return result;
	}

}
