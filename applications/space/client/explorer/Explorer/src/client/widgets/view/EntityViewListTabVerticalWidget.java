package client.widgets.view;

import client.core.model.View;
import client.presenters.displays.view.ViewDisplay;
import client.presenters.displays.view.ViewListDisplay;
import client.services.TranslatorService;
import client.widgets.toolbox.HTMLListWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.widgets.CosmosHtmlPanel;
import cosmos.presenters.Presenter;

public class EntityViewListTabVerticalWidget extends CosmosHtmlPanel {
	private final ViewListDisplay<View> display;
	private final TranslatorService translator;
	private HTMLListWidget<ViewDisplay> list;

	public EntityViewListTabVerticalWidget(ViewListDisplay display, String layout, TranslatorService translator) {
        super(layout);
		addStyleName(StyleName.TAB, StyleName.VERTICAL);
		this.display = display;
		this.translator = translator;
		createComponents();
		refresh();
		hook();
	}

	private void createComponents() {
		setWidth("100%");

		list = new HTMLListWidget<>(new Tab.Builder(), translator);
		list.addStyleName(StyleName.TAB);
		list.addClickHandler(new HTMLListWidget.ClickHandler<ViewDisplay>() {
			@Override
			public void onClick(int index, ViewDisplay value) {
				display.activateView(value);
			}
		});
		add(list);

		bind();
	}

	private void refresh() {
        refreshHighlight(refreshViews());
	}

	private ViewDisplay refreshViews() {
		ViewDisplay selectedView = null;

		list.clear();
		for (ViewDisplay viewDisplay : display.getViews()) {
			cosmos.gwt.presenters.Presenter.Builder builder = Builder.getViewBuilder();
			if (!builder.canBuild(viewDisplay, "tab")) continue;

			if (display.isActiveView(viewDisplay))
				selectedView = viewDisplay;

			list.addItem(viewDisplay);
		}

		return selectedView;
	}

	private void refreshHighlight(ViewDisplay display) {
		deactivateAll();

		Tab tab = getTab(display);
		if (tab != null)
		    tab.highlight();
	}

	private void deactivateAll() {
		for (Widget child : list.getItems())
			if (child instanceof Tab)
                ((Tab) child).deactivate();
	}

	private Tab getTab(ViewDisplay display) {

		for (Widget child : this.list.getItems()) {
			if (!(child instanceof Tab)) continue;
			Tab childTab = (Tab)child;
			if (childTab.isFor(display))
				return childTab;
		}

		return null;
	}

	private void hook() {
		display.addHook(new ViewListDisplay.Hook() {
			@Override
			public void activate(ViewDisplay viewDisplay) {
				refreshHighlight(viewDisplay);
			}
		});
	}

	private void bind() {
		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {
		public static EntityViewWidget.Builder viewBuilder;

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(ViewListDisplay.TYPE) && design.equals("tab-vertical");
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			createViewBuilder();
			return new EntityViewListTabVerticalWidget((ViewListDisplay) presenter, getLayout(layout), translator);
		}

		public static EntityViewWidget.Builder getViewBuilder() {
			return viewBuilder;
		}

		private void createViewBuilder() {
			if (viewBuilder != null) return;

			viewBuilder = new EntityViewWidget.Builder();
			viewBuilder.inject(translator);
			viewBuilder.inject(theme);
		}

		private String getLayout(String layout) {
			return layout.isEmpty() ? "" : theme.getLayout(layout);
		}
	}

	public static class Tab extends HTMLListWidget.ListItem<ViewDisplay> {
		private Anchor component;

		public Tab(ViewDisplay value) {
			super(value);
			init();
		}

		public void highlight() {
			addStyleName(StyleName.HIGHLIGHT);
		}

		public void deactivate() {
			removeStyleName(StyleName.HIGHLIGHT);
		}

		public boolean isFor(ViewDisplay display) {
			return value == display;
		}

		@Override
		protected Widget[] createControlOperations() {
			return new Widget[0];
		}

		@Override
		protected Widget createComponent() {
			component = new Anchor(getLabel());
			component.setStyleName(StyleName.LABEL);

			component.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					notifyClick();
				}
			});

			return component;
		}

		private String getLabel() {
			return value.getLabel();
		}

		@Override
		protected Widget[] createOperations() {
			return new Widget[0];
		}

		public static class Builder extends HTMLListWidget.ListItem.Builder<ViewDisplay> {

			@Override
			public HTMLListWidget.ListItem<ViewDisplay> build(ViewDisplay value, HTMLListWidget.Mode mode) {
                return new Tab(value);
			}
		}

		private interface StyleName {
			String HIGHLIGHT = "highlight";
			String LABEL = "label";
		}
	}

	private interface StyleName {
		String TAB = "tab";
		String VERTICAL = "vertical";
	}

}
