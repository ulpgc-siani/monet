package client.core.system;

import client.core.model.Task;

public abstract class TaskView extends View implements client.core.model.TaskView {
	private Task scope;

	public TaskView() {
	}

	public TaskView(client.core.model.Key key, String label, boolean isDefault) {
		super(key, label, isDefault);
	}

	public TaskView(client.core.model.Key key, String label, boolean isDefault, Task scope) {
		super(key, label, isDefault);
		this.scope = scope;
	}

	public Task getScope() {
		return this.scope;
	}

	public void setScope(Task scope) {
		this.scope = scope;
	}

}
