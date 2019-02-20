package client.core.model;

import cosmos.types.Date;

public interface TaskListIndexEntry extends IndexEntry<Task> {

	Instance.ClassName CLASS_NAME = new Instance.ClassName("TaskListIndexEntry");

	String getDescription();
	User getOwner();
	void setOwner(User owner);
	User getSender();
	Task.State getState();
	Task.Type getType();
	Date getCreateDate();
	Date getUpdateDate();
	int getMessagesCount();
	String getTimeLineImageUrl();
	boolean isUrgent();
	void setUrgent(boolean value);

}
