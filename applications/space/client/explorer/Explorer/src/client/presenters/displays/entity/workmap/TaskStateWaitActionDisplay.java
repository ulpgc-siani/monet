package client.presenters.displays.entity.workmap;

import client.core.model.Task;
import client.core.model.workmap.WaitAction;
import client.presenters.operations.SetupTaskWaitOperation;
import client.services.TranslatorService;
import cosmos.presenters.Operation;
import cosmos.presenters.Presenter;

import static client.core.model.workmap.WaitAction.Scale;
import static client.core.model.workmap.WaitAction.Step;

public class TaskStateWaitActionDisplay extends TaskStateActionDisplay<WaitAction> {

	public static final Type TYPE = new Type("TaskStateWaitActionDisplay", TaskStateActionDisplay.TYPE);

	public TaskStateWaitActionDisplay(Task task, WaitAction action) {
        super(task, action);
	}

	@Override
	protected void onInjectServices() {
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void load() {
		notifyStep();
	}

	public Step getStep() {
		return getAction().getStep();
	}

	public String getMessage() {
		TranslatorService translator = services.getTranslatorService();
		return translator.getTaskWaitMessage(getAction().getDueDate());
	}

	public void increment(final Scale scale, final int count) {
		SetupTaskWaitOperation operation = new SetupTaskWaitOperation(new Operation.Context() {
			@Override
			public Presenter getCanvas() {
				return TaskStateWaitActionDisplay.this.getOwner();
			}

			@Override
			public Operation getReferral() {
				return getVisitingDisplayOperation();
			}
		}, getTask(), scale, count);

		operation.inject(services);
		operation.execute();
	}

	public void decrement(final Scale scale, final int count) {
		SetupTaskWaitOperation operation = new SetupTaskWaitOperation(new Operation.Context() {
			@Override
			public Presenter getCanvas() {
				return TaskStateWaitActionDisplay.this.getOwner();
			}

			@Override
			public Operation getReferral() {
				return getVisitingDisplayOperation();
			}
		}, getTask(), scale, -count);

		operation.inject(services);
		operation.execute();
	}

	public interface Hook extends TaskStateActionDisplay.Hook {
		void step();
	}

	public static class Builder extends TaskStateActionDisplay.Builder<WaitAction> {

		protected static void register() {
			registerBuilder(WaitAction.CLASS_NAME.toString(), new Builder());
		}

		@Override
		public TaskStateActionDisplay build(Task entity, WaitAction action) {
			return new TaskStateWaitActionDisplay(entity, action);
		}
	}

	private void notifyStep() {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.step();
			}
		});
	}


}
