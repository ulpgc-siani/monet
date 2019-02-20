package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.space.explorer.model.BusinessUnit;

import java.lang.reflect.Type;

public class BusinessUnitSerializer extends AbstractSerializer<BusinessUnit> implements JsonSerializer<BusinessUnit> {

	public BusinessUnitSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(BusinessUnit businessUnit) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(BusinessUnit.class, this);
		return builder.create().toJsonTree(businessUnit, BusinessUnit.class);
	}

	@Override
	public JsonElement serialize(BusinessUnit businessUnit, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("name", businessUnit.getName());
		result.addProperty("url", businessUnit.getUrl());
		result.addProperty("type", businessUnit.getType().toString());
		result.addProperty("active", businessUnit.isActive());
		result.addProperty("disabled", businessUnit.isDisabled());

		return result;
	}

}
