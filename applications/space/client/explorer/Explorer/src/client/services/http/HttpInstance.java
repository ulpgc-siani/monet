package client.services.http;

import client.core.model.List;
import client.core.system.MonetList;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.query.client.js.JsMap;
import cosmos.types.Date;

public class HttpInstance extends JavaScriptObject {

	protected HttpInstance() {
	}

	public static HttpInstance createInstance() {
		return (HttpInstance)JavaScriptObject.createObject();
	}

	public static HttpList createList() {
		HttpList result = (HttpList)JavaScriptObject.createObject();
		result.set("count", 0);
		result.set("items", JavaScriptObject.createArray());
		return result;
	}

	public static HttpList createList(JsArray items) {
		HttpList result = (HttpList)JavaScriptObject.createObject();
		result.set("count", items.length());
		result.set("items", items);
		return result;
	}

	public final native String getString(String key) /*-{
		return this[key] != null ? "" + this[key] : "";
	}-*/;

	public final int getInt(String key) {
		try {
			return Integer.parseInt(getString(key));
		}
		catch (NumberFormatException exception) {
			return -1;
		}
	}

	public final long getLong(String key) {
        if (getString(key).isEmpty())
            return -1;
		return Long.parseLong(getString(key));
	}

	public final Double getDouble(String key) {
		return Double.parseDouble(getString(key));
	}

	public final boolean getBoolean(String key) {
		String value = getString(key);
		return !value.isEmpty() && Boolean.parseBoolean(value);
	}

	public final Date getDate(String key) {
		return getString(key).isEmpty() ? null : new Date(getLong(key));
	}

	public final native <T extends HttpInstance> JsArray<T> getArray(String key) /*-{
		if (this[key] == null)
			this[key] = [];

		return this[key];
	}-*/;

	public final native HttpList getList(String key) /*-{
		if (this[key] == null)
			this[key] = { count: 0, items: [] };

		return this[key];
	}-*/;

	public final native <T> JsMap<String, T> getMap(String key) /*-{
		if (this[key] == null)
			this[key] = {};

		return this[key];
	}-*/;

	public final native Object getMapObject(String map, String key) /*-{
		if (this[map] == null)
			this[map] = {};

		return this[map][key];
	}-*/;

	public final native HttpInstance getMapInstance(String map, String key) /*-{
		if (this[map] == null)
			this[map] = {};

		return this[map][key];
	}-*/;

	public final native HttpInstance getObject(String key) /*-{
		return this[key];
	}-*/;

	public final native boolean isString(String key) /*-{
		var value = this[key];
		return (typeof value == "string") || (value instanceof String);
	}-*/;

	public final native String asString() /*-{
		return this;
	}-*/;

	public final native void setString(String key, String value) /*-{
		this[key] = value;
	}-*/;

	public final native void set(String key, Object value) /*-{
		this[key] = value;
	}-*/;

	public static List<String> toList(JsArray items) {
		List result = new MonetList<>();

		if (items == null)
			return result;

		for (int i = 0; i < items.length(); i++)
			result.add(items.get(i));

		return result;
	}

	public final native boolean isItemOfTypeString(String arrayName, int pos) /*-{
		if (this[arrayName] == null)
			return false;

		var value = this[arrayName][pos];
		return (typeof value == "string") || (value instanceof String);
	}-*/;

	public final native String getItemOfTypeString(String arrayName, int pos) /*-{
		if (this[arrayName] == null)
			return false;

		return this[arrayName][pos];
	}-*/;

	public final HttpInstance getItemOfTypeObject(String arrayName, int pos) {
		return getArray(arrayName).get(pos);
	}

	public final native void setArrayValue(HttpInstance instance, String key, int i) /*-{
		this[key] = instance[key][i];
	}-*/;

	public final native boolean isList() /*-{
		return this["totalCount"] != null && this["items"] != null;
	}-*/;

}
