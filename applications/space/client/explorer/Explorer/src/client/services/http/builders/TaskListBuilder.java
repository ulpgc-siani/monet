package client.services.http.builders;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.TaskList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class TaskListBuilder extends EntityBuilder<TaskList, client.core.model.TaskList, List<client.core.model.TaskList>> implements Builder<client.core.model.TaskList, List<client.core.model.TaskList>> {
	@Override
	public client.core.model.TaskList build(HttpInstance instance) {
		if (instance == null)
			return null;

		client.core.system.TaskList taskList = new client.core.system.TaskList();
		initialize(taskList, instance);
		return taskList;
	}

	@Override
	public List<client.core.model.TaskList> buildList(HttpList instances) {
		return new MonetList<>();
	}
}
