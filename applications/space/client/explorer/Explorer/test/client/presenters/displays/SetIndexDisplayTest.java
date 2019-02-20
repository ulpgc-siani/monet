package client.presenters.displays;

import client.core.IndexBuilder;
import client.core.NodeBuilder;
import client.core.model.*;
import client.core.system.MonetList;
import client.core.system.Report;
import client.presenters.displays.view.ViewDisplay;
import client.services.*;
import client.services.callback.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class SetIndexDisplayTest {
	private Map<String, Boolean> calls = new HashMap<>();

	@Test
	public void handleCallsAreMade() {
		SetIndexDisplay setIndexDisplay = createIndexDisplay();
		NodeIndexEntry entry = createIndexEntry("1", "Entrada 1");

		setIndexDisplay.activate(entry);
		assertTrue(called("Handler.activate"));
		resetCalls();

		setIndexDisplay.select(entry);
		assertTrue(called("Handler.select"));
		resetCalls();

		setIndexDisplay.unSelect(entry);
		assertTrue(called("Handler.unSelect"));
		resetCalls();

		setIndexDisplay.unSelectAll();
		assertTrue(called("Handler.unSelectAll"));
		resetCalls();

		setIndexDisplay.delete(entry);
		assertTrue(called("Handler.delete"));
		resetCalls();
	}

	@Test
	public void hooksCallsAreMade() {
		SetIndexDisplay setIndexDisplay = createIndexDisplay();

		setIndexDisplay.clear();
		assertTrue(called("Hook.clear"));
		resetCalls();
	}

	@Test
	public void checkSelectionCountIsModifiedWhenSelectedItemIsRemoved() {
		SetIndexDisplay setIndexDisplay = createIndexDisplay();
		NodeIndexEntry entry = createIndexEntry("2", "Entrada 2");

		setIndexDisplay.add(createIndexEntry("1", "Entrada 1"));
		setIndexDisplay.add(entry);
		setIndexDisplay.select(entry);

		assertEquals(setIndexDisplay.getSelectionCount(), 1);
		setIndexDisplay.delete(entry);
		assertEquals(setIndexDisplay.getSelectionCount(), 0);

		assertTrue(called("Handler.delete"));
		assertTrue(called("Handler.unSelect"));
		assertTrue(called("Hook.selectEntries"));
		assertTrue(called("Hook.clear"));
		assertTrue(called("Hook.page"));
	}

	private SetIndexDisplay createIndexDisplay() {
		resetCalls();

		SetIndexDisplay display = new SetIndexDisplay(null, IndexBuilder.buildIndex("indice", new Index.Attribute[0]), null, new SetIndexDisplay.Handler() {
			@Override
			public void onActivate(SetIndexDisplay display, NodeIndexEntry entry) {
				calls.put("Handler.activate", true);
			}

			@Override
			public void onSelect(SetIndexDisplay display, NodeIndexEntry entry) {
				calls.put("Handler.select", true);
			}

			@Override
			public void onDelete(SetIndexDisplay display, NodeIndexEntry entry) {
				calls.put("Handler.delete", true);
				display.reloadPage();
			}

			@Override
			public void onUnSelect(SetIndexDisplay display, NodeIndexEntry entry) {
				calls.put("Handler.unSelect", true);
			}

			@Override
			public void onUnSelectAll(SetIndexDisplay display) {
				calls.put("Handler.unSelectAll", true);
			}
		});
		display.addHook(new SetIndexDisplay.Hook() {
			@Override
			public void clear() {
				calls.put("Hook.clear", true);
			}

			@Override
			public void loadingPage() {

			}

			@Override
			public void page(IndexDisplay.Page<NodeIndexEntry> page) {
				calls.put("Hook.page", true);
			}

			@Override
			public void pagesCount(int count) {
				calls.put("Hook.pagesCount", true);
			}

			@Override
			public void pageEntryAdded(NodeIndexEntry entry) {
				calls.put("Hook.pageEntryAdded", true);
			}

			@Override
			public void pageEntryDeleted() {
			}

			@Override
			public void pageFailure() {
				calls.put("Hook.failure", true);
			}

			@Override
			public void pageEntryUpdated(NodeIndexEntry entry) {
			}

			@Override
			public void entityView(ViewDisplay entityViewDisplay) {
				calls.put("Hook.entityView", true);
			}

			@Override
			public void selectEntries(List<NodeIndexEntry> entries) {
				calls.put("Hook.selectEntries", true);
			}

			@Override
			public void selectOptions(Filter filter) {
				calls.put("Hook.selectOptions", true);
			}

			@Override
			public void loadingOptions() {

			}

			@Override
			public void options(Filter filter, List<Filter.Option> options) {
				calls.put("Hook.options", true);
			}
		});
		display.inject(createServices());

		return display;
	}

	private Services createServices() {
		return new Services() {
			@Override
			public SpaceService getSpaceService() {
				return null;
			}

			@Override
			public NodeService getNodeService() {
				return null;
			}

			@Override
			public TaskService getTaskService() {
				return null;
			}

			@Override
			public AccountService getAccountService() {
				return null;
			}

			@Override
			public SourceService getSourceService() {
				return null;
			}

			@Override
			public IndexService getIndexService() {
				return new IndexService() {
					@Override
					public void open(String code, IndexCallback callback) {
					}

					@Override
					public void getEntries(Index index, Scope scope, List<Filter> filters, List<Order> orders, int start, int limit, NodeIndexEntriesCallback callback) {
						callback.success(new MonetList<NodeIndexEntry>());
					}

					@Override
					public void getEntry(Index index, Scope scope, Node entryNode, NodeIndexEntryCallback callback) {
					}

					@Override
					public void searchEntries(Index index, Scope scope, String condition, List<Filter> filters, List<Order> orders, int start, int limit, NodeIndexEntriesCallback callback) {
						callback.success(new MonetList<NodeIndexEntry>());
					}

					@Override
					public void searchNodeEntries(Node container, String condition, int start, int limit, NodeIndexEntriesCallback callback) {
					}

					@Override
					public void getHistory(Index index, Scope scope, String dataStore, int start, int limit, NodeIndexEntriesCallback callback) {

					}

					@Override
					public void searchHistory(Index index, Scope scope, String dataStore, String filter, int start, int limit, NodeIndexEntriesCallback callback) {

					}

					@Override
					public void getFilterOptions(Index index, Scope scope, Filter filter, FilterOptionsCallback callback) {
						callback.success(new MonetList<Filter.Option>());
					}

					@Override
					public void getFilterOptionsReport(Index index, Scope scope, Filter filter, ReportCallback callback) {
						callback.success(new Report());
					}

					@Override
					public void searchFilterOptions(Index index, Scope scope, Filter filter, String condition, FilterOptionsCallback callback) {
						callback.success(new MonetList<Filter.Option>());
					}

				};
			}

			@Override
			public NewsService getNewsService() {
				return null;
			}

			@Override
			public TranslatorService getTranslatorService() {
				return null;
			}

            @Override
            public NotificationService getNotificationService() {
                return null;
            }
		};
	}

	private NodeIndexEntry createIndexEntry(String id, String label) {
		return IndexBuilder.buildNodeIndexEntry(NodeBuilder.buildContainer(null), label);
	}

	private void resetCalls() {
		calls.clear();
	}

	private boolean called(String event) {
		return calls.containsKey(event) && calls.get(event);
	}

}