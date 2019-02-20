package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryDate;

import java.util.Date;

public class Notification {
	private String id;
	private String userId;
	private String publicationId;
	private String label;
	private String icon;
	private String target;
	private Date createDate = new Date();
	private boolean read;

	public static final String WALL_MODIFIED_ID = "monet_wall_modified";

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public String getPublicationId() {
		return this.publicationId;
	}

	public void setPublicationId(String publicationId) {
		this.publicationId = publicationId;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTarget() {
		return target;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isRead() {
		return read;
	}

	public JSONObject toJson() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", this.id);
		jsonObject.put("userId", this.userId);
		jsonObject.put("publicationId", this.publicationId);
		jsonObject.put("label", this.label);
		jsonObject.put("icon", this.icon);
		jsonObject.put("target", this.target);
		jsonObject.put("createDate", LibraryDate.getDateAndTimeString(this.createDate, Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.INTERNAL, true, Strings.BAR45));
		jsonObject.put("read", this.read);
		return jsonObject;
	}

}
