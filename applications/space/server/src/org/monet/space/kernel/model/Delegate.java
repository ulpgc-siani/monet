package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import org.monet.space.kernel.constants.LabelCode;
import org.monet.space.office.core.model.Language;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class Delegate extends BaseObject {
	private String url;

	public Delegate(String id, String code, String name, String url) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		String label = this.getName();

		result.put("id", this.getName());

		if (label == null || label.trim().isEmpty())
			label = Language.getInstance().getLabel(LabelCode.NO_LABEL);

		result.put("id", this.id);
		result.put("code", this.code);
		result.put("label", label);
		result.put("url", this.url);

		return result;
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
	}

}
