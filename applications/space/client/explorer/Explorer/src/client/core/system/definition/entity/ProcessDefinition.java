package client.core.system.definition.entity;

import client.core.model.List;

import java.util.HashMap;
import java.util.Map;

public class ProcessDefinition extends TaskDefinition implements client.core.model.definition.entity.ProcessDefinition {
	private List<client.core.model.definition.views.TaskViewDefinition> views;
	private Map<String, client.core.model.definition.views.TaskViewDefinition> viewsMap = new HashMap<>();

	@Override
	public List<client.core.model.definition.views.TaskViewDefinition> getViews() {
		return views;
	}

	public void setViews(client.core.model.List<client.core.model.definition.views.TaskViewDefinition> views) {
		this.views = views;
		this.viewsMap.clear();

		for (client.core.model.definition.views.TaskViewDefinition view : views) {
			this.viewsMap.put(view.getName(), view);
			this.viewsMap.put(view.getCode(), view);
		}
	}

	@Override
	public client.core.model.definition.views.TaskViewDefinition getView(String key) {
		return viewsMap.get(key);
	}

}
