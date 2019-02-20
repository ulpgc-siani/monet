package org.monet.space.explorer.control.dialogs;

import com.google.inject.Inject;
import org.monet.space.explorer.control.dialogs.constants.Parameter;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.model.Node;

public class NodeDialog extends HttpDialog {

    private LayerProvider layerProvider;

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    public String getInstanceId() {
        return getString(Parameter.INSTANCE_ID);
    }

    public Node getNode() {
        return layerProvider.getNodeLayer().loadNode(getEntityId());
    }
}
