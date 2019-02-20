package client.services.http;

import client.core.adapters.DefinitionAdapter;
import client.core.adapters.NodeIndexEntriesAdapter;
import client.core.adapters.NodeIndexEntryAdapter;
import client.core.model.*;
import client.core.model.definition.entity.EntityDefinition;
import client.core.model.definition.entity.IndexDefinition;
import client.services.Services;
import client.services.callback.*;
import client.services.http.dialogs.index.*;

public class IndexService extends HttpService implements client.services.IndexService {

	public IndexService(Stub stub, Services services) {
		super(stub, services);
	}

	@Override
	public void open(String code, final IndexCallback callback) {
		Index index = services.getSpaceService().getEntityFactory().createIndex(code);
		loadDefinition(index, callback);
	}

	@Override
	public void getEntries(final Index index, final Scope scope, final List<Filter> filters, final List<Order> orders, final int start, final int limit, final NodeIndexEntriesCallback callback) {
		loadDefinition(index, new Callback() {
			@Override
			public void success(Object object) {
				stub.request(new LoadIndexEntriesDialog(index, scope, filters, orders, start, limit), NodeIndexEntry.CLASS_NAME, new NodeIndexEntriesCallback() {
					@Override
					public void success(List<NodeIndexEntry> object) {
						new NodeIndexEntriesAdapter().adapt(object, ((IndexDefinition) index.getDefinition()).getReference());
						callback.success(object);
					}

					@Override
					public void failure(String error) {
						callback.failure(error);
					}
				});
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	@Override
	public void getEntry(final Index index, final Scope scope, final Node entryNode, final NodeIndexEntryCallback callback) {
		loadDefinition(index, new Callback() {
			@Override
			public void success(Object object) {
				stub.request(new LoadIndexEntryDialog(index, scope, entryNode), NodeIndexEntry.CLASS_NAME, new NodeIndexEntryCallback() {
					@Override
					public void success(NodeIndexEntry object) {
						new NodeIndexEntryAdapter().adapt(object, ((IndexDefinition)index.getDefinition()).getReference());
						callback.success(object);
					}

					@Override
					public void failure(String error) {
						callback.failure(error);
					}
				});
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	@Override
	public void searchEntries(final Index index, final Scope scope, final String condition, final List<Filter> filters, final List<Order> orders, final int start, final int limit, final NodeIndexEntriesCallback callback) {
		loadDefinition(index, new Callback() {
			@Override
			public void success(Object object) {
				stub.request(new SearchIndexEntriesDialog(index, scope, condition, filters, orders, start, limit), NodeIndexEntry.CLASS_NAME, new NodeIndexEntriesCallback() {
					@Override
					public void success(List<NodeIndexEntry> object) {
						new NodeIndexEntriesAdapter().adapt(object, ((IndexDefinition)index.getDefinition()).getReference());
						callback.success(object);
					}

					@Override
					public void failure(String error) {
						callback.failure(error);
					}
				});
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	@Override
	public void searchNodeEntries(Node node, final String condition, final int start, final int limit, final NodeIndexEntriesCallback callback) {
		stub.request(new SearchNodeEntriesDialog(node, condition, start, limit), NodeIndexEntry.CLASS_NAME, callback);
	}

	@Override
	public void getHistory(Index index, Scope scope, String dataStore, int start, int limit, NodeIndexEntriesCallback callback) {
		stub.request(new HistoryDialog(index, scope, dataStore, null, start, limit), NodeIndexEntry.CLASS_NAME, callback);
	}

	@Override
	public void searchHistory(Index index, Scope scope, String dataStore, String filter, int start, int limit, NodeIndexEntriesCallback callback) {
		stub.request(new HistoryDialog(index, scope, dataStore, filter, start, limit), NodeIndexEntry.CLASS_NAME, callback);
	}

	@Override
	public void getFilterOptions(final Index index, final Scope scope, final Filter filter, final FilterOptionsCallback callback) {
		loadDefinition(index, new Callback() {
			@Override
			public void success(Object object) {
				stub.request(new LoadIndexFilterOptionsDialog(index, scope, filter), Filter.Option.CLASS_NAME, callback);
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	@Override
	public void getFilterOptionsReport(final Index index, final Scope scope, final Filter filter, final ReportCallback callback) {
		stub.request(new LoadIndexFilterOptionsReportDialog(index, scope, filter), Report.CLASS_NAME, callback);
	}

	@Override
	public void searchFilterOptions(final Index index, final Scope scope, final Filter filter, final String condition, final FilterOptionsCallback callback) {
		loadDefinition(index, new Callback() {
			@Override
			public void success(Object object) {
				stub.request(new SearchIndexFilterOptionsDialog(index, scope, filter, condition), Filter.Option.CLASS_NAME, callback);
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	private void loadDefinition(final Index index, final Callback callback) {
		services.getSpaceService().loadDefinition(index.getId(), IndexDefinition.CLASS_NAME, new DefinitionCallback<EntityDefinition>() {
			@Override
			public void success(EntityDefinition definition) {
				new DefinitionAdapter().adapt(index, definition);
				callback.success(index);
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

}
