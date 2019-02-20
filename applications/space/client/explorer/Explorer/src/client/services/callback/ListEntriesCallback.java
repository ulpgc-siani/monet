package client.services.callback;

import client.core.model.IndexEntry;
import client.core.model.List;

public interface ListEntriesCallback<T extends IndexEntry> extends Callback<List<T>> {
}
