package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class PeriodRange extends BaseObject {
	private String previousMember;
	private String currentMember;

	public PeriodRange() {
		this(null, null);
	}

	public PeriodRange(String previousMember, String currentMember) {
		this.previousMember = previousMember;
		this.currentMember = currentMember;
	}

	public String getPreviousMember() {
		return previousMember;
	}

	public String getCurrentMember() {
		return currentMember;
	}

	@Override
	public JSONObject toJson() {
		JSONObject result = new JSONObject();

		result.put("previous", this.getPreviousMember());
		result.put("current", this.getCurrentMember());

		return result;
	}

	public void fromJson(String content) throws ParseException {
		JSONObject jsonObject = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(content);
		if (jsonObject.containsKey("previous")) this.previousMember = (String) jsonObject.get("previous");
		if (jsonObject.containsKey("current")) this.currentMember = (String) jsonObject.get("current");
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}
}
