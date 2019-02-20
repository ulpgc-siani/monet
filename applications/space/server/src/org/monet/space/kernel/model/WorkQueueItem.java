package org.monet.space.kernel.model;

import org.simpleframework.xml.core.Persister;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

public class WorkQueueItem extends BaseObject {
	public static final String TARGET = "target";

	private String target;
	private WorkQueueType type;
	private WorkQueueState state = WorkQueueState.Pending;
	private int retries = 0;
	private String lastError;
	private Date lastUpdateTime;

	public WorkQueueItem(WorkQueueType type, Object target) throws Exception {
		this.type = type;
		this.setTarget(target);
	}

	public WorkQueueItem() {

	}

	public void setTarget(String target) {
		this.addLoadedAttribute(TARGET);
		this.target = target;
	}

	public <T> void setTarget(Object target) throws Exception {
		Persister persister = new Persister();
		StringWriter writer = new StringWriter();
		persister.write(target, writer);
		this.setTarget(writer.toString());
	}

	public String getTarget() {
		this.onLoad(this, TARGET);
		return this.target;
	}

	public <T> T getTarget(Class<T> clazz) throws Exception {
		String target = this.getTarget();
		Persister persister = new Persister();
		return persister.read(clazz, target);
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date value) {
		this.lastUpdateTime = value;
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}

	public void setState(WorkQueueState state) {
		this.state = state;
	}

	public WorkQueueState getState() {
		return state;
	}

	public void setType(WorkQueueType type) {
		this.type = type;
	}

	public WorkQueueType getType() {
		return type;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public int getRetries() {
		return retries;
	}

	public void setLastError(String lastError) {
		this.lastError = lastError;
	}

	public String getLastError() {
		return lastError;
	}
}