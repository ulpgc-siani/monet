package org.monet.space.kernel.model;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.HashMap;

public class Member extends BaseObject {
	private String key;
	private String link;
	private String value;
	private HashMap<String, String> extra = new HashMap<String, String>();

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void fromJson(JSONObject jsonObject) {
		this.key = (String) jsonObject.get("key");
		this.code = (String) jsonObject.get("code");
		this.link = (String) jsonObject.get("link");
		this.value = (String) jsonObject.get("value");

		this.extra.clear();
		JSONArray extra = (JSONArray) jsonObject.get("extra");
		for (int i = 0; i < extra.size(); i++) {
			JSONObject extraItem = (JSONObject) extra.get(i);
			this.extra.put((String) extraItem.get("key"), (String) extraItem.get("value"));
		}
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		result.put("code", this.getCode());
		result.put("value", this.getValue());
		result.put("link", this.getLink());
		return result;
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}

	public String getExtra(String key) {
		return extra.get(key);
	}

	public void setExtra(HashMap<String, String> extra) {
		this.extra = extra;
	}

}
