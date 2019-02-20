package client.widgets.index.entities;

import client.core.model.IndexEntry;
import client.core.model.NodeIndexEntry;
import client.core.model.definition.views.ViewDefinition;
import client.core.model.factory.EntityFactory;
import client.presenters.displays.IndexDisplay;
import client.presenters.displays.SearchIndexDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.services.TranslatorService;
import client.widgets.index.IndexFacet;
import client.widgets.index.IndexWidget;
import client.widgets.toolbox.FluidListController;
import client.widgets.toolbox.HTMLListWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.presenters.PresenterHolder;
import cosmos.gwt.widgets.CosmosHtmlPanel;
import cosmos.presenters.Presenter;

import static com.google.gwt.dom.client.Style.Unit;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class SearchFluidIndexWidget extends IndexWidget<SearchIndexDisplay, NodeIndexEntry> implements IndexFacet.Widget {
	private HTMLPanel embeddedEntityPanel;

	public SearchFluidIndexWidget(SearchIndexDisplay display, LayoutHelper layoutHelper, TranslatorService translator) {
		super(display, layoutHelper, translator);
		addStyleName(StyleName.SEARCH_INDEX_WIDGET);
		inject(new FluidIndexFacet<>(this, createDesktopHelper()));
	}

	@Override
	public void onHold(PresenterHolder holder) {
		setHeight("90%");
		facet.refreshListHeight();
		display.setPageSize(facet.getPageSize());
	}

	@Override
	protected void createComponents() {
		createEmbeddedEntityPanel();
		super.createComponents();
	}

	@Override
	protected void createLoading() {
		super.createLoading();
		loading.setHTML(translator.translate(TranslatorService.Label.SEARCHING));
	}

	private void createEmbeddedEntityPanel() {
		embeddedEntityPanel = new CosmosHtmlPanel();
	}

	@Override
	protected void refresh() {
		super.refresh();
		refreshWidths();
	}

	private void refreshWidths() {

		if (embeddedEntityPanel.getWidgetCount() == 0)
			$(getElement()).find(toRule(StyleName.LIST_BOX)).get(0).getStyle().setWidth(100, Unit.PCT);
		else {
			$(getElement()).find(toRule(StyleName.LIST_BOX)).get(0).getStyle().setWidth(30, Unit.PCT);
			$(getElement()).find(toRule(StyleName.EMBEDDED_ENTITY_BOX)).get(0).getStyle().setWidth(70, Unit.PCT);
		}
	}

	@Override
	protected void bind() {
		bindKeepingStyles(embeddedEntityPanel, toRule(StyleName.EMBEDDED_ENTITY_PANEL));
		super.bind();
	}

	@Override
	protected IndexItem.Builder getBuilder() {
		return new NodeIndexItem.Builder(layoutHelper, translator);
	}

	@Override
	protected void createItemsList() {
		super.createItemsList();

		new FluidListController<>(list, new FluidListController.PageHandler() {
			@Override
			public void nextPage() {
				facet.nextPage();
			}

			@Override
			public void reloadPage() {
				facet.reloadPage(false);
			}
		});
	}

	@Override
	public HTMLListWidget getList() {
		return list;
	}

	@Override
	public IndexDisplay getDisplay() {
		return display;
	}

	@Override
	public NodeIndexEntry createLoadingEntry() {
		EntityFactory entityFactory = getDisplay().getEntityFactory();
		return entityFactory.createNodeIndexEntry(entityFactory.createContainer(null), "loading");
	}

	public static class NodeIndexItem extends IndexItem<NodeIndexEntry> {
		protected HTMLPanel componentPanel;
		protected LayoutHelper helper;

		public NodeIndexItem(NodeIndexEntry value, LayoutHelper helper, TranslatorService translator) {
			super(value, translator, false);
			this.helper = helper;
			init();
		}

		@Override
		protected Widget createComponent() {
			componentPanel = new HTMLPanel(helper.getIndexEntryLayout(HTMLListWidget.Mode.SIMPLE));

			addLabel(componentPanel);
			refresh();

			return componentPanel;
		}

		protected void addLabel(HTMLPanel component) {
			Label label = new Label();

			label.addStyleName(StyleName.LABEL);
			label.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					notifyClick();
				}
			});

			replaceRegion(component, toRule(StyleName.LABEL), label);
		}

		protected void refreshLabel() {
			Label label = (Label) getRegion(toRule(StyleName.LABEL));

			label.setText(value.getLabel());
			label.setTitle(value.getLabel());
		}

		@Override
		public void refresh() {
			refreshLabel();
		}

		public interface StyleName extends IndexItem.StyleName {
			String LABEL = "label";
		}

		public static class Builder extends IndexItem.Builder<NodeIndexEntry> {
			private final LayoutHelper helper;

			public Builder(LayoutHelper helper, TranslatorService translator) {
				super(translator);
				this.helper = helper;
			}

			@Override
			public HTMLListWidget.ListItem<NodeIndexEntry> build(NodeIndexEntry value, HTMLListWidget.Mode mode) {
				return new NodeIndexItem(value, helper, translator);
			}

			@Override
			public HTMLListWidget.Mode[] getAcceptedModes() {
				return new HTMLListWidget.Mode[] { HTMLListWidget.Mode.LIST, HTMLListWidget.Mode.ICON };
			}
		}
	}

	public static class Builder extends IndexWidget.Builder<SearchIndexDisplay> {
		public static final String LAYOUT = ViewDefinition.Design.LIST.toString();

		public static void register() {
			registerBuilder(SearchIndexDisplay.TYPE.toString() + IndexWidget.Builder.DESIGN + LAYOUT, new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new SearchFluidIndexWidget((SearchIndexDisplay)presenter, createLayoutHelper("node", "search"), translator);
		}
	}

	private class FluidIndexFacet<E extends IndexEntry> extends client.widgets.index.facets.FluidIndexFacet<E> {
		com.google.gwt.user.client.ui.Widget activeItem = null;

		public FluidIndexFacet(Widget widget, DesktopHelper helper) {
			super(widget, helper);
		}

		@Override
		public void activateIndexEntry(E entry) {
			super.activateIndexEntry(entry);

			if (activeItem != null)
				activeItem.removeStyleName(StyleName.ACTIVE);

			com.google.gwt.user.client.ui.Widget item = list.getItem((NodeIndexEntry) entry);
			item.addStyleName(StyleName.ACTIVE);
			activeItem = item;
		}

		@Override
		public void refreshEntityView(E e, ViewDisplay viewDisplay) {
			setEmbeddedEntityView(viewDisplay);
		}
	}

	private void setEmbeddedEntityView(ViewDisplay viewDisplay) {
		String design = ViewDefinition.Design.DOCUMENT.toString();
		embeddedEntityPanel.clear();
		embeddedEntityPanel.add(IndexWidget.Builder.getViewBuilder().build(viewDisplay, design, design));
		refreshWidths();
	}

	protected interface StyleName extends IndexWidget.StyleName {
		String ACTIVE = "active";
		String LIST_BOX = "list-box";
		String EMBEDDED_ENTITY_BOX = "embedded-entity-box";
		String SEARCH_INDEX_WIDGET = "search-index-widget";
	}
}
