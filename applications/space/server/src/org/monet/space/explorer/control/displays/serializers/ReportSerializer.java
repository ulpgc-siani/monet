package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.space.explorer.model.Report;

import java.lang.reflect.Type;
import java.util.Map;

public class ReportSerializer extends AbstractSerializer<Report> implements JsonSerializer<Report> {

	public ReportSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(Report report) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(Report.class, this);
		return builder.create().toJsonTree(report, Report.class);
	}

	@Override
	public JsonElement serialize(Report report, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.add("values", serializeValues(report));

		return result;
	}

	private JsonElement serializeValues(Report report) {
		JsonArray result = new JsonArray();

		for (Map.Entry<String, Integer> value : report.getValues().entrySet())
			result.add(serializeValue(value));

		return result;
	}

	private JsonElement serializeValue(Map.Entry<String, Integer> value) {
		JsonObject result = new JsonObject();
		result.addProperty("code", value.getKey());
		result.addProperty("value", value.getValue());
		return result;
	}

}
