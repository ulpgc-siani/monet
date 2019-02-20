package org.monet.space.explorer.control.dialogs;

import com.google.inject.Inject;
import org.monet.space.explorer.control.dialogs.constants.Parameter;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.model.Task;

public class SaveTaskUrgencyDialog extends HttpDialog {
	private LayerProvider layerProvider;

	@Inject
	public void inject(LayerProvider layerProvider) {
		this.layerProvider = layerProvider;
	}

	public Task getTask() {
		return layerProvider.getTaskLayer().loadTask(getEntityId());
	}

	public boolean isUrgent() {
		return getBoolean(Parameter.URGENT);
	}

}
