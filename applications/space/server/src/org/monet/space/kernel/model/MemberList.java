package org.monet.space.kernel.model;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.jdom.Element;

public class MemberList extends MonetList<Member> {
	private static final long serialVersionUID = 1L;

	public void fromJson(String content) throws ParseException {
		JSONObject jsonObject = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(content);
		JSONArray jsonRows = (JSONArray) jsonObject.get("rows");

		for (Object row : jsonRows) {
			JSONObject jsonRow = (JSONObject) row;
			Member member = new Member();
			member.fromJson(jsonRow);
			this.add(member);
		}

		this.setTotalCount(Integer.valueOf((String) jsonObject.get("nrows")));
	}

	@Override
	public void deserializeFromXML(Element object) {
	}

}
