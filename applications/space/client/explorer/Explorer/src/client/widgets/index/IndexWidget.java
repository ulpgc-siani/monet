package client.widgets.index;

import client.core.model.Entity;
import client.core.model.Filter;
import client.core.model.IndexEntry;
import client.core.model.List;
import client.presenters.displays.IndexDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.services.TranslatorService;
import client.widgets.LayoutWidget;
import client.widgets.index.entities.*;
import client.widgets.index.facets.FluidIndexFacet;
import client.widgets.toolbox.ErasableListWidget;
import client.widgets.toolbox.HTMLListWidget;
import client.widgets.view.EntityViewWidget;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.*;
import cosmos.gwt.model.HoldAble;
import cosmos.gwt.model.Theme;
import cosmos.gwt.presenters.PresenterHolder;
import cosmos.gwt.widgets.CosmosHtmlPanel;
import cosmos.presenters.Presenter;

import java.util.HashMap;
import java.util.Map;

import static client.widgets.toolbox.ErasableListWidget.ListItem;
import static client.widgets.toolbox.HTMLListWidget.Mode;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElement;

public abstract class IndexWidget<D extends IndexDisplay, E extends IndexEntry> extends CosmosHtmlPanel implements HoldAble {
	protected IndexFacet facet;
	protected final D display;
	protected final TranslatorService translator;
	protected final LayoutHelper layoutHelper;
	protected HTMLListWidget<E> list;
	protected HTML loading;

	public IndexWidget(D display, LayoutHelper layoutHelper, TranslatorService translator) {
		super(getHtml(layoutHelper.getIndexLayout(), translator));
		addStyleName(StyleName.INDEX_WIDGET);
		this.layoutHelper = layoutHelper;
		this.display = display;
		this.translator = translator;
		createComponents();
		refresh();
		hook();
	}

	public void inject(IndexFacet facet) {
		this.facet = facet;
	}

	@Override
	public void onHold(PresenterHolder holder) {
		display.setPageSize(facet.getPageSize());
		display.loadPage(0);
	}

	protected FluidIndexFacet.DesktopHelper createDesktopHelper() {
		return new FluidIndexFacet.DesktopHelper() {
			@Override
			public Element getMain() {
				return LayoutWidget.getMain();
			}

			@Override
			public Element getMainDisplay() {
				return LayoutWidget.getMainDisplay();
			}
		};
	}

	protected abstract IndexItem.Builder getBuilder();

	protected void createComponents() {
		createLoading();
		createItemsList();
		bind();
	}

	protected void createLoading() {
		loading = new HTML(translator.translate(TranslatorService.Label.LOADING));
		loading.addStyleName(StyleName.LOADING);
		loading.setVisible(false);
	}

	protected void createItemsList() {

		list = display.isEditable()?new ErasableListWidget<E>(getBuilder(), translator):new HTMLListWidget<E>(getBuilder(), translator);
		list.addAddHandler(new HTMLListWidget.AddHandler<E>() {
			@Override
			public void onAdd(final HTMLListWidget.ListItem<E> item) {
				final IndexItem indexItem = (IndexItem)item;
				indexItem.addSelectHandler(new IndexItem.SelectHandler() {
					@Override
					public void onSelect() {
						display.select((E)indexItem.getValue());
					}

					@Override
					public void onUnSelect() {
						display.unSelect((E)indexItem.getValue());
					}
				});
			}
		});

		if (list instanceof ErasableListWidget)
			((ErasableListWidget)list).addDeleteHandler(new ErasableListWidget.DeleteHandler<E>() {
				@Override
				public void onDelete(E value) {
					display.delete(value);
				}
			});

		list.addClickHandler(new HTMLListWidget.ClickHandler<E>() {
			@Override
			public void onClick(int index, E value) {
				facet.activateIndexEntry(value);
			}
		});
		list.addChangeHandler(new HTMLListWidget.ChangeHandler<E>() {
			@Override
			public void onChangeItem(int index, E value) {
			}

			@Override
			public void onChangeMode(Mode mode) {
				clearPages();
				facet.firstPage();
			}
		});

		list.setVisible(true);
	}

	protected void refresh() {
	}

	protected void refreshFilters() {
		// TODO mandar a cargar los filtros
	}

