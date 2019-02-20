package org.monet.space.explorer.control.displays.serializers.definition.entity.views;

import com.google.gson.*;
import org.monet.metamodel.*;
import org.monet.metamodel.internal.Ref;
import org.monet.space.explorer.control.displays.serializers.AbstractSerializer;
import org.monet.space.explorer.control.displays.serializers.definition.RefSerializer;

import java.lang.reflect.Type;

public class ViewDefinitionSerializer<T extends ViewProperty> extends AbstractSerializer<T> implements JsonSerializer<T> {

	public ViewDefinitionSerializer(Helper helper) {
		super(helper);
	}

	public static void registerAdapters(GsonBuilder builder, Helper helper) {
		builder.registerTypeHierarchyAdapter(DesktopDefinition.ViewProperty.class, new DesktopViewDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(ContainerDefinition.ViewProperty.class, new ContainerViewDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(SetDefinition.SetViewProperty.class, new SetViewDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(FormDefinition.FormViewProperty.class, new FormViewDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(IndexDefinition.IndexViewProperty.class, new IndexViewDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(ProcessDefinition.ViewProperty.class, new ProcessViewDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(Ref.class, new RefSerializer(helper));
	}

	@Override
	public JsonElement serializeObject(T definition) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		ViewDefinitionSerializer.registerAdapters(builder, helper);
		return builder.create().toJsonTree(definition, definition.getClass());
	}

	@Override
	public JsonElement serialize(T definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("code", definition.getCode());
		result.addProperty("name", definition.getName());
		result.addProperty("type", getType(definition));

		return result;
	}

	private String getType(T definition) {
		if (definition instanceof DesktopDefinition.ViewProperty)
			return "desktop";

		if (definition instanceof ContainerDefinition.ViewProperty)
			return "container";

		if (definition instanceof SetDefinition.SetViewProperty)
			return "set";

		if (definition instanceof FormDefinition.FormViewProperty)
			return "form";

		if (definition instanceof IndexDefinition.IndexViewProperty)
			return "index";

		return "abstract";
	}
}
