package client.core.system;

public class TaskBoard extends Entity implements client.core.model.TaskBoard {

	@Override
	public final ClassName getClassName() {
		return client.core.model.TaskBoard.CLASS_NAME;
	}
}
