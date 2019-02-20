package client.core.system;

import static client.core.model.TaskList.Situation;

public class TaskListView extends View implements client.core.model.TaskListView {
	private Situation situation;

	public TaskListView() {
	}

	public TaskListView(Key key, String label, boolean isDefault, Situation situation) {
		super(key, label, isDefault);
		this.situation = situation;
	}

	@Override
	public Situation getSituation() {
		return situation;
	}

	public void setSituation(Situation situation) {
		this.situation = situation;
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.TaskListView.CLASS_NAME;
	}
}
