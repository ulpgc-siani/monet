package org.monet.space.explorer.control.dialogs;

import com.google.inject.Inject;
import org.monet.metamodel.SourceDefinition;
import org.monet.space.explorer.control.dialogs.constants.Parameter;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.model.FeederUri;
import org.monet.space.kernel.model.Source;

public class LocateSourceDialog extends HttpDialog {
	private LayerProvider layerProvider;

	@Inject
	public void inject(LayerProvider layerProvider) {
		this.layerProvider = layerProvider;
	}

	public String getKey() {
		return getEntityId();
	}

	public FeederUri getUri() {
		String url = getString(Parameter.URL);

		if (url == null)
			return null;

		return FeederUri.build(url);
	}

}
