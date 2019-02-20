package client.services.http.dialogs.index;

import client.core.model.Filter;
import client.core.model.Index;
import client.core.model.List;
import client.core.model.Order;
import client.services.IndexService;

public class SearchIndexEntriesDialog extends LoadIndexEntriesDialog {

    public SearchIndexEntriesDialog(Index index, IndexService.Scope scope, String condition, List<Filter> filters, List<Order> orders, int start, int limit) {
        super(index, scope, filters, orders, start, limit);
        add("condition", condition);
    }

}
