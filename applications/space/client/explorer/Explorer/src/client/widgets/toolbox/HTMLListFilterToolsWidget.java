package client.widgets.toolbox;

import client.core.model.Filter;
import client.core.model.List;
import client.core.model.Order;
import client.core.system.MonetList;
import client.services.TranslatorService;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.HashMap;
import java.util.Map;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElementAndKeepStyles;

public class HTMLListFilterToolsWidget extends HTMLPanel {
	private final IndexFilterWidget.Builder filterBuilder;
	private IndexFilterWidget.Delegate delegate;
	private final TranslatorService translator;
	private List<Handler> handlers = new MonetList<>();
	private Map<Filter, IndexFilterWidget> filterItems = new HashMap<>();
	private UnorderedListWidget filters;
	private OrdersWidget orders;
	private IndexFilterWidget selectedFilter;

	public enum Mode { LIST, ICON }

	public HTMLListFilterToolsWidget(IndexFilterWidget.Builder filterBuilder, IndexFilterWidget.Delegate delegate, TranslatorService translator, String layout) {
		super(layout);
		this.filterBuilder = filterBuilder;
		this.delegate = delegate;
		this.translator = translator;
		init();
	}

	public void addHandler(Handler handler) {
		handlers.add(handler);
	}

	public interface Handler {
		void onSelectFilterOptions(Filter filter, List<Filter.Option> options);
		void onClearFilterSelectedOptions(Filter filter);

		void onSelectOrder(Order order);
	}

	public void addFilter(final Filter filter) {
		filters.setVisible(true);

		final IndexFilterWidget filterItem = filterBuilder.build(filter, translator, delegate);
		filterItem.setHandler(new IndexFilterWidget.Handler() {
			@Override
			public void activateFilter() {
				activate(filterItem);
			}

			@Override
			public void deactivateFilter() {
				deactivate();
			}

			@Override
			public void onSelectOptions(List<Filter.Option> options) {
				notifyFilterSelectOptions(filter, options);
			}

			@Override
			public void onClearOptions() {
				notifyFilterClearOptions(filter);
			}
		});

		filterItems.put(filter, filterItem);
		filters.add(filterItem);
	}

	public void clearFilters() {
		filterItems.clear();
		filters.clear();
		filters.setVisible(false);
	}

	public void clearOrders() {
		orders.clearOrders();
		orders.setVisible(false);
	}

	public void setFilterOptions(Filter filter, List<Filter.Option> options) {
		if (filterItems.containsKey(filter))
			filterItems.get(filter).refreshOptions(options);
	}

	public void setSelectedFilterOptions(Filter filter, List<Filter.Option> options) {
		if (filterItems.containsKey(filter))
			filterItems.get(filter).refreshSelectedOptions(options);
	}

	public void addOrder(Order order) {
		orders.setVisible(true);
		orders.addOrder(order);
	}

	private void init() {
		addStyleName(StyleName.HTML_LIST_FILTER_TOOLS);
		createFiltersPanel();
		createOrdersPanel();
		bind();
	}

	private void createFiltersPanel() {
		filters = new UnorderedListWidget();
		filters.setVisible(false);
	}

	private void createOrdersPanel() {
		orders = new OrdersWidget($(getElement()).find(toRule(StyleName.ORDERS)).html(), translator);
		orders.setVisible(false);
		orders.setOrderSelectHandler(new OrdersWidget.OrderSelectHandler() {
			@Override
			public void select(Order order) {
				notifyOrderSelect(order);
			}
		});
	}


	private void bind() {
		bindWidgetToElementAndKeepStyles(this, orders, $(getElement()).children(toRule(StyleName.ORDERS)).get(0));
		bindWidgetToElementAndKeepStyles(this, filters, $(getElement()).children(toRule(StyleName.FILTERS)).get(0));

		onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private void activate(IndexFilterWidget filterItem) {
		if (selectedFilter != null)
			selectedFilter.deactivate();

		selectedFilter = filterItem;
		selectedFilter.activate();
	}

	private void deactivate() {
		if (selectedFilter == null)
			return;

		selectedFilter.deactivate();
		selectedFilter = null;
	}

	protected void notifyFilterSelectOptions(Filter filter, List<Filter.Option> options) {
		for (Handler handler : handlers)
			handler.onSelectFilterOptions(filter, options);
	}

	protected void notifyFilterClearOptions(Filter filter) {
		for (Handler handler : handlers)
			handler.onClearFilterSelectedOptions(filter);
	}

	protected void notifyOrderSelect(Order order) {
		for (Handler handler : handlers)
			handler.onSelectOrder(order);
	}

	private interface StyleName {
		String FILTERS = "filters";
		String HTML_LIST_FILTER_TOOLS = "html-list-filter-tools";
		String ORDERS = "orders";
	}
}