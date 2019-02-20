package client.presenters.operations;

import client.core.messages.SolvingTaskLineError;
import client.core.model.Task;
import client.core.model.workmap.LineAction.Stop;
import client.services.TranslatorService;
import client.services.callback.TaskCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SolveTaskLineOperation extends TaskActionOperation {
	private final Stop stop;

	public static final Type TYPE = new Type("SolveTaskLineOperation", TaskActionOperation.TYPE);

	public SolveTaskLineOperation(Context context, Task task, Stop stop) {
		super(context, task);
		this.stop = stop;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Task line to solve: " + task.getLabel());
		getMessageDisplay().showLoading(services.getTranslatorService().getLoadingLabel());

		if (disableButtonWhenExecute())
			disable();

		this.services.getTaskService().solveLine(task, stop, new TaskCallback() {
			@Override
			public void success(Task task) {
				getMessageDisplay().hideLoading();
				executePerformed();
			}

			@Override
			public void failure(String details) {
				getMessageDisplay().hideLoading();
				executeFailed(new SolvingTaskLineError(details));
			}
		});
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(TranslatorService.OperationLabel.SOLVE_TASK_LINE);
	}

}
