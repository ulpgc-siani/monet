package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.space.explorer.control.dialogs.constants.Operation;
import org.monet.space.explorer.model.TaskListIndexEntry;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.User;

import java.lang.reflect.Type;

public class TaskListIndexEntrySerializer extends IndexEntrySerializer<TaskListIndexEntry> {

	private static final String TIME_LINE_IMAGE_URL = Operation.LOAD_TASK_TIME_LINE + "/%s&random=%s";

	public TaskListIndexEntrySerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(TaskListIndexEntry entry) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(TaskListIndexEntry.class, this);
		builder.registerTypeAdapter(User.class, new UserSerializer(helper));
		return builder.create().toJsonTree(entry, TaskListIndexEntry.class);
	}

	@Override
	public JsonElement serialize(TaskListIndexEntry entry, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(entry, type, jsonSerializationContext);
		Task entity = entry.getEntity();

		result.addProperty("description", entity.getDescription());
		result.addProperty("createDate", entity.getInternalCreateDate().getTime());
		result.addProperty("updateDate", entity.getInternalUpdateDate().getTime());
		result.addProperty("messagesCount", entity.getNewMessagesCount());
		result.addProperty("timeLineImageUrl", helper.constructApiUrl(String.format(TIME_LINE_IMAGE_URL, entity.getId(), String.valueOf(Math.random()))));
		result.addProperty("urgent", entity.isUrgent());
		result.addProperty("state", entity.getState());
		result.addProperty("type", entity.getType().toString());
		result.add("owner", jsonSerializationContext.serialize(entity.getOwner(), entity.getOwner().getClass()));
		result.add("sender", jsonSerializationContext.serialize(entity.getSender(), entity.getSender().getClass()));

		return result;
	}

}
