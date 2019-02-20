package org.monet.space.explorer.control.dialogs.deserializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDeserializer<T, D> extends ExplorerDeserializer implements Deserializer<T, D, JsonArray> {

	public AbstractDeserializer(Helper helper) {
		super(helper);
	}

	@Override
	public List<T> deserializeList(String content) {
		return deserializeList((JsonArray) new JsonParser().parse(content));
	}

	@Override
	public List<T> deserializeList(JsonArray content) {
		List<T> list = new ArrayList<>();

		for (int i = 0; i < content.size(); i++)
			list.add(deserialize((D)content.get(i)));

		return list;
	}

}
