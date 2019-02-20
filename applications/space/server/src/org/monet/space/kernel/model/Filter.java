package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class Filter extends BaseObject {
	private String key;
	private String operator = Operator.EQUAL;
	private String value;

	public class Operator {
		public static final String EQUAL = "=";
		public static final String LESS = "<";
		public static final String LESS_OR_EQUAL = "<=";
		public static final String GREAT = ">";
		public static final String GREAT_OR_EQUAL = ">=";
	}

	public Filter() {
	}

	public Filter(String key, String operator, String value) {
		this.setKey(key);
		this.setOperator(operator);
		this.setValue(value);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		result.put("key", this.getKey());
		result.put("operator", this.getOperator());
		result.put("value", this.getValue());
		return result;
	}

	public void fromJson(JSONObject jsonObject) {
		this.setKey((String) jsonObject.get("key"));
		this.setValue((String) jsonObject.get("value"));
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub

	}

}