	protected void showLoadingPages() {
		loading.setVisible(true);
		list.disableEmptyMessage();
	}

	protected void hideLoadingPages() {
		loading.setVisible(false);
		list.enableEmptyMessage();
	}

	protected void clearPages() {
		list.clear();
	}

	protected void addPage(IndexDisplay.Page<E> page) {
		for (E indexIndexE : page.getEntries())
			addPageEntry(indexIndexE);
	}

	protected void addPageEntry(E entry) {
		list.addItem(entry);
	}

	protected void updatePageEntry(E entry) {
		Entity entity = entry.getEntity();

		for (Widget child : list.getItems()) {
			if (!(child instanceof IndexItem)) continue;
			IndexItem<E> item = (IndexItem) child;

			if (!entity.equals(item.getValue().getEntity()))
				continue;

			item.setValue(entry);
			item.refresh();
		}
	}

	protected void hook() {
		display.addHook(new IndexDisplay.Hook<E>() {
			@Override
			public void clear() {
				clearPages();
			}

			@Override
			public void loadingPage() {
				showLoadingPages();
			}

			@Override
			public void page(IndexDisplay.Page<E> page) {
				hideLoadingPages();
				addPage(page);
			}

			@Override
			public void pagesCount(int count) {
			}

			@Override
			public void pageEntryAdded(E entry) {
				addPageEntry(entry);
			}

			@Override
			public void pageEntryDeleted() {
			}

			@Override
			public void pageEntryUpdated(E entry) {
				updatePageEntry(entry);
			}

			@Override
			public void pageFailure() {
				hideLoadingPages();
			}

			@Override
			public void entityView(ViewDisplay entityViewDisplay) {
				facet.refreshEntityView(display.getActive(), entityViewDisplay);
			}

			@Override
			public void selectEntries(List<E> entries) {
			}

			@Override
			public void selectOptions(Filter filter) {
				refreshFilters();
			}

			@Override
			public void loadingOptions() {
			}

			@Override
			public void options(Filter filter, List<Filter.Option> options) {
			}
		});
	}

