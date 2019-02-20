package client.services.http.builders;

import client.core.model.TaskList;
import client.core.system.TaskListView;
import client.core.system.ViewList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class TaskListViewBuilder extends EntityBuilder<TaskListView, client.core.model.TaskListView, client.core.model.ViewList<client.core.model.TaskListView>> implements Builder<client.core.model.TaskListView, client.core.model.ViewList<client.core.model.TaskListView>> {

	@Override
	public client.core.model.TaskListView build(HttpInstance instance) {
		if (instance == null)
			return null;

		TaskListView view = new TaskListView();
		initialize(view, instance);
		return view;
	}

	@Override
	public void initialize(client.core.model.TaskListView object, HttpInstance instance) {
		super.initialize(object, instance);

		TaskListView taskListView = (TaskListView)object;

		if (!instance.getString("state").isEmpty())
			taskListView.setSituation(TaskList.Situation.fromString(instance.getString("state")));
	}

	@Override
	public client.core.model.ViewList<client.core.model.TaskListView> buildList(HttpList instance) {
		client.core.model.ViewList<client.core.model.TaskListView> result = new ViewList<>();

		for (int i = 0; i < instance.getItems().length(); i++)
			result.add(build(instance.getItems().get(i)));

		return result;
	}

}
