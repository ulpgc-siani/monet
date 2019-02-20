package org.monet.space.explorer.control.displays.serializers.definition.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.*;
import org.monet.metamodel.internal.Ref;

import java.lang.reflect.Type;

public class ActivityDefinitionSerializer extends TaskDefinitionSerializer<ActivityDefinition> {

	public ActivityDefinitionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(ActivityDefinition definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type, jsonSerializationContext);

		result.add("providers", serializeProviders(definition, jsonSerializationContext));
		result.add("places", serializePlaces(definition, jsonSerializationContext));
		result.add("views", serializeViews(definition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeProviders(ActivityDefinition definition, JsonSerializationContext jsonSerializationContext) {
		JsonArray providers = new JsonArray();

		for (TaskProviderProperty providerDefinition : definition.getTaskProviderPropertyList())
			providers.add(serializeProvider(providerDefinition, jsonSerializationContext));

		return toListObject(providers);
	}

	private JsonElement serializeProvider(TaskProviderProperty providerDefinition, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("code", providerDefinition.getCode());
		result.addProperty("name", providerDefinition.getName());
		result.add("role", jsonSerializationContext.serialize(providerDefinition.getRole(), providerDefinition.getRole().getClass()));

		return result;
	}

	private JsonElement serializePlaces(ActivityDefinition definition, JsonSerializationContext jsonSerializationContext) {
		JsonArray places = new JsonArray();

		for (ActivityDefinition.ActivityPlaceProperty placeDefinition : definition.getPlaceList())
			places.add(serializePlace(placeDefinition, jsonSerializationContext));

		return toListObject(places);
	}

	private JsonElement serializePlace(ActivityDefinition.ActivityPlaceProperty placeDefinition, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("code", placeDefinition.getCode());
		result.addProperty("name", placeDefinition.getName());
		result.add("action", serializeAction(placeDefinition, jsonSerializationContext));

		return result;
	}

	private JsonObject serializeAction(ActivityDefinition.ActivityPlaceProperty placeDefinition, JsonSerializationContext jsonSerializationContext) {
		if (placeDefinition.getDelegationActionProperty() != null)
			return serializeDelegationAction(placeDefinition.getDelegationActionProperty(), jsonSerializationContext);
		if (placeDefinition.getLineActionProperty() != null)
			return serializeLineAction(placeDefinition.getLineActionProperty(), jsonSerializationContext);
		if (placeDefinition.getEditionActionProperty() != null)
			return serializeEditionAction(placeDefinition.getEditionActionProperty());
		if (placeDefinition.getWaitActionProperty() != null)
			return serializeWaitAction(placeDefinition.getWaitActionProperty());

		return null;
	}

	private JsonObject serializeDelegationAction(DelegationActionProperty delegationActionDefinition, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("type", "delegation");
		result.addProperty("label", helper.getLanguage().getModelResource(delegationActionDefinition.getLabel()));
		result.add("provider", jsonSerializationContext.serialize(delegationActionDefinition.getProvider(), delegationActionDefinition.getProvider().getClass()));

		return result;
	}

	private JsonObject serializeLineAction(LineActionProperty lineActionDefinition, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("type", "line");
		result.addProperty("label", helper.getLanguage().getModelResource(lineActionDefinition.getLabel()));
		result.add("timeout", serializeLineActionTimeout(lineActionDefinition, jsonSerializationContext));

		JsonArray stops = new JsonArray();
		for (LineActionPropertyBase.LineStopProperty lineStopDefinition : lineActionDefinition.getStopList()) {
			JsonObject stop = new JsonObject();
			stop.addProperty("code", lineStopDefinition.getCode());
			stop.addProperty("label", helper.getLanguage().getModelResource(lineStopDefinition.getLabel()));
            stops.add(stop);
		}
		result.add("stop", stops);

		return result;
	}

	private JsonElement serializeLineActionTimeout(LineActionProperty lineActionDefinition, JsonSerializationContext jsonSerializationContext) {

		if (lineActionDefinition.getTimeout() == null)
			return null;

		Ref take = lineActionDefinition.getTimeout().getTake();

		JsonObject result = new JsonObject();
		result.add("take", jsonSerializationContext.serialize(take, take.getClass()));

		return result;
	}

	private JsonObject serializeEditionAction(EditionActionProperty editionActionDefinition) {
		JsonObject result = new JsonObject();

		result.addProperty("type", "edition");
		result.addProperty("label", helper.getLanguage().getModelResource(editionActionDefinition.getLabel()));

		return result;
	}

	private JsonObject serializeWaitAction(WaitActionProperty waitActionDefinition) {
		JsonObject result = new JsonObject();

		result.addProperty("type", "wait");
		result.addProperty("label", helper.getLanguage().getModelResource(waitActionDefinition.getLabel()));

		return result;
	}

	private JsonElement serializeViews(ActivityDefinition definition, JsonSerializationContext jsonSerializationContext) {
		JsonArray views = new JsonArray();

		for (ProcessDefinitionBase.ViewProperty viewDefinition : definition.getViewList())
			views.add(jsonSerializationContext.serialize(viewDefinition, viewDefinition.getClass()));

		return toListObject(views);
	}

}
