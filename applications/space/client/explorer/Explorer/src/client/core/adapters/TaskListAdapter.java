package client.core.adapters;

import client.core.model.*;
import client.core.system.Key;
import client.core.system.MonetList;
import client.services.TranslatorService;
import client.services.TranslatorService.Label;

public class TaskListAdapter {
	private final TranslatorService translatorService;

	public TaskListAdapter(TranslatorService translatorService) {
		this.translatorService = translatorService;
	}

	public void adapt(TaskList object) {
		client.core.system.TaskList taskList = (client.core.system.TaskList)object;

		taskList.setLabel(translate(Label.TASK_LIST));
		taskList.setFilters(buildFilters());
		taskList.setOrders(buildOrders());
		taskList.setViews(buildViews());
	}

	private List<Filter> buildFilters() {
		List<client.core.model.Filter> result = new MonetList<>();
		result.add(new client.core.system.Filter("label", translate(Label.TITLE)));
		result.add(new client.core.system.Filter("urgent", translate(Label.URGENT)));
		return result;
	}

	private List<Order> buildOrders() {
		List<client.core.model.Order> result = new MonetList<>();
		result.add(new client.core.system.Order("label", translate(Label.TITLE)));
		result.add(new client.core.system.Order("create_date", translate(Label.CREATE_DATE)));
		result.add(new client.core.system.Order("update_date", translate(Label.UPDATE_DATE)));
		result.add(new client.core.system.Order("urgent", translate(Label.URGENT)));
		return result;
	}

	private ViewList<client.core.model.TaskListView> buildViews() {
		ViewList<client.core.model.TaskListView> result = new client.core.system.ViewList<>();
		result.add(new client.core.system.TaskListView(new Key("all", "all"), translate(Label.ALL), false, TaskList.Situation.ALL));
		result.add(new client.core.system.TaskListView(new Key("alive", "alive"), translate(Label.ALIVE), false, TaskList.Situation.ALIVE));
		result.add(new client.core.system.TaskListView(new Key("active", "active"), translate(Label.ACTIVE), false, TaskList.Situation.ACTIVE));
		result.add(new client.core.system.TaskListView(new Key("pending", "pending"), translate(Label.PENDING), false, TaskList.Situation.PENDING));
		result.add(new client.core.system.TaskListView(new Key("finished", "finished"), translate(Label.FINISHED), false, TaskList.Situation.FINISHED));
		return result;
	}

	private String translate(Label label) {
		return translatorService.translate(label);
	}

}
