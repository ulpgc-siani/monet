package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryDate;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

public class Report extends BaseObject {
	private String idCube;
	private String label;
	private String description;
	private String data;
	private Date createDate;
	private Date updateDate;
	private boolean isValid;
	private String content;

	public Report() {
		super();
		this.id = "";
		this.idCube = "";
		this.label = "";
		this.description = "";
		this.data = "";
		this.createDate = null;
		this.updateDate = null;
		this.isValid = false;
		this.content = "";
	}

	public String getIdCube() {
		return this.idCube;
	}

	public void setIdCube(String idCube) {
		this.idCube = idCube;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Date getInternalCreateDate() {
		return this.createDate;
	}

	public String getCreateDate(String format, String codeLanguage, TimeZone zone) {
		onLoad(this, Task.PROPERTIES);
		if (this.createDate == null)
			return null;
		return LibraryDate.getDateAndTimeString(this.createDate, codeLanguage, zone, format, true, Strings.BAR45);
	}

	public String getCreateDate(String format) {
		return this.getCreateDate(format, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getCreateDate() {
		return this.getCreateDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public boolean isValid() {
		return this.isValid;
	}

	public void setIsValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}

	public JSONObject toJson() {
		Boolean isPartialLoading = this.isPartialLoading();
		JSONObject result = new JSONObject();

		if (isPartialLoading)
			this.disablePartialLoading();

		result.put("id", this.getId());
		result.put("idcube", this.getIdCube());
		result.put("label", this.getLabel());
		result.put("description", this.getDescription());
		result.put("data", this.getData());
		result.put("createDate", this.getCreateDate());
		result.put("updateDate", this.getCreateDate());
		result.put("isValid", this.isValid());
		result.put("content", this.getContent());

		if (isPartialLoading)
			this.enablePartialLoading();

		return result;
	}

}
