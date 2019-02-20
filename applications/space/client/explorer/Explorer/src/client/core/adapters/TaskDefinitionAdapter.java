package client.core.adapters;

import client.core.model.Task;
import client.core.model.TaskView;
import client.core.model.definition.entity.ActivityDefinition;
import client.core.model.definition.entity.PlaceDefinition;
import client.core.model.definition.entity.TaskDefinition;
import client.core.model.workmap.Action;
import client.core.model.workmap.Place;
import client.core.system.ViewList;
import client.services.TranslatorService;

public class TaskDefinitionAdapter {
	private final TranslatorService translatorService;

	public TaskDefinitionAdapter(TranslatorService translatorService) {
		this.translatorService = translatorService;
	}

	public void adapt(Task task, TaskDefinition definition) {
		task.setDefinition(definition);

		adaptViews(task);

		if (definition instanceof ActivityDefinition)
			adaptPlace(task, (ActivityDefinition) definition);
	}

	private void adaptViews(client.core.model.Task task) {
		for (TaskView view : (ViewList<TaskView>)task.getViews())
			if (view.equals(TaskView.STATE_VIEW))
				view.setLabel(translate(TranslatorService.Label.STATE));
	}

	private void adaptPlace(Task task, ActivityDefinition definition) {
		Place place = task.getWorkMap().getPlace();
		PlaceDefinition placeDefinition = definition.getPlace(place.getCode());

		Action action = task.getAction();
		if (action != null)
			action.setDefinition(placeDefinition.getAction());
	}

	private String translate(TranslatorService.Label label) {
		return translatorService.translate(label);
	}

}