package org.monet.api.space.backservice.impl.model.workmap;

import org.monet.api.space.backservice.impl.model.MailBoxUri;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.util.Date;

@Root(name = "collaborator")
public class Contest {

	@Attribute(name = "local-mail-box", required = false)
	private MailBoxUri localMailBox;
	@Attribute(name = "task-mail-box", required = false)
	private MailBoxUri taskMailBox;
	@Attribute(name = "is-open")
	private boolean isOpen = false;
	@Attribute(name = "order-id", required = false)
	private String orderId = null;
	@Attribute(name = "failure-date", required = false)
	private Date failureDate = null;

	public MailBoxUri getLocalMailBox() {
		return localMailBox;
	}

	public void setLocalMailBox(MailBoxUri localMailBox) {
		this.localMailBox = localMailBox;
	}

	public MailBoxUri getTaskMailBox() {
		return taskMailBox;
	}

	public void setTaskMailBox(MailBoxUri taskMailBox) {
		this.taskMailBox = taskMailBox;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public boolean hasFailures() {
		return this.failureDate != null;
	}

	public Date getFailureDate() {
		return this.failureDate;
	}

	public void setFailureDate(Date date) {
		this.failureDate = date;
	}

	public void reset() {
		this.failureDate = null;
		this.orderId = null;
	}

}
