package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.space.kernel.model.User;
import org.monet.space.kernel.model.UserInfo;

import java.lang.reflect.Type;

public class UserSerializer extends AbstractSerializer<User> implements JsonSerializer<User> {

	public UserSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(User user) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(User.class, this);
		return builder.create().toJsonTree(user);
	}

	@Override
	public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
		UserInfo info = user.getInfo();
		JsonObject result = new JsonObject();

		result.addProperty("id", user.getId());
		result.addProperty("fullName", info.getFullname());
		result.addProperty("email", info.getEmail());
		result.addProperty("photo", info.getPhoto());

		return result;
	}
}
