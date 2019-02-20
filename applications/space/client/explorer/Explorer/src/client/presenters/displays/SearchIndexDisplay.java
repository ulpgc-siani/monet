package client.presenters.displays;

import client.core.model.*;
import client.core.system.MonetList;
import client.services.callback.NodeIndexEntriesCallback;

public class SearchIndexDisplay extends IndexDisplay<Index, NodeIndexEntry> {
	private final Node container;
	private int entriesCount = -1;

	public static final Type TYPE = new Type("SearchResultsIndexDisplay", IndexDisplay.TYPE);

	public SearchIndexDisplay(Node container, Handler handler) {
		super(handler);
		this.container = container;
	}

	@Override
	protected void onInjectServices() {
	}

	@Override
	public boolean isEditable() {
		return container.isEditable();
	}

	@Override
	public List<Filter> getFilters() {
		return new MonetList<>();
	}

	@Override
	public List<Order> getOrders() {
		return new MonetList<>();
	}

	@Override
	public void loadFilterOptions(final Filter filter, String condition) {
	}

	@Override
	public int getEntriesCount() {
		return entriesCount;
	}

	@Override
	public void setEntriesCount(int count) {
		this.entriesCount = count;
	}

	@Override
	protected void updateEntriesCount(int count) {
		if (count == entriesCount)
			return;

		this.entriesCount = count;
		notifyPagesCount(getPagesCount());
	}

	@Override
	protected void loadEntries(int start, int limit) {
		services.getIndexService().searchNodeEntries(container, getCondition(), start, limit, new NodeIndexEntriesCallback() {
			@Override
			public void success(List<NodeIndexEntry> result) {
				successCallback(result);
			}

			@Override
			public void failure(String error) {
				notifyPageFailure();
			}
		});
	}

	@Override
	protected void loadEntry(final Entity entryEntity) {
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public interface Hook extends IndexDisplay.Hook<NodeIndexEntry> {}
	public interface Page extends IndexDisplay.Page<NodeIndexEntry> {}
	public interface Handler extends IndexDisplay.Handler<SearchIndexDisplay, NodeIndexEntry> {}

}
