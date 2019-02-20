package org.monet.space.kernel.model;

import com.google.gson.JsonPrimitive;
import net.minidev.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class ClientOperation extends BaseObject {
	private String name;
	private JSONObject data;

	public ClientOperation(String name, JSONObject data) {
		this.name = name;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		result.put("name", this.getName());
		result.put("data", this.getData());
		return result;
	}

	@Override
	public String toString() {
		String parameters = "";

		for (Object parameterValue : getData().values())
			parameters += (parameters.isEmpty()?"":",") + parameterValue;

		return getName() + "(" + parameters + ")";
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub

	}

}
