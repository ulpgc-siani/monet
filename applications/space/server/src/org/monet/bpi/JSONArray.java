package org.monet.bpi;

public class JSONArray extends JSONObject {

	net.minidev.json.JSONArray wrapper;

	public JSONObject get(int i) {
		Object value = wrapper.get(i);
		if (value instanceof net.minidev.json.JSONArray) {
			JSONArray array = new JSONArray();
			array.wrapper = (net.minidev.json.JSONArray) value;
			return array;
		} else {
			JSONObject object = new JSONObject();
			object.wrapper = (net.minidev.json.JSONObject) value;
			return object;
		}
	}

	public int getCount() {
		return wrapper.size();
	}

}
