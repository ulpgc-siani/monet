package org.monet.space.explorer.control.dialogs;

import com.google.inject.Inject;
import org.monet.metamodel.IndexDefinition;
import org.monet.space.explorer.control.dialogs.constants.Parameter;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.model.HistoryStore;

public class IndexHistoryDialog extends LoadIndexDialog {

    private LayerProvider layerProvider;

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    public HistoryStore getStore() {
        return layerProvider.getHistoryStoreLayer().loadHistoryStore(getString(Parameter.STORE));
    }

    public IndexDefinition getDefinition() {
        return dictionary.getIndexDefinition(getEntityId());
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
}
