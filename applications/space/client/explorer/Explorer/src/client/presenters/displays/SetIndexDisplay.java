package client.presenters.displays;

import client.core.model.*;
import client.core.model.definition.views.SetViewDefinition;
import client.core.system.MonetList;
import client.services.IndexService;
import client.services.callback.FilterOptionsCallback;
import client.services.callback.NodeIndexEntriesCallback;
import client.services.callback.NodeIndexEntryCallback;

public class SetIndexDisplay extends IndexDisplay<Index, NodeIndexEntry> {

	private final Node node;
	private final View view;
	private int entriesCount = -1;

	public static final Type TYPE = new Type("SetIndexDisplay", IndexDisplay.TYPE);

	public SetIndexDisplay(Node node, Index index, View view, Handler handler) {
		super(index, handler);
		this.node = node;
		this.view = view;
	}

	@Override
	protected void onInjectServices() {
	}

	@Override
	public boolean isEditable() {
		return node.isEditable();
	}

	@Override
	public List<Filter> getFilters() {
		return ((Set)node).getFilters(view.getKey());
	}

	@Override
	public List<Order> getOrders() {
		return ((Set)node).getOrders(view.getKey());
	}

	@Override
	public void loadFilterOptions(final Filter filter, String condition) {
		IndexService service = this.services.getIndexService();

		notifyLoadingOptions();

		FilterOptionsCallback callback = new FilterOptionsCallback() {
			@Override
			public void success(List<Filter.Option> options) {
				notifyOptions(filter, options);
			}

			@Override
			public void failure(String error) {
				notifyFilterOptionsFailure(filter);
			}
		};

		if (condition == null || condition.isEmpty())
			service.getFilterOptions(getEntity(), createScope(), filter, callback);
		else
			service.searchFilterOptions(getEntity(), createScope(), filter, condition, callback);
	}

	private IndexService.Scope createScope() {
		return new IndexService.NodeScope() {
			@Override
			public Set getSet() {
				return (Set)node;
			}

			@Override
			public Key getSetView() {
				return view.getKey();
			}

			@Override
			public String getIndexView() {
				SetViewDefinition.ShowDefinition.IndexDefinition index = ((SetView) view).getDefinition().getShow().getIndex();
				return index == null ? null : index.getWithView();
			}

		};
	}

	@Override
	public List<Order> getSelectedOrders() {
		final List<Order> orders = super.getSelectedOrders();
		if (!orders.isEmpty() || getShow() == null || getShow().getIndex() == null)
			return orders;
		String sortBy = getShow().getIndex().getSortBy();
		String sortMode = getShow().getIndex().getSortMode();
		if (sortMode == null) sortMode = "desc";
		return new MonetList<>(getEntityFactory().createOrder(sortBy, sortBy, Order.Mode.fromString(sortMode)));
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
		if (count == entriesCount) return;

		this.entriesCount = count;
		notifyPagesCount(getPagesCount());
	}

	@Override
	protected void loadEntries(int start, int limit) {
		loadEntries(start, limit, getCondition(), new NodeIndexEntriesCallback() {
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

	private void loadEntries(int start, int limit, String condition, NodeIndexEntriesCallback callback) {
		if (condition == null)
			services.getIndexService().getEntries(getEntity(), createScope(), getSelectedFilters(), getSelectedOrders(), start, limit, callback);
		else
			services.getIndexService().searchEntries(getEntity(), createScope(), condition, getSelectedFilters(), getSelectedOrders(), start, limit, callback);
	}

	@Override
	protected void loadEntry(Entity entryEntity) {
		services.getIndexService().getEntry(getEntity(), createScope(), (Node) entryEntity, new NodeIndexEntryCallback() {
			@Override
			public void success(NodeIndexEntry result) {
				notifyUpdatePageEntry(result);
			}

			@Override
			public void failure(String error) {
				notifyPageFailure();
			}
		});
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	private SetViewDefinition.ShowDefinition getShow() {
		return ((SetView) view).getDefinition().getShow();
	}

	public interface Hook extends IndexDisplay.Hook<NodeIndexEntry> {}
	public interface Page extends IndexDisplay.Page<NodeIndexEntry> {}
	public interface Handler extends IndexDisplay.Handler<SetIndexDisplay, NodeIndexEntry> {}

}
