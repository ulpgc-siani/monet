package org.monet.space.explorer.control.dialogs;

import com.google.inject.Inject;
import org.monet.space.explorer.control.dialogs.constants.Parameter;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.model.Node;

public class DeleteFieldDialog extends HttpDialog {
	private LayerProvider layerProvider;

	@Inject
	public void inject(LayerProvider layerProvider) {
		this.layerProvider = layerProvider;
	}

	public Node getNode() {
		return layerProvider.getNodeLayer().loadNode(getEntityId());
	}

	public String getParentPath() {
		return getString(Parameter.PARENT);
	}

	public int getPos() {
		return getInt(Parameter.POS);
	}

    public String getInstanceId() {
        return getString(Parameter.INSTANCE_ID);
    }
}
