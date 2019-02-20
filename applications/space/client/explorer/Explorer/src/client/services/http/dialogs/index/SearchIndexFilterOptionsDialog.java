package client.services.http.dialogs.index;

import client.core.model.Filter;
import client.core.model.Index;
import client.services.IndexService;

public class SearchIndexFilterOptionsDialog extends LoadIndexFilterOptionsDialog {

    public SearchIndexFilterOptionsDialog(Index index, IndexService.Scope scope, Filter filter, String condition) {
        super(index, scope, filter);

        add("condition", condition);
    }

}
