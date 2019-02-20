package org.monet.space.explorer.control.dialogs;

import com.google.inject.Inject;
import org.monet.space.explorer.control.dialogs.constants.Parameter;
import org.monet.space.explorer.model.ComponentProvider;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.User;

public class SaveTaskOwnerDialog extends TaskDialog {
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

	public Task getTask() {
		return layerProvider.getTaskLayer().loadTask(getEntityId());
	}

	public User getOwner() {
		return componentProvider.getComponentFederation().getDefaultLayer().loadUser(getString(Parameter.OWNER));
	}

	public String getReason() {
		return getString(Parameter.REASON);
	}
}
