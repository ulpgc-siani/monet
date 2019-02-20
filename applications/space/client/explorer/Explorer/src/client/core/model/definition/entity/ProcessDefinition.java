package client.core.model.definition.entity;

import client.core.model.List;
import client.core.model.definition.views.TaskViewDefinition;

public interface ProcessDefinition extends TaskDefinition {
	List<TaskViewDefinition> getViews();
	TaskViewDefinition getView(String key);
}
