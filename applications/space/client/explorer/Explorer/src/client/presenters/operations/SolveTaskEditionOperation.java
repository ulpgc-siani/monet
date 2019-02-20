package client.presenters.operations;

import client.core.messages.SolvingTaskEditionError;
import client.core.model.Task;
import client.presenters.displays.MessageDisplay;
import client.services.TranslatorService;
import client.services.callback.TaskCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SolveTaskEditionOperation extends TaskActionOperation {

	public static final Type TYPE = new Type("SolveTaskEditionOperation", TaskActionOperation.TYPE);

	public SolveTaskEditionOperation(Context context, Task task) {
		super(context, task);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Task edition to solve: " + task.getLabel());
		getMessageDisplay().showLoading(services.getTranslatorService().getLoadingLabel());

		if (disableButtonWhenExecute())
			disable();

		services.getTaskService().solveEdition(task, new TaskCallback() {
			@Override
			public void success(Task task) {
				getMessageDisplay().hideLoading();
				executePerformed();
			}

			@Override
			public void failure(String details) {
				getMessageDisplay().hideLoading();
				getMessageDisplay().alert(services.getTranslatorService().translate(TranslatorService.ErrorLabel.WARNING), details, new MessageDisplay.AlertCallback() {
					@Override
					public void close() {
					}
				});
				executeFailed(new SolvingTaskEditionError(details));
				enable();
			}
		});
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(TranslatorService.OperationLabel.SOLVE_TASK_EDITION);
	}

}
