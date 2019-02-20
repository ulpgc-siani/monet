package client.services.http.builders;

import client.core.model.List;
import client.core.model.Task;
import client.core.system.TaskListIndexEntry;
import client.services.http.HttpInstance;

public class TaskListIndexEntryBuilder extends IndexEntryBuilder<Task, client.core.model.TaskListIndexEntry> implements Builder<client.core.model.TaskListIndexEntry, List<client.core.model.TaskListIndexEntry>> {

	@Override
	public client.core.model.TaskListIndexEntry build(HttpInstance instance) {
		if (instance == null)
			return null;

		TaskListIndexEntry entry = new TaskListIndexEntry();
		initialize(entry, instance);
		return entry;
	}

	@Override
	public void initialize(client.core.model.TaskListIndexEntry object, HttpInstance instance) {
		super.initialize(object, instance);

		TaskListIndexEntry entry = (TaskListIndexEntry)object;

		entry.setDescription(instance.getString("description"));
		entry.setCreateDate(instance.getDate("createDate"));
		entry.setUpdateDate(instance.getDate("updateDate"));
		entry.setMessagesCount(instance.getInt("messagesCount"));
		entry.setTimeLineImageUrl(instance.getString("timeLineImageUrl"));
		entry.setUrgent(instance.getBoolean("urgent"));

		if (!instance.getString("state").isEmpty())
			entry.setState(Task.State.fromString(instance.getString("state")));

		if (!instance.getString("type").isEmpty())
			entry.setType(Task.Type.fromString(instance.getString("type")));

		entry.setOwner(new UserBuilder().build(instance.getObject("owner")));
		entry.setSender(new UserBuilder().build(instance.getObject("sender")));
	}

	@Override
	protected EntityBuilder getEntityBuilder() {
		return new TaskListBuilder();
	}

}
