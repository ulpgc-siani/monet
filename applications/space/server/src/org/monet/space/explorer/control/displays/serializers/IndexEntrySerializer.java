package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.space.explorer.model.IndexEntry;
import org.monet.space.kernel.model.Entity;

import java.lang.reflect.Type;

public abstract class IndexEntrySerializer<T extends IndexEntry> extends AbstractSerializer<T> implements JsonSerializer<T> {

	public IndexEntrySerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(T entry, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();
		Entity entity = entry.getEntity();

		result.addProperty("label", entry.getLabel());
		result.add("entity", new EntitySerializer<>(helper).serializeObject(entity));

		return result;
}

	}
