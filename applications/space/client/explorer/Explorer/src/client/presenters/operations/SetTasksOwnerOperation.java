package client.presenters.operations;

import client.core.messages.SettingTasksOwnerError;
import client.core.model.Task;
import client.core.model.User;
import client.services.TranslatorService;
import client.services.callback.VoidCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SetTasksOwnerOperation extends Operation {
	private final User owner;
	private final TaskSelection selection;

	public static final Type TYPE = new Type("SetTasksOwnerOperation", Operation.TYPE);

	public SetTasksOwnerOperation(Context context, User owner, TaskSelection selection) {
		super(context);
		this.owner = owner;
		this.selection = selection;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Selection to remove: " + serializeTasks());
		getMessageDisplay().showLoading(services.getTranslatorService().getLoadingLabel());

		this.services.getTaskService().saveOwner(this.selection.get(), owner, "", new VoidCallback() {
			@Override
			public void success(Void value) {
				getMessageDisplay().hideLoading();
				executePerformed();
			}

			@Override
			public void failure(String details) {
				getMessageDisplay().hideLoading();
				executeFailed(new SettingTasksOwnerError(details));
			}
		});
	}

	private String serializeTasks() {
		String result = "";
		for (Task task : selection.get()) {
			if (!result.isEmpty())
				result += ",";
			result += task.getLabel();
		}
		return result;
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(TranslatorService.OperationLabel.SET_TASKS_OWNER_SELECTION);
	}

	public interface TaskSelection {
		Task[] get();
	}
}
