package client.core.model.factory;

import client.core.model.*;

public interface EntityFactory extends TypeFactory, FieldFactory, IndexFactory {
	Node createNode(final String id);
	Container createContainer(final String id);
	Task createTask(final String id);
	Activity createActivity(final String id);
	News createNews(final String label);
	Index createIndex(final String id);
	Source createSource(final String id);
	TaskList createTaskList();
}