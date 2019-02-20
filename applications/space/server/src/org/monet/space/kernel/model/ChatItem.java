package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryDate;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

public class ChatItem extends BaseObject {
	private String taskId;
	private String orderId;
	private String message;
	private Type type;
	private Date createDate = new Date();
	private boolean sent;

	public enum Type {
		in, out
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setType(String type) {
		if (type.toString().equals("in")) this.type = Type.in;
		else if (type.toString().equals("out")) this.type = Type.out;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public String getCreateDate(String format, String codeLanguage, TimeZone zone) {
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

	public Date getInternalCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		result.put("id", this.getId());
		result.put("date", this.getCreateDate());
		result.put("message", this.getMessage());
		result.put("type", this.getType());
		result.put("sent", this.isSent());
		return result;
	}

}
