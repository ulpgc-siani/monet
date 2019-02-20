package client.core.model;

import client.core.model.definition.entity.TaskDefinition;
import client.core.model.workmap.Action;
import client.core.model.workmap.WorkMap;
import cosmos.types.Date;

public interface Task<Definition extends TaskDefinition> extends Entity<Definition>, Searchable {

	ClassName CLASS_NAME = new ClassName("Task");

	enum State {
		NEW, PENDING, WAITING, EXPIRED, FINISHED, ABORTED, FAILURE;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

		public static State fromString(String state) {
			return State.valueOf(state.toUpperCase());
		}
	}

	Date getUpdateDate();

	WorkMap getWorkMap();
	<A extends Action> A getAction();

	ViewList<TaskView> getViews();
	TaskView getView(Key key);

	String getDefinitionClass();
	boolean isActivity();
	boolean isService();
	boolean isJob();

	State getState();
	void setState(State state);
	boolean isPending();
	boolean isWaiting();
	boolean isFailure();

	String[] search(String query);

}
