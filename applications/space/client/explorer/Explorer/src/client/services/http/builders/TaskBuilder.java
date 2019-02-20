package client.services.http.builders;

import client.core.model.List;
import client.core.system.Task;
import client.services.http.HttpInstance;
import client.services.http.builders.workmap.WorkMapBuilder;

public class TaskBuilder<T extends client.core.model.Task> extends EntityBuilder<Task, T, List<T>> implements Builder<T, List<T>> {

	@Override
	public void initialize(T object, HttpInstance instance) {
		super.initialize(object, instance);

		client.core.system.Task task = (client.core.system.Task)object;
		task.setUpdateDate(instance.getDate("updateDate"));

		if (!instance.getString("state").isEmpty())
			task.setState(Task.State.fromString(instance.getString("state")));

		if (!instance.getString("type").isEmpty())
			task.setType(Task.Type.fromString(instance.getString("type")));

		task.setWorkMap(new WorkMapBuilder().build(instance.getObject("workMap")));
	}

}
