package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;

public class Range extends BaseObject {
	private Date minDate;
	private Date maxDate;

	public Range() {
		this(null, null);
	}

	public Range(Date minDate, Date maxDate) {
		this.minDate = minDate;
		this.maxDate = maxDate;
	}

	public Date getMinDate() {
		return minDate;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	@Override
	public JSONObject toJson() {
		JSONObject result = new JSONObject();

		result.put("min", this.getMinDate().getTime());
		result.put("max", this.getMaxDate().getTime());

		return result;
	}

	public void fromJson(String content) throws ParseException {
		JSONObject jsonObject = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(content);
		this.minDate = new Date((Long.valueOf(jsonObject.get("min").toString())));
		this.maxDate = new Date((Long.valueOf(jsonObject.get("max").toString())));
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}
}