	protected void bind() {
		bindKeepingStyles(loading, toRule(StyleName.LOADING));
        bindKeepingStyles(list, toRule(StyleName.ITEMS));

		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	public interface StyleName {
		String ITEMS = "items";
		String EMBEDDED_ENTITY_PANEL = "embedded-entity";
		String LOADING = "loading";
		String INDEX_WIDGET = "index-widget";
	}

	public static abstract class IndexItem<E extends IndexEntry> extends ListItem<E> {
		private boolean selected = false;
		private SelectHandler selectHandler = null;
		private final Map<String, Widget> regions = new HashMap<>();

		public IndexItem(E value, TranslatorService translator, boolean init) {
			super(value, translator, init);
		}

		public void addSelectHandler(SelectHandler selectHandler) {
			this.selectHandler = selectHandler;
		}

		public abstract void refresh();

		public interface SelectHandler {
			void onSelect();
			void onUnSelect();
		}

		@Override
		protected Widget[] createControlOperations() {
			Widget[] controlOperations = new Widget[1];
			controlOperations[0] = createCheckBox();
			return controlOperations;
		}

		private Widget createCheckBox() {
			CheckBox checkBox = new CheckBox();
			checkBox.setTitle(translator.translate(TranslatorService.ListLabel.SELECT_ELEMENT));
			checkBox.addStyleName(StyleName.SELECT_BOX);
			checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onChange(HTMLListWidget.ListItem<Boolean> item) {
				}

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					select(event.getValue());
				}
			});
			return checkBox;
		}

		private void select(Boolean value) {
			selected = value;

			if (selected)
				this.addStyleName(StyleName.SELECTED);
			else
				this.removeStyleName(StyleName.SELECTED);

			notifySelect();
		}

		private void notifySelect() {
			if (selectHandler == null)
				return;

			if (selected)
				selectHandler.onSelect();
			else
				selectHandler.onUnSelect();
		}

		protected Widget getRegion(String cssRegion) {
			return regions.get(cssRegion);
		}

		protected void replaceRegion(HTMLPanel component, String cssRule, Widget content) {

			if (content == null)
				$(component).find(cssRule).remove();
			else
				bindWidgetToElement(component, content, $(component).find(cssRule).get(0));

			regions.put(cssRule, content);
		}

		public abstract static class Builder<E extends IndexEntry> extends ListItem.Builder<E> {
			public Builder(TranslatorService translator) {
				super(translator);
			}
		}

		public interface StyleName {
			String SELECTED = "selected";
			String SELECT_BOX = "select-box";
		}

	}

	public static class Builder<D extends IndexDisplay> extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {
		private static EntityViewWidget.Builder viewBuilder;

		public static final String DESIGN = "default";
		public static final String REPORT = "report";

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			register();
			return presenter.is(D.TYPE) && design != null && design.equals(DESIGN);
		}

		@Override
		public void inject(TranslatorService translator) {
			super.inject(translator);
			if (theme != null) createBuilders();
		}

		@Override
		public void inject(Theme theme) {
			super.inject(theme);
			if (translator != null) createBuilders();
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			Builder builder = getChildBuilder(presenter, design, layout, translator, theme);
			if (builder == null)
				return null;

			Widget widget = builder.build(presenter, design, layout);
			if (!layout.isEmpty())
				widget.getElement().addClassName(layout);

			return widget;
		}

		public static EntityViewWidget.Builder getViewBuilder() {
			return viewBuilder;
		}

		protected LayoutHelper createLayoutHelper(final String type, final String layout) {
			return new LayoutHelper() {
				private Map<String, String> templates = null;

				private void parseTemplates() {
					if (templates != null)
						return;

					templates = new HashMap<>();

					String templatesLayoutName = "index-templates" + (layout!=null?"-"+layout:"");
					if (!theme.existsLayout(templatesLayoutName))
						templatesLayoutName = "index-templates";

					String templatesLayout = theme.getLayout(templatesLayoutName);
					HTMLPanel panel = new HTMLPanel(templatesLayout);
					NodeList<Element> elements = $(panel).not("div div").find("div").get();

					for (int i = 0; i < elements.getLength(); i++) {
						Element element = elements.getItem(i);
						templates.put(element.getClassName(), element.getInnerHTML());
					}
				}

				@Override
				public String getIndexLayout() {
					String layoutName = "index" + (layout!=null?"-"+layout:"");

					if (!theme.existsLayout(layoutName))
						layoutName = "index";

					return theme.getLayout(layoutName);
				}

				@Override
				public boolean existsIndexTemplate(String name) {
					parseTemplates();
					return templates.containsKey(name);
				}

				@Override
				public String getIndexTemplate(String name) {
					parseTemplates();
					return templates.get(name);
				}

				@Override
				public String getIndexEntryLayout(Mode mode) {
					String layoutName = "index-entry" + (layout != null ? "-" + layout : "") + " " + type;

					if (!theme.existsLayout(layoutName))
						layoutName = "index-entry " + type;

					if (!theme.existsLayout(layoutName))
						layoutName = "index-entry" + (layout != null ? "-" + layout : "");

					if (!theme.existsLayout(layoutName))
						layoutName = "index-entry";

					if (theme.existsLayout(layoutName + " " + mode.toString().toLowerCase()))
						layoutName = layoutName + " " + mode.toString().toLowerCase();

					return theme.getLayout(layoutName);
				}

			};
		}

		protected static void register() {
			SearchFluidIndexWidget.Builder.register();
			SetFluidIndexWidget.Builder.register();
			SetSolidIndexWidget.Builder.register();
			TaskListFluidIndexWidget.Builder.register();
			TaskListIndexFilterToolsWidget.Builder.register();
			SetSolidIndexToolbarWidget.Builder.register();
			TaskListIndexToolbarWidget.Builder.register();
			IndexReportWidget.Builder.register();
		}

		private void createBuilders() {
			if (viewBuilder != null)
				return;
			viewBuilder = new EntityViewWidget.Builder();
			viewBuilder.inject(translator);
			viewBuilder.inject(theme);
		}

	}

	public interface LayoutHelper {
		String getIndexLayout();
		boolean existsIndexTemplate(String name);
		String getIndexTemplate(String name);
		String getIndexEntryLayout(Mode mode);
	}

}
