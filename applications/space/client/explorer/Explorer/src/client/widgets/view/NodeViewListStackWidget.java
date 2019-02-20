package client.widgets.view;

import client.presenters.displays.view.ViewDisplay;
import client.presenters.displays.view.ViewListDisplay;
import client.services.TranslatorService;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

public class NodeViewListStackWidget extends StackLayoutPanel {
	private final ViewListDisplay display;
	private final TranslatorService translator;

	public NodeViewListStackWidget(ViewListDisplay display, String layout, TranslatorService translator) {
		super(Style.Unit.PX);
		this.getElement().addClassName("stack");
		this.display = display;
		this.translator = translator;
		this.createComponents();
		this.refresh();
		this.hook();
	}

	private void createComponents() {
		this.setHeight("500px");
		this.setWidth("400px");
		this.bind();
	}

	private void refresh() {
		this.clear();

		for (ViewDisplay viewDisplay : this.display.getViews()) {
            cosmos.gwt.presenters.Presenter.Builder builder = Builder.getNodeViewBuilder();
            if (!builder.canBuild(viewDisplay, "stack")) continue;

			Widget widget = builder.build(viewDisplay, "stack", "stack");
            if (widget == null) continue;

            this.add(widget, viewDisplay.getLabel(), 25);
		}
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
			return presenter.is(ViewListDisplay.TYPE) && design.equals("stack");
		}

		@Override
		public com.google.gwt.user.client.ui.Widget build(Presenter presenter, String design, String layout) {
			this.createNodeViewBuilder();
			return new NodeViewListStackWidget((ViewListDisplay) presenter, getLayout(layout), translator);
		}

		private void createNodeViewBuilder() {
			if (nodeViewBuilder != null) return;

			nodeViewBuilder = new NodeViewWidget.Builder();
			nodeViewBuilder.inject(translator);
			nodeViewBuilder.inject(theme);
		}

		public static NodeViewWidget.Builder getNodeViewBuilder() {
			return nodeViewBuilder;
		}

		private String getLayout(String layout) {
			return layout.isEmpty() ? "" : this.theme.getLayout(layout);
		}
	}

}
