package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.space.kernel.model.ClientOperation;

import java.lang.reflect.Type;

public class ClientOperationSerializer extends AbstractSerializer<ClientOperation> implements JsonSerializer<ClientOperation>  {

	public ClientOperationSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(ClientOperation operation) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(ClientOperation.class, this);
		return builder.create().toJsonTree(operation, operation.getClass());
	}

	@Override
	public JsonElement serialize(ClientOperation operation, Type type, JsonSerializationContext jsonSerializationContext) {

		if (operation == null)
			return null;

		return new JsonPrimitive(operation.toString());
	}

}
