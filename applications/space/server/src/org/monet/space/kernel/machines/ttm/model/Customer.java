package org.monet.space.kernel.machines.ttm.model;

import org.monet.space.kernel.model.MailBoxUri;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Root(name = "customer")
public class Customer {

	@Attribute(name = "local-mail-box", required = false)
	private MailBoxUri localMailBox;
	@ElementList(name = "client-mail-box", required = false, inline = true)
	private ArrayList<MailBoxUri> clientMailBoxes = new ArrayList<MailBoxUri>();
	@Attribute(name = "order-id", required = false)
	private String orderId;
	@Attribute(name = "failure-date", required = false)
	private Date failureDate = null;

	public MailBoxUri getLocalMailBox() {
		return localMailBox;
	}

	public void setLocalMailBox(MailBoxUri localMailBox) {
		this.localMailBox = localMailBox;
	}

	public List<MailBoxUri> getClientMailBoxes() {
		return clientMailBoxes;
	}

	public void addClientMailBox(MailBoxUri clientMailBox) {
		this.clientMailBoxes.add(clientMailBox);
	}

	public void setOrderId(String value) {
		this.orderId = value;
	}

	public String getOrderId() {
		return this.orderId;
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
}
