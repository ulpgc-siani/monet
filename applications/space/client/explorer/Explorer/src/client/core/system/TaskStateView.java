package client.core.system;

import client.core.model.Task;

public class TaskStateView extends TaskView implements client.core.model.TaskStateView {

	public TaskStateView() {
	}

	public TaskStateView(client.core.model.Key key, String label, boolean isDefault) {
		super(key, label, isDefault);
	}

	public TaskStateView(client.core.model.Key key, String label, boolean isDefault, Task scope) {
		super(key, label, isDefault, scope);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.TaskStateView.CLASS_NAME;
	}

}
