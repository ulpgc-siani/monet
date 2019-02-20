package org.monet.space.kernel.machines.ttm.model;

import org.monet.space.kernel.model.MailBoxUri;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.util.Date;

@Root(name = "provider")
public class Provider {

	@Attribute(name = "local-mail-box", required = false)
	private MailBoxUri localMailBox;
	@Attribute(name = "service-mail-box", required = false)
	private MailBoxUri serviceMailBox;
	@Attribute(name = "is-open")
	private boolean isOpen = false;
	@Attribute(name = "failure-date", required = false)
	private Date failureDate = null;
	@Attribute(name = "is-internal")
	private boolean isInternal = false;
	@Attribute(name = "order-id", required = false)
	private String orderId = null;

	public MailBoxUri getLocalMailBox() {
		return localMailBox;
	}

	public void setLocalMailBox(MailBoxUri localMailBox) {
		this.localMailBox = localMailBox;
	}

	public MailBoxUri getServiceMailBox() {
		return serviceMailBox;
	}

	public void setServiceMailBox(MailBoxUri serviceMailBox) {
		this.serviceMailBox = serviceMailBox;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
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

	public boolean isInternal() {
		return isInternal;
	}

	public void setInternal(boolean isInternal) {
		this.isInternal = isInternal;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void reset() {
		this.failureDate = null;
		this.orderId = null;
	}

}
