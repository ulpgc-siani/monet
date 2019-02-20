package client.widgets.view;

import client.core.model.NodeView;
import client.presenters.displays.view.ViewDisplay;
import client.presenters.displays.view.ViewListDisplay;
import client.services.TranslatorService;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

public class NodeViewListTabWidget extends TabPanel {
	private final ViewListDisplay<NodeView> display;

	public NodeViewListTabWidget(ViewListDisplay display) {
		super();
        addStyleName("tab");
		this.display = display;
		createComponents();
		refresh();
		hook();
	}

	private void createComponents() {
		this.setWidth("100%");
		this.setHeight("100%");
		this.bind();
	}

	private void refresh() {
		int selectedTab = 0, index = 0;

		this.clear();
		for (ViewDisplay viewDisplay : this.display.getViews()) {
            cosmos.gwt.presenters.Presenter.Builder builder = Builder.getNodeViewBuilder();
            if (!builder.canBuild(viewDisplay, "tab")) continue;

			if (this.display.isActiveView(viewDisplay))
				selectedTab = index;

			Widget widget = builder.build(viewDisplay, "tab", "tab");
            if (widget == null) continue;

            this.add(widget, viewDisplay.getLabel());
			index++;
		}

		this.selectTab(selectedTab);
	}

	private void hook() {
		this.display.addHook(new ViewListDisplay.Hook() {
			@Override
			public void activate(ViewDisplay viewDisplay) {
			}
		});
	}

	private void bind() {
		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {
		public static NodeViewWidget.Builder nodeViewBuilder;

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(ViewListDisplay.TYPE) && design.equals("tab");
		}

		@Override
		public com.google.gwt.user.client.ui.Widget build(Presenter presenter, String design, String layout) {
			createNodeViewBuilder();
			return new NodeViewListTabWidget((ViewListDisplay) presenter);
		}

		public static NodeViewWidget.Builder getNodeViewBuilder() {
			return nodeViewBuilder;
		}

		private void createNodeViewBuilder() {
			if (nodeViewBuilder != null) return;

			nodeViewBuilder = new NodeViewWidget.Builder();
			nodeViewBuilder.inject(translator);
			nodeViewBuilder.inject(theme);
		}

		private String getLayout(String layout) {
			return layout.isEmpty() ? "" : this.theme.getLayout(layout);
		}
	}
}
