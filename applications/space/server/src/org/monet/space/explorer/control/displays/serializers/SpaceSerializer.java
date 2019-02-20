package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.monet.space.explorer.control.displays.serializers.definition.DictionarySerializer;
import org.monet.space.explorer.model.Space;
import org.monet.space.explorer.model.Tab;
import org.monet.space.kernel.model.*;

public class SpaceSerializer extends AbstractSerializer<Space> {

	public SpaceSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(Space space) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(Account.class, new AccountSerializer(helper));
		builder.registerTypeAdapter(Node.class, new EntitySerializer(helper));
		builder.registerTypeAdapter(Entity.class, new EntitySerializer(helper));
		builder.registerTypeAdapter(Tab.class, new TabSerializer(helper));
		builder.registerTypeAdapter(User.class, new UserSerializer(helper));
		builder.registerTypeAdapter(Dictionary.class, new DictionarySerializer(helper));
		return builder.create().toJsonTree(space);
	}

}
