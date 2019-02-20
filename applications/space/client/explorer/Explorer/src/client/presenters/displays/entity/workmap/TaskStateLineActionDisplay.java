package client.presenters.displays.entity.workmap;

import client.core.model.Task;
import client.core.model.workmap.LineAction;
import client.core.model.workmap.LineAction.Stop;
import client.presenters.operations.SolveTaskLineOperation;
import cosmos.presenters.Operation;
import cosmos.presenters.Presenter;

public class TaskStateLineActionDisplay extends TaskStateActionDisplay<LineAction> {

	public static final Type TYPE = new Type("TaskStateLineActionDisplay", TaskStateActionDisplay.TYPE);

	public TaskStateLineActionDisplay(Task task, LineAction action) {
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
	}

	public String getDueMessage() {
        return services.getTranslatorService().getTaskLineTimeoutMessage(getAction().getDueDate(), getAction().getDueStop());
	}

	public Stop[] getStops() {
		return getAction().getStops();
	}

	public void selectStop(final Stop stop) {
		SolveTaskLineOperation operation = new SolveTaskLineOperation(new Operation.Context() {
			@Override
			public Presenter getCanvas() {
				return TaskStateLineActionDisplay.this.getOwner();
			}

			@Override
			public Operation getReferral() {
				return getVisitingDisplayOperation();
			}
		}, getTask(), stop);
		operation.inject(services);
		operation.execute();
	}

	public interface Hook extends TaskStateActionDisplay.Hook {
	}

	public static class Builder extends TaskStateActionDisplay.Builder<LineAction> {

		protected static void register() {
			registerBuilder(LineAction.CLASS_NAME.toString(), new Builder());
		}

		@Override
		public TaskStateActionDisplay build(Task entity, LineAction action) {
			return new TaskStateLineActionDisplay(entity, action);
		}
	}

}
