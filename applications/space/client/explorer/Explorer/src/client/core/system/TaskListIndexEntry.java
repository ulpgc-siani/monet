package client.core.system;

import client.core.model.Task;
import client.core.model.Task.State;
import cosmos.types.Date;

public class TaskListIndexEntry extends IndexEntry<Task> implements client.core.model.TaskListIndexEntry {
	private String description;
	private Date createDate;
	private Date updateDate;
	private int messagesCount;
	private String timeLineImageUrl;
	private boolean urgent;
	private State state;
	private Task.Type type;
	private client.core.model.User owner;
	private client.core.model.User sender;

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public int getMessagesCount() {
		return messagesCount;
	}

	public void setMessagesCount(int messagesCount) {
		this.messagesCount = messagesCount;
	}

	@Override
	public String getTimeLineImageUrl() {
		return timeLineImageUrl;
	}

	public void setTimeLineImageUrl(String progressImage) {
		this.timeLineImageUrl = progressImage;
	}

	@Override
	public boolean isUrgent() {
		return urgent;
	}

	@Override
	public void setUrgent(boolean urgent) {
		this.urgent = urgent;
	}

	@Override
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public Task.Type getType() {
		return type;
	}

	public void setType(Task.Type type) {
		this.type = type;
	}

	@Override
	public client.core.model.User getOwner() {
		return this.owner;
	}

	@Override
	public void setOwner(client.core.model.User owner) {
		this.owner = owner;
	}

	@Override
	public client.core.model.User getSender() {
		return this.sender;
	}

	public void setSender(client.core.model.User sender) {
		this.sender = sender;
	}

}
