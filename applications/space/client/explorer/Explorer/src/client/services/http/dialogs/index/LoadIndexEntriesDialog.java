package client.services.http.dialogs.index;

import client.core.model.Filter;
import client.core.model.Index;
import client.core.model.List;
import client.core.model.Order;
import client.services.IndexService;
import client.services.http.serializers.FilterSerializer;
import client.services.http.serializers.OrderSerializer;

public class LoadIndexEntriesDialog extends IndexDialog {
    private final Index index;

    public LoadIndexEntriesDialog(Index index, IndexService.Scope scope, List<Filter> filters, List<Order> orders, int start, int limit) {
        this.index = index;

        addScope(scope);
        add("filters", new FilterSerializer().serializeList(filters));
        add("orders", new OrderSerializer().serializeList(orders));
        add("start", start);
        add("limit", limit);
    }

    @Override
    public String getOperation() {
        return "index$entries";
    }

    @Override
    public String getEntityId() {
        return index.getId();
    }

}
