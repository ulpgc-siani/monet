package client.services.callback;

public interface PendingTasksCallback extends Callback<PendingTasksCallback.Result> {

	interface Result {
		int getTaskTrayCount();
		int getTaskBoardCount();
	}

}
