package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.space.kernel.model.Role;

import java.lang.reflect.Type;

public class RoleSerializer extends AbstractSerializer<Role> implements JsonSerializer<Role> {

	public RoleSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(Role role) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(Role.class, this);
		return builder.create().toJsonTree(role, Role.class);
	}

	@Override
	public JsonElement serialize(Role role, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("id", role.getId());
		result.addProperty("label", role.getLabel());
		result.addProperty("type", role.getType().toString());

		return result;
	}

}
