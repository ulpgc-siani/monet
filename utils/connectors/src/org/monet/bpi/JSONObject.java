package org.monet.bpi;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.commons.lang.NotImplementedException;

public class JSONObject {

	net.minidev.json.JSONObject wrapper;

	static public JSONObject parseFromUrl(String url) throws Exception {
		throw new NotImplementedException();
	}

	static public JSONObject parse(String content) {
		try {
			JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
			JSONObject jsonObject = new JSONObject();
			jsonObject.wrapper = (net.minidev.json.JSONObject) p.parse(content);
			return jsonObject;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public String getString(String key) {
		return (String) wrapper.get(key);
	}

	public Integer getInteger(String key) {
		return (Integer) wrapper.get(key);
	}

	public Number getNumber(String key) {
		return (Number) wrapper.get(key);
	}

	public JSONArray getArray(String key) {
		JSONArray array = new JSONArray();
		array.wrapper = (net.minidev.json.JSONArray) wrapper.get(key);
		return array;
	}

}
