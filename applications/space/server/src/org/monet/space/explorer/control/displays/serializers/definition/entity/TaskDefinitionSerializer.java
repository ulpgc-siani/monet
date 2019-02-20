package org.monet.space.explorer.control.displays.serializers.definition.entity;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.ActivityDefinition;
import org.monet.metamodel.JobDefinition;
import org.monet.metamodel.ServiceDefinition;
import org.monet.metamodel.TaskDefinition;
import org.monet.space.explorer.control.displays.serializers.definition.DefinitionSerializer;
import org.monet.space.explorer.control.displays.serializers.definition.entity.views.ViewDefinitionSerializer;

import java.lang.reflect.Type;

public class TaskDefinitionSerializer<T extends TaskDefinition> extends DefinitionSerializer<T> {

	public TaskDefinitionSerializer(Helper helper) {
		super(helper);
	}

	public static void registerAdapters(GsonBuilder builder, Helper helper) {
		builder.registerTypeHierarchyAdapter(ActivityDefinition.class, new ActivityDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(ServiceDefinition.class, new ServiceDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(JobDefinition.class, new JobDefinitionSerializer(helper));
		NodeDefinitionSerializer.registerAdapters(builder, helper);
		ViewDefinitionSerializer.registerAdapters(builder, helper);
	}

	@Override
	public JsonElement serializeObject(T definition) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		TaskDefinitionSerializer.registerAdapters(builder, helper);
		return builder.create().toJsonTree(definition);
	}

}
