package client.core.model;

public interface TaskList extends Entity, Showable, AllowAnalyze {

	ClassName CLASS_NAME = new ClassName("TaskList");
	String Abstract = "task-list";

	enum Situation {
		ALL, ALIVE, ACTIVE, PENDING, FINISHED;

		public static Situation fromString(String state) {
			return Situation.valueOf(state.toUpperCase());
		}
	}

	enum Inbox {
		TASK_TRAY, TASK_BOARD
	}

	String getDefinitionClass();
	ClassName getClassName();

	ViewList<TaskListView> getViews();
	TaskListView getView(Key key);

}
