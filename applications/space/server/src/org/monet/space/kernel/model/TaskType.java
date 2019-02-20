package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;

public class TaskType {
	private String code;
	private String label;

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public JSONObject toJSON() {
		JSONObject result = new JSONObject();
		result.put("code", this.code);
		result.put("label", this.label);
		return result;
	}
}