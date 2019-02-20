package client.presenters.displays.entity.workmap;

import client.core.model.Form;
import client.core.model.Node;
import client.core.model.Task;
import client.core.model.workmap.EditionAction;
import client.presenters.displays.view.ViewDisplay;
import client.presenters.operations.SolveTaskEditionOperation;
import client.services.callback.NodeCallback;
import cosmos.presenters.Operation;
import cosmos.presenters.Presenter;

public class TaskStateEditionActionDisplay extends TaskStateActionDisplay<EditionAction> {

	public static final Type TYPE = new Type("TaskStateEditionActionDisplay", TaskStateActionDisplay.TYPE);
	private SolveTaskEditionOperation solveTaskEditionOperation;

	public TaskStateEditionActionDisplay(Task task, EditionAction action) {
        super(task, action);
	}

	@Override
	protected void onInjectServices() {
		addSolveOperation();
		addForm();
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void load() {
	}

	private void addForm() {
		Form form = getAction().getForm();
		notifyFormLoading();

		services.getNodeService().open(form.getId(), new NodeCallback() {
			@Override
			public void success(Node object) {
				getAction().setForm((Form) object);
				Form form = (Form) object;
				ViewDisplay display = new ViewDisplay.Builder<>().build(form, form.getViews().getDefaultView());
				addChild(display);
				solveTaskEditionOperation.setVisible(true);
				notifyFormLoaded();
			}

			@Override
			public void failure(String error) {
				notifyFormFailure(error);
			}
		});
	}

	private void addSolveOperation() {
		solveTaskEditionOperation = new SolveTaskEditionOperation(new Operation.Context() {
			@Override
			public Presenter getCanvas() {
				return TaskStateEditionActionDisplay.this.getOwner();
			}

			@Override
			public Operation getReferral() {
				return getVisitingDisplayOperation();
			}
		}, getTask());

		solveTaskEditionOperation.inject(services);
		solveTaskEditionOperation.setVisible(false);

		addChild(solveTaskEditionOperation);
	}

	public interface Hook extends TaskStateActionDisplay.Hook {
		void formLoading();
		void formLoaded();
		void formFailure(String error);
	}

	public static class Builder extends TaskStateActionDisplay.Builder<EditionAction> {

		protected static void register() {
			registerBuilder(EditionAction.CLASS_NAME.toString(), new Builder());
		}

		@Override
		public TaskStateActionDisplay build(Task entity, EditionAction action) {
			return new TaskStateEditionActionDisplay(entity, action);
		}
	}

	private void notifyFormLoading() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.formLoading();
			}
		});
	}

	private void notifyFormLoaded() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.formLoaded();
			}
		});
	}

	private void notifyFormFailure(final String error) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.formFailure(error);
			}
		});
	}
}
