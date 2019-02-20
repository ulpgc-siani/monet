package org.monet.space.kernel.model;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.jdom.Element;

public class FilterGroupList extends MonetList<FilterGroup> {
	private static final long serialVersionUID = 1L;

	public FilterGroupList() {
		super();
	}

	public FilterGroupList(FilterGroupList filterGroupList) {
		super(filterGroupList);
	}

	public void fromJson(String content) throws ParseException {
		JSONObject jsonObject = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(content);
		JSONArray jsonRows = (JSONArray) jsonObject.get("rows");

		for (Object row : jsonRows) {
			JSONObject jsonRow = (JSONObject) row;
			FilterGroup filterGroup = new FilterGroup();
			filterGroup.fromJson(jsonRow);
			this.add(filterGroup);
		}
	}

	@Override
	public void deserializeFromXML(Element object) {
	}

}