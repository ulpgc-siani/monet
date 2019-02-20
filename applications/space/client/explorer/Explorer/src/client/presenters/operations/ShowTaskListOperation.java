package client.presenters.operations;

import client.core.model.TaskList;
import client.core.model.TaskListView;
import client.presenters.displays.ApplicationDisplay;
import client.presenters.displays.EntityVisitingDisplay;
import client.presenters.displays.ExplorationDisplay;
import client.presenters.displays.entity.TaskListDisplay;
import client.services.TranslatorService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowTaskListOperation extends ShowOperation<TaskList, TaskListView> {

	public static final Type TYPE = new Type("ShowTaskListOperation", Operation.TYPE);

	public ShowTaskListOperation(Context context, TaskList taskList, TaskListView view) {
		super(context, taskList, view);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. " + (view!=null?"View: " + view.getLabel():""));
		refresh();
	}

	private void refresh() {
		refreshExplorationDisplay();
		refreshCanvas();
	}

	private void refreshExplorationDisplay() {
		ApplicationDisplay display = this.context.getCanvas();
		ExplorationDisplay explorationDisplay = display.getExplorationDisplay();
		explorationDisplay.setRootOperation(explorationDisplay.get(this));
		explorationDisplay.clearChildren();
	}

	private void refreshCanvas() {
		TaskListDisplay entityDisplay = new TaskListDisplay.Builder().build(getEntity(), view);
		EntityVisitingDisplay visitingDisplay = new EntityVisitingDisplay(entityDisplay, this.context.getReferral());
		ApplicationDisplay display = this.context.getCanvas();

		display.removeVisitingDisplay();
		display.addVisitingDisplay(visitingDisplay);

		this.executePerformed();
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getMenuLabel(boolean fullLabel) {
		TranslatorService service = services.getTranslatorService();
		return service.translate(TranslatorService.Label.TASK_LIST);
	}

	@Override
	public String getDefaultLabel() {
		TranslatorService service = services.getTranslatorService();
		return service.translate(TranslatorService.OperationLabel.SHOW_TASK_LIST);
	}

	@Override
	public String getDescription() {
		return entity.getLabel();
	}
}
