package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.space.kernel.model.Fact;
import org.monet.space.kernel.model.MonetLink;

import java.lang.reflect.Type;

public class FactSerializer extends AbstractSerializer<Fact> implements JsonSerializer<Fact> {

	public FactSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(Fact fact) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(Fact.class, this);
		builder.registerTypeAdapter(MonetLink.class, new MonetLinkSerializer(helper));
		return builder.create().toJsonTree(fact, Fact.class);
	}

	@Override
	public JsonElement serialize(Fact fact, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("title", fact.getTitle());
		result.addProperty("subTitle", fact.getSubTitle());
		result.addProperty("user", fact.getUserId());
		result.add("links", serializeLinks(fact, jsonSerializationContext));
		result.addProperty("createDate", fact.getInternalCreateDate().getTime());

		return result;
	}

	private JsonArray serializeLinks(Fact fact, JsonSerializationContext jsonSerializationContext) {
		JsonArray result = new JsonArray();

		for (MonetLink link : fact.getLinks())
			result.add(jsonSerializationContext.serialize(link, link.getClass()));

		return result;
	}

}
