package org.monet.space.kernel.model;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.jdom.Element;

import java.util.HashMap;
import java.util.HashSet;

public class FilterList extends MonetList<Filter> {
	private static final long serialVersionUID = 1L;
	private HashMap<String, HashSet<String>> itemsValuesMap;

	public FilterList() {
		super();
		this.itemsValuesMap = new HashMap<String, HashSet<String>>();
	}

	public FilterList(FilterList filterList) {
		this();
		for (Filter item : filterList)
			this.add(item);
	}

	public void fromJson(String content) throws ParseException {
		JSONObject jsonObject = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(content);
		JSONArray jsonRows = (JSONArray) jsonObject.get("rows");

		this.clear();
		for (Object row : jsonRows) {
			JSONObject jsonRow = (JSONObject) row;
			Filter filter = new Filter();
			filter.fromJson(jsonRow);
			this.add(filter);
		}
	}

	@Override
	public void deserializeFromXML(Element object) {
	}

	@Override
	public boolean add(Filter filter) {
		super.add(filter);

		if (!this.itemsValuesMap.containsKey(filter.getKey()))
			this.itemsValuesMap.put(filter.getKey(), new HashSet<String>());

		HashSet<String> itemValuesSet = this.itemsValuesMap.get(filter.getKey());
		itemValuesSet.add(filter.getValue());

		return true;
	}

	public boolean containsKey(String key) {
		return this.itemsValuesMap.containsKey(key);
	}

	public boolean containsValueWithKey(String key, String value) {

		if (!this.containsKey(key))
			return false;

		HashSet<String> itemValuesSet = this.itemsValuesMap.get(key);
		return itemValuesSet.contains(value);
	}
}