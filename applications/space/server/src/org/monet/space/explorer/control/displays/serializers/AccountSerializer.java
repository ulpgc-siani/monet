package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.User;

import java.lang.reflect.Type;

public class AccountSerializer extends AbstractSerializer<Account> implements JsonSerializer<Account> {

	public AccountSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(Account account) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(Account.class, this);
		builder.registerTypeAdapter(User.class, new UserSerializer(helper));
		builder.registerTypeAdapter(Node.class, new NodeSerializer(helper));
		return builder.create().toJsonTree(account);
	}

	@Override
	public JsonElement serialize(Account account, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("id", account.getId());
		result.add("user", jsonSerializationContext.serialize(account.getUser(), account.getUser().getClass()));
		result.add("rootNode", jsonSerializationContext.serialize(account.getRootNode(), account.getRootNode().getClass()));

		return result;
	}

}
