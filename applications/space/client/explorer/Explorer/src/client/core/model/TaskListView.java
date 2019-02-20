package client.core.model;

import static client.core.model.TaskList.*;

public interface TaskListView extends View {

	ClassName CLASS_NAME = new ClassName("TaskListView");

	Situation getSituation();
	ClassName getClassName();

}
