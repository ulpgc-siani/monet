package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.space.kernel.model.Notification;

import java.lang.reflect.Type;

public class NotificationSerializer extends AbstractSerializer<Notification> implements JsonSerializer<Notification> {

	public NotificationSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(Notification notification) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(Notification.class, this);
		return builder.create().toJsonTree(notification, notification.getClass());
	}

	@Override
	public JsonElement serialize(Notification notification, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("id", notification.getId());
		result.addProperty("userId", notification.getUserId());
		result.addProperty("publicationId", notification.getPublicationId());
		result.addProperty("label", notification.getLabel());
		result.addProperty("icon", notification.getIcon());
		result.addProperty("target", notification.getTarget());
		result.addProperty("createDate", notification.getCreateDate().getTime());

		if (notification.isRead())
			result.addProperty("read", notification.isRead());

		return result;
	}

}
