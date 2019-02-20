package org.monet.space.kernel.model;

import org.monet.metamodel.Definition;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;

public class Service extends BaseObject {

	public static final String PERMISSIONS = "permissions";

	private Date createdDate;
	private Definition definition;
	private String taskId;
	private String localMailBox;
	private MailBoxUri replyToMailBox;
	private String remoteUnitLabel;

	public void setReplyToMailBox(MailBoxUri replyToMailBox) {
		this.replyToMailBox = replyToMailBox;
	}

	public MailBoxUri getReplyToMailBox() {
		return replyToMailBox;
	}

	public void setCreateDate(Date date) {
		this.createdDate = date;
	}

	public Date getCreateDate() {
		return this.createdDate;
	}

	public void setDefinition(Definition definition) {
		this.definition = definition;
	}

	public Definition getDefinition() {
		return this.definition;
	}

	public void setTaskId(String id) {
		this.taskId = id;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setLocalMailBox(String localMailBox) {
		this.localMailBox = localMailBox;
	}

	public String getLocalMailBox() {
		return this.localMailBox;
	}

	public String getRemoteUnitLabel() {
		return remoteUnitLabel;
	}

	public void setRemoteUnitLabel(String remoteUnitLabel) {
		this.remoteUnitLabel = remoteUnitLabel;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}

}
