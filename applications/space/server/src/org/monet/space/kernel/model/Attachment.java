package org.monet.space.kernel.model;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;

public class Attachment extends BaseObject {
	private String sourceId;
	private String targetId;
	private String targetType;
	private String targetCode;
	private String data;
	private Date deleteDate;

	public Attachment() {
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String type) {
		this.targetType = type;
	}

	public String getTargetCode() {
		return targetCode;
	}

	public void setTargetCode(String code) {
		this.targetCode = code;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub

	}
}
