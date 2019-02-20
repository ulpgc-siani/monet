package client.core;

import client.core.model.definition.Ref;
import client.core.model.*;

public class ListBuilder<T> {

	public static final ViewList<TaskView> EmptyTaskViewList = ListBuilder.buildTaskViewList(new TaskView[0]);
	public static final ViewList<TaskListView> EmptyTaskListViewList = ListBuilder.buildTaskListViewList(new TaskListView[0]);
	public static final ViewList<NodeView> EmptyNodeViewList = ListBuilder.buildNodeViewList(new NodeView[0]);
	public static final List<Command> EmptyCommandList = ListBuilder.buildCommandList(new Command[0]);
	public static final List<Ref> EmptyRefList = ListBuilder.buildRefList(new Ref[0]);

	public static List<Notification> buildNotificationList(final Notification[] elements) {
		return new client.core.system.MonetList(elements);
	}

	public static BusinessUnitList buildBusinessUnitList(final BusinessUnit[] elements) {
		return new client.core.system.BusinessUnitList(elements);
	}

	public static ViewList<client.core.model.NodeView> buildNodeViewList(final NodeView[] elements) {
		return new client.core.system.ViewList<>(elements);
	}

	public static ViewList<TaskView> buildTaskViewList(final TaskView[] elements) {
		return new client.core.system.ViewList<>(elements);
	}

	public static ViewList<TaskListView> buildTaskListViewList(final TaskListView[] elements) {
		return new client.core.system.ViewList<>(elements);
	}

	public static List<Role> buildRoleList(final Role[] elements) {
		return new client.core.system.MonetList(elements);
	}

	public static List<Command> buildCommandList(final Command[] elements) {
		return new client.core.system.MonetList(elements);
	}

	public static List<Ref> buildRefList(final Ref[] elements) {
		return new client.core.system.MonetList(elements);
	}

}
