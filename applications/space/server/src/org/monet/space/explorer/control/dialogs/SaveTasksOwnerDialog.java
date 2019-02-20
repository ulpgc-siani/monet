package org.monet.space.explorer.control.dialogs;

import com.google.inject.Inject;
import org.monet.space.explorer.control.dialogs.constants.Parameter;
import org.monet.space.explorer.model.ComponentProvider;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.User;

import java.util.ArrayList;
import java.util.List;

public class SaveTasksOwnerDialog extends HttpDialog {
	private LayerProvider layerProvider;
	private ComponentProvider componentProvider;

	@Inject
	public void inject(LayerProvider layerProvider) {
		this.layerProvider = layerProvider;
	}

	@Inject
	public void inject(ComponentProvider componentProvider) {
		this.componentProvider = componentProvider;
	}

	public Task[] getTasks() {
		TaskLayer taskLayer = layerProvider.getTaskLayer();
		String[] ids = getEntityIds();
		List<Task> result = new ArrayList<>();

		for (String id : ids)
			result.add(taskLayer.loadTask(id));

		return result.toArray(new Task[result.size()]);
	}

	public User getOwner() {
		return componentProvider.getComponentFederation().getDefaultLayer().loadUser(getString(Parameter.OWNER));
	}

	public String getReason() {
		return getString(Parameter.REASON);
	}

}
