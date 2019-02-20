package org.monet.space.explorer.control.dialogs;

import com.google.inject.Inject;
import org.monet.metamodel.SourceDefinition;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.model.Source;

public class LoadSourceDialog extends HttpDialog {
	private LayerProvider layerProvider;

	@Inject
	public void inject(LayerProvider layerProvider) {
		this.layerProvider = layerProvider;
	}

	public Source<SourceDefinition> getSource() {
		return layerProvider.getSourceLayer().loadSource(getEntityId());
	}

}
