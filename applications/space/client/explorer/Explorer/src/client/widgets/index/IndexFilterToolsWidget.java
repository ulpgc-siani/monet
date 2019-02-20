package client.widgets.index;

import client.core.model.Filter;
import client.core.model.IndexEntry;
import client.core.model.List;
import client.core.model.Order;
import client.presenters.displays.IndexDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.services.TranslatorService;
import client.widgets.toolbox.HTMLListFilterToolsWidget;
import client.widgets.toolbox.IndexFilterItemWidget;
import client.widgets.toolbox.IndexFilterWidget;
import client.widgets.toolbox.SearchBoxWidget;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.model.Theme;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElement;

public class IndexFilterToolsWidget<D extends IndexDisplay, E extends IndexEntry> extends HTMLPanel {

	protected final D display;
	private final TranslatorService translator;
	private final LayoutHelper layoutHelper;
	private HTMLListFilterToolsWidget filterTools;
	private SearchBoxWidget searchInput;

	public IndexFilterToolsWidget(D display, LayoutHelper layoutHelper, TranslatorService translator) {
		super(getHtml(layoutHelper.getLayout(), translator));
		addStyleName(StyleName.INDEX_FILTER_TOOLS_WIDGET);
		this.layoutHelper = layoutHelper;
		this.display = display;
		this.translator = translator;
		createComponents();
		refresh();
		hook();
	}

	private void createComponents() {
		createSearchInput();
		createFilterTools();
		bind();
	}

	private void createFilterTools() {

		filterTools = new HTMLListFilterToolsWidget(new IndexFilterItemWidget.Builder(layoutHelper), new IndexFilterWidget.Delegate() {
			@Override
			public void loadFilterOptions(Filter filter, String condition) {
				display.loadFilterOptions(filter, condition);
			}
		}, translator, $(getElement()).children(toRule(StyleName.FILTER_TOOLS)).html());

		filterTools.addHandler(new HTMLListFilterToolsWidget.Handler() {

			@Override
			public void onSelectFilterOptions(Filter filter, List<Filter.Option> options) {
				filter.setOptions(options);
				display.select(filter);
			}

			@Override
			public void onClearFilterSelectedOptions(Filter filter) {
				display.unSelectAllFilter(filter);
			}

			@Override
			public void onSelectOrder(Order order) {
				display.select(order);
			}
		});

		for (Object filter : display.getFilters())
			filterTools.addFilter((Filter)filter);

		for (Object order : display.getOrders())
			filterTools.addOrder((Order)order);
	}


	private void createSearchInput() {
		searchInput = new SearchBoxWidget($(getElement()).find(toRule(StyleName.SEARCH_PANEL)).html(), SearchBoxWidget.Design.EXPANDED);
		bindWidgetToElement(this, searchInput, $(getElement()).find(toRule(StyleName.SEARCH_PANEL)).get(0));
		searchInput.setVisible(false);
		searchInput.addConditionHandler(new SearchBoxWidget.ConditionHandler() {

			private Timer timer;

			@Override
			public void onChange(final String condition) {
				if (timer != null) timer.cancel();
				timer = createTimer(condition);
				timer.schedule(500);
			}

			@Override
			public void onEnter(String condition) {
				if (timer != null) timer.cancel();
				display.setCondition(condition);
			}

			private Timer createTimer(final String condition) {
				return new Timer() {
					@Override
					public void run() {
						display.setCondition(condition);
					}
				};
			}
		});
	}

	private void refresh() {
	}

	protected void refreshFilterOptions(Filter filter, List<Filter.Option> options) {
		filterTools.setFilterOptions(filter, options);
	}

	private void refreshSelectedFilterOptions(Filter filter, List<Filter.Option> options) {
		filterTools.setSelectedFilterOptions(filter, options);
	}

	protected void hook() {
		display.addHook(new D.Hook<E>() {
			@Override
			public void clear() {
			}

			@Override
			public void loadingPage() {
			}

			@Override
			public void page(D.Page<E> page) {
			}

			@Override
			public void pagesCount(int count) {
				refreshToolsVisibility();
			}

			@Override
			public void pageEntryAdded(E entry) {
				refreshToolsVisibility();
			}

			@Override
			public void pageEntryDeleted() {
				refreshToolsVisibility();
			}

			@Override
			public void pageFailure() {
			}

			@Override
			public void pageEntryUpdated(E entry) {
			}

			@Override
			public void entityView(ViewDisplay entityViewDisplay) {
			}

			@Override
			public void selectEntries(List<E> entries) {
			}

			@Override
			public void selectOptions(Filter filter) {
				refreshSelectedFilterOptions(filter, filter.getOptions());
			}

			@Override
			public void loadingOptions() {
			}

			@Override
			public void options(Filter filter, List<Filter.Option> options) {
				refreshFilterOptions(filter, options);
			}
		});
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) refreshToolsVisibility();
	}

	private void refreshToolsVisibility() {
		searchInput.setVisible(display.canSearch());
		filterTools.setVisible(!display.getFilters().isEmpty());
	}

	private void bind() {
		bindWidgetToElement(this, filterTools, $(getElement()).find(toRule(StyleName.FILTER_TOOLS)).get(0));

		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		public static final String DESIGN = "filter-tools";

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(IndexDisplay.TYPE) && (design!=null && design.equals(DESIGN));
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			Widget widget;

			Builder builder = getChildBuilder(presenter, design, layout, translator, theme);
			if (builder != null)
				widget = builder.build(presenter, design, layout);
			else
				widget = new IndexFilterToolsWidget<>((IndexDisplay) presenter, createLayoutHelper(theme), translator);

			if (!layout.isEmpty())
				widget.addStyleName(layout);

			return widget;
		}

		@Override
		public void inject(TranslatorService translator) {
			super.inject(translator);
		}

		@Override
		public void inject(Theme theme) {
			super.inject(theme);
		}

		protected LayoutHelper createLayoutHelper(final Theme theme) {
			return new LayoutHelper() {

				@Override
				public String getLayout() {
					final HTMLPanel panel = new HTMLPanel(theme.getLayout("index-filter-tools"));
					$(panel).find(toRule(StyleName.SEARCH)).replaceWith(theme.getLayout(StyleName.SEARCH));
					return panel.getElement().getInnerHTML();
				}

				@Override
				public String getFilterLayout() {
					return theme.getLayout("index-filter-tools-filter");
				}

			};
		}

	}

	public interface LayoutHelper {
		String getLayout();
		String getFilterLayout();
	}

	public interface StyleName {
		String FILTER_TOOLS = "filter-tools";
		String INDEX_FILTER_TOOLS_WIDGET = "index-filter-tools-widget";
		String SEARCH_PANEL = "search-panel";
		String SEARCH = "search";
	}

}
