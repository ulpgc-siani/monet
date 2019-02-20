package client.core.system;

import client.core.model.List;

public class TaskList extends Entity implements client.core.model.TaskList {
	private client.core.model.ViewList<client.core.model.TaskListView> viewList;
	private List<client.core.model.Filter> filters;
	private List<client.core.model.Order> orders;

	public TaskList() {
	}

	public TaskList(String label, client.core.model.ViewList<client.core.model.TaskListView> viewList) {
		super("taskList", label);
		this.viewList = viewList;
	}

	@Override
	public String getDefinitionClass() {
		return TaskList.Abstract;
	}

	@Override
	public ClassName getClassName() {
		return client.core.model.TaskList.CLASS_NAME;
	}

	@Override
	public client.core.model.ViewList<client.core.model.TaskListView> getViews() {
		return this.viewList;
	}

	public void setViews(client.core.model.ViewList<client.core.model.TaskListView> viewList) {
		this.viewList = viewList;
	}

	@Override
	public client.core.model.TaskListView getView(client.core.model.Key key) {
		return getViews().get(key);
	}

	@Override
	public List<client.core.model.Filter> getFilters(client.core.model.Key view) {
		return this.filters;
	}

	public void setFilters(List<client.core.model.Filter> filters) {
		this.filters = filters;
	}

	@Override
	public List<client.core.model.Order> getOrders(client.core.model.Key view) {
		return orders;
	}

	public void setOrders(List<client.core.model.Order> orders) {
		this.orders = orders;
	}

}
