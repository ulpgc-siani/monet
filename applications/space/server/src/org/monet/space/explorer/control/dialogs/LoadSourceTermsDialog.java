package org.monet.space.explorer.control.dialogs;

import com.google.inject.Inject;
import org.monet.metamodel.SourceDefinition;
import org.monet.space.explorer.control.dialogs.constants.Parameter;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.model.DataRequest;
import org.monet.space.kernel.model.Source;

public class LoadSourceTermsDialog extends HttpDialog {
	private LayerProvider layerProvider;

	@Inject
	public void inject(LayerProvider layerProvider) {
		this.layerProvider = layerProvider;
	}

	public Source<SourceDefinition> getSource() {
		return layerProvider.getSourceLayer().loadSource(getEntityId());
	}

	public String getCondition() {
		return getString(Parameter.CONDITION);
	}

	public int getStart() {
		return getInt(Parameter.START);
	}

	public int getLimit() {
		return getInt(Parameter.LIMIT);
	}

	public String getMode() {
		String mode = getString(Parameter.MODE);

		if (mode == null)
			return DataRequest.Mode.FLATTEN;

		return mode.toLowerCase();
	}

    public boolean hasCondition() {
        return getCondition() != null && !getCondition().isEmpty();
    }

	public String getFlatten() {
		return getString(Parameter.FLATTEN);
	}

	public String getDepth() {
		return getString(Parameter.DEPTH);
	}
}
