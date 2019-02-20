package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryDate;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

public class TaskOrder extends BaseObject {
	private String taskId;
	private String setupNodeId = null;
	private String roleId;
	private Role role;
	private Type type;
	private String partnerContext = null;
	private Chat chat;
	private int newMessagesCount;
	private Date createDate;
	private Date suggestedStartDate = null;
	private Date suggestedEndDate = null;
	private String comments;
	private boolean urgent = false;
	private boolean closed = false;

	public static final String ROLE = "role";
	public static final String CHAT = "chat";

	public enum Type {
		provider, customer, job
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

	public String getSetupNodeId() {
		return this.setupNodeId;
	}

	public void setSetupNodeId(String id) {
		this.setupNodeId = id;
	}

	public Chat getChat() {
		onLoad(this, TaskOrder.CHAT);
		return this.chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
		this.addLoadedAttribute(TaskOrder.CHAT);
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isUrgent() {
		return this.urgent;
	}

	public void setUrgent(boolean value) {
		this.urgent = value;
	}

	public boolean isClosed() {
		return this.closed;
	}

	public void setClosed(boolean value) {
		this.closed = value;
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

	public String getSuggestedStartDate(String format, String codeLanguage, TimeZone zone) {
		if (this.suggestedStartDate == null)
			return null;
		return LibraryDate.getDateAndTimeString(this.suggestedStartDate, codeLanguage, zone, format, true, Strings.BAR45);
	}

	public String getSuggestedStartDate(String format) {
		return this.getSuggestedStartDate(format, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getSuggestedStartDate() {
		return this.getSuggestedStartDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getInternalSuggestedStartDate() {
		return this.suggestedStartDate;
	}

	public void setSuggestedStartDate(Date suggestedStartDate) {
		this.suggestedStartDate = suggestedStartDate;
	}

	public String getSuggestedEndDate(String format, String codeLanguage, TimeZone zone) {
		if (this.suggestedEndDate == null)
			return null;
		return LibraryDate.getDateAndTimeString(this.suggestedEndDate, codeLanguage, zone, format, true, Strings.BAR45);
	}

	public String getSuggestedEndDate(String format) {
		return this.getSuggestedEndDate(format, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getSuggestedEndDate() {
		return this.getSuggestedEndDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getInternalSuggestedEndDate() {
		return this.suggestedEndDate;
	}

	public void setSuggestedEndDate(Date suggestedEndDate) {
		this.suggestedEndDate = suggestedEndDate;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public Role getRole() {
		if (this.roleId == null)
			return null;

		onLoad(this, TaskOrder.ROLE);

		if (!this.role.getId().equals(this.roleId)) {
			this.removeLoadedAttribute(TaskOrder.ROLE);
			onLoad(this, TaskOrder.ROLE);
		}

		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
		this.setRoleId(role != null ? role.getId() : null);
		this.addLoadedAttribute(TaskOrder.ROLE);
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setType(String type) {
		if (type.toString().equals("customer")) this.type = Type.customer;
		else if (type.toString().equals("provider")) this.type = Type.provider;
		else if (type.toString().equals("job")) this.type = Type.job;
	}

	public Type getType() {
		return this.type;
	}

	public String getPartnerContext() {
		return this.partnerContext;
	}

	public void setPartnerContext(String partnerContext) {
		this.partnerContext = partnerContext;
	}

	public void setNewMessagesCount(int newMessages) {
		this.newMessagesCount = newMessages;
	}

	public int getNewMessagesCount() {
		return this.newMessagesCount;
	}

	public boolean hasNewMessages() {
		return this.newMessagesCount == 0;
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		result.put("id", this.getId());
		result.put("idTask", this.getTaskId());
		result.put("idRole", this.getRoleId());
		result.put("code", this.getCode());
		result.put("type", this.getType());
		result.put("newMessagesCount", this.newMessagesCount);

		String label = this.partnerContext;
		if (label == null) {
			Role role = this.getRole();
            if (role != null)
			    label = role.getLabel();
			if (label == null)
				label = BusinessUnit.getInstance().getLabel();
		}

		result.put("label", label);
		result.put("comments", this.getComments());
		result.put("urgent", this.isUrgent());
		result.put("closed", this.isClosed());
		result.put("createDate", LibraryDate.getDateAndTimeString(this.getInternalCreateDate(), Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.INTERNAL, true, "/"));
		result.put("suggestedStartDate", LibraryDate.getDateAndTimeString(this.getInternalSuggestedStartDate(), Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.INTERNAL, true, "/"));
		result.put("suggestedEndDate", LibraryDate.getDateAndTimeString(this.getInternalSuggestedEndDate(), Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.INTERNAL, true, "/"));

		return result;
	}

}
