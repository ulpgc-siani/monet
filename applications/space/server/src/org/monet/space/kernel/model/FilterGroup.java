package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;

public class FilterGroup extends BaseObject {
	private String userId;
	private String cubeId;
	private String label;
	private FilterList filterList;
	private Date createDate;

	public FilterGroup() {
		this.label = "";
		this.userId = "";
		this.cubeId = "";
		this.filterList = new FilterList();
		this.createDate = new Date();
	}

	public FilterGroup(String id, String userId, String label, FilterList filterList, Date createDate) {
		this.id = id;
		this.userId = userId;
		this.label = label;
		this.filterList = filterList;
		this.createDate = createDate;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCubeId() {
		return this.cubeId;
	}

	public void setCubeId(String cubeId) {
		this.cubeId = cubeId;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public FilterList getFilterList() {
		return this.filterList;
	}

	public void setFilterList(FilterList filterList) {
		this.filterList = filterList;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		result.put("id", this.getId());
		result.put("label", this.getLabel());
		result.put("filterList", this.getFilterList().toJson());
		return result;
	}

	@Override
	public void fromJson(String content) throws ParseException {
		JSONObject jsonObject = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(content);

		this.setId((String) jsonObject.get("id"));
		this.setLabel((String) jsonObject.get("label"));
		this.getFilterList().fromJson((String) jsonObject.get("filterList"));
	}

	public void fromJson(JSONObject jsonObject) throws ParseException {
		this.setId((String) jsonObject.get("id"));
		this.setLabel((String) jsonObject.get("label"));
		this.getFilterList().fromJson((String) jsonObject.get("filterList"));
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}

}
