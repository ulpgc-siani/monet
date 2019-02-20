package client.presenters.displays.view;

import client.core.model.*;
import client.core.model.Task.State;
import client.core.model.definition.views.TaskViewDefinition;
import client.core.model.workmap.Action;
import client.presenters.displays.OperationListDisplay;
import client.presenters.operations.DownloadNodeOperation;
import client.services.callback.NodeCallback;
import cosmos.types.Date;

import java.util.logging.Logger;

public class TaskShortcutViewDisplay extends TaskViewDisplay<TaskShortcutView> {
	private ViewDisplay shortcutDisplay;

	public static final Type TYPE = new Type("TaskShortcutViewDisplay", TaskViewDisplay.TYPE);

	public TaskShortcutViewDisplay(Task task, TaskShortcutView view) {
		super(task, view);
	}

	@Override
	protected void onInjectServices() {
		addShortcut();
		addOperations();
	}

	public String getTitle() {
		Action action = getEntity().getWorkMap().getPlace().getAction();

		if (action == null)
			return "";

		return action.getDefinition().getLabel();
	}

	public Date getDate() {
		return getEntity().getUpdateDate();
	}

	public State getState() {
		return getEntity().getState();
	}

	private void addShortcut() {
		Node shortcut = getView().getShortcut();

		services.getNodeService().open(shortcut.getId(), new NodeCallback() {
			@Override
			public void success(Node object) {
				String viewKey = ((TaskViewDefinition)getView().getDefinition()).getShow().getShortcutView();
				View view = viewKey!=null?object.getView(viewKey):(View)object.getViews().get(0);
				shortcutDisplay = new ViewDisplay.Builder<>().build(object, view);
				shortcutDisplay.inject(services);
				notifyShortcut();
			}

			@Override
			public void failure(String error) {
				notifyShortcutFailure(error);
			}
		});
	}

	private void addOperations() {
		OperationListDisplay display = getOperationListDisplay();
		if (display == null)
			return;

		DownloadNodeOperation operation = new DownloadNodeOperation(getOperationContext(), getView().getShortcut());
		operation.inject(services);

		display.addChild(operation);
	}
	private void notifyShortcut() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.shortcut();
			}
		});
	}

	private void notifyShortcutFailure(final String error) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.shortcutFailure(error);
			}
		});
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public ViewDisplay getShortcut() {
		return shortcutDisplay;
	}

	public static class Builder extends TaskViewDisplay.Builder<TaskShortcutView> {

		protected static void register() {
			registerBuilder(Activity.CLASS_NAME.toString() + TaskShortcutView.CLASS_NAME.toString(), new Builder());
			registerBuilder(Service.CLASS_NAME.toString() + TaskShortcutView.CLASS_NAME.toString(), new Builder());
		}

		@Override
		public ViewDisplay build(Task entity, TaskShortcutView view) {
			return new TaskShortcutViewDisplay(entity, view);
		}
	}

	public interface Hook extends TaskViewDisplay.Hook {
		void shortcut();
		void shortcutFailure(String error);
	}

}
