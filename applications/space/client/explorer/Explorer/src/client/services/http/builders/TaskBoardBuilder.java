package client.services.http.builders;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.TaskBoard;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class TaskBoardBuilder extends EntityBuilder<TaskBoard, client.core.model.TaskBoard, List<client.core.model.TaskBoard>> {
	@Override
	public client.core.model.TaskBoard build(HttpInstance instance) {
		if (instance == null)
			return null;

		client.core.system.TaskBoard taskBoard = new client.core.system.TaskBoard();
		initialize(taskBoard, instance);
		return taskBoard;
	}

	@Override
	public void initialize(client.core.model.TaskBoard object, HttpInstance instance) {
	}

	@Override
	public List<client.core.model.TaskBoard> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
