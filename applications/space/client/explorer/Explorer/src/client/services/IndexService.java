package client.services;

import client.core.model.*;
import client.core.model.definition.entity.field.LinkFieldDefinition;
import client.services.callback.*;

public interface IndexService extends Service {
	void open(String code, final IndexCallback callback);

	void getEntries(Index index, Scope scope, List<Filter> filters, List<Order> orders, int start, int limit, final NodeIndexEntriesCallback callback);
	void getEntry(Index index, Scope scope, Node node, final NodeIndexEntryCallback callback);
	void searchEntries(Index index, Scope scope, String condition, List<Filter> filters, List<Order> orders, int start, int limit, final NodeIndexEntriesCallback callback);
	void searchNodeEntries(Node container, final String condition, final int start, final int limit, final NodeIndexEntriesCallback callback);
	void getHistory(Index index, Scope scope, String dataStore, int start, int limit, NodeIndexEntriesCallback callback);
	void searchHistory(Index index, Scope scope, String dataStore, String filter, int start, int limit, NodeIndexEntriesCallback callback);

	void getFilterOptions(Index index, Scope scope, Filter filter, FilterOptionsCallback callback);
	void getFilterOptionsReport(Index index, Scope scope, Filter filter, final ReportCallback callback);
	void searchFilterOptions(Index index, Scope scope, Filter filter, String condition, FilterOptionsCallback callback);

	interface Scope {
		String getIndexView();
	}

	interface NodeScope extends Scope {
		Set getSet();
		Key getSetView();
	}

	interface FieldScope extends Scope {
		String getSingleton();
		LinkFieldDefinition getFieldDefinition();
	}
}
