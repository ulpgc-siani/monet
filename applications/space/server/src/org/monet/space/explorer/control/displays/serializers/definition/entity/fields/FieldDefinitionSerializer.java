package org.monet.space.explorer.control.displays.serializers.definition.entity.fields;

import com.google.gson.*;
import org.monet.metamodel.*;
import org.monet.metamodel.internal.Ref;
import org.monet.space.explorer.control.displays.serializers.definition.PropertySerializer;
import org.monet.space.explorer.control.displays.serializers.definition.RefSerializer;

import java.lang.reflect.Type;

public class FieldDefinitionSerializer<T extends FieldProperty> extends PropertySerializer<T> {

	public FieldDefinitionSerializer(Helper helper) {
		super(helper);
	}

	public static void registerAdapters(GsonBuilder builder, Helper helper) {
		builder.registerTypeHierarchyAdapter(BooleanFieldProperty.class, new BooleanFieldDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(CheckFieldProperty.class, new CheckFieldDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(CompositeFieldProperty.class, new CompositeFieldDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(DateFieldProperty.class, new DateFieldDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(FileFieldProperty.class, new FileFieldDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(LinkFieldProperty.class, new LinkFieldDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(MemoFieldProperty.class, new MemoFieldDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(NodeFieldProperty.class, new NodeFieldDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(NumberFieldProperty.class, new NumberFieldDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(PictureFieldProperty.class, new PictureFieldDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(SelectFieldProperty.class, new SelectFieldDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(SerialFieldProperty.class, new SerialFieldDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(SummationFieldProperty.class, new SummationFieldDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(TextFieldProperty.class, new TextFieldDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(UriFieldProperty.class, new UriFieldDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(Ref.class, new RefSerializer(helper));
	}

	@Override
	public JsonElement serializeObject(T definition) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		FieldDefinitionSerializer.registerAdapters(builder, helper);
		return builder.create().toJsonTree(definition);
	}

	@Override
	public JsonElement serialize(T definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(definition, type, jsonSerializationContext);

		result.addProperty("label", helper.getLanguage().getModelResource(definition.getLabel()));
		result.addProperty("description", helper.getLanguage().getModelResource(definition.getDescription()));
		result.addProperty("type", definition.getType());

		if (definition.getTemplate() != null)
			result.addProperty("template", definition.getTemplate().toString());

		if (definition.isCollapsible())
			result.addProperty("collapsible", definition.isCollapsible());

		if (definition.isRequired())
			result.addProperty("required", definition.isRequired());

		if (definition.isReadonly())
			result.addProperty("readonly", definition.isReadonly());

		if (definition.isExtended())
			result.addProperty("extended", definition.isExtended());

		/* disabled isSuperField
		if (definition.isSuperfield())
			result.addProperty("superField", definition.isSuperfield());
		*/

		if (definition.isMultiple())
			result.addProperty("multiple", definition.isMultiple());

		if (definition.isStatic())
			result.addProperty("static", definition.isStatic());

		if (definition.isUnivocal())
			result.addProperty("univocal", definition.isUnivocal());

        if (definition.isMultiple() && ((MultipleableFieldProperty) definition).getBoundary() != null)
            result.add("boundary", serializeBoundary(((MultipleableFieldProperty) definition).getBoundary()));

		result.add("displays", serializeDisplayList(definition));

		return result;
	}

    private JsonObject serializeBoundary(MultipleableFieldProperty.BoundaryProperty boundary) {
        JsonObject object = new JsonObject();
        object.addProperty("min", boundary.getMin());
        object.addProperty("max", boundary.getMax());
        return object;
    }

    private JsonObject serializeDisplayList(T definition) {
		JsonArray result = new JsonArray();

		for (FieldPropertyBase.DisplayProperty displayDefinition : definition.getDisplayList()) {
			JsonObject display = new JsonObject();
			display.addProperty("message", helper.getLanguage().getModelResource(displayDefinition.getMessage()));
			display.addProperty("when", displayDefinition.getWhen().toString());
			result.add(display);
		}

		return toListObject(result);
	}

}
