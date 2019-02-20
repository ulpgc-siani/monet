package client.core.model.factory;

import client.core.model.*;

public interface IndexFactory {
	NodeIndexEntry createNodeIndexEntry(Node node, String label);
	TaskListIndexEntry createTaskListIndexEntry(Task task, String label);
	Filter createTypeFilter();
	Filter createFilter(String name, String label);
	Filter.Option createFilterOption(String value, String label);
	Order createOrder(String name, String label, Order.Mode mode);
}
