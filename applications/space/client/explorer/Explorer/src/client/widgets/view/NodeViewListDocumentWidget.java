package client.widgets.view;

import client.presenters.displays.view.ViewDisplay;
import client.presenters.displays.view.ViewListDisplay;
import client.services.TranslatorService;
import com.google.gwt.user.client.ui.*;
import cosmos.presenters.Presenter;

public class NodeViewListDocumentWidget extends VerticalPanel {
	private final ViewListDisplay display;
	private final TranslatorService translator;

	public NodeViewListDocumentWidget(ViewListDisplay display, String layout, TranslatorService translator) {
		super();
		this.getElement().addClassName("document");
		this.display = display;
		this.translator = translator;
		this.createComponents();
		this.refresh();
		this.hook();
	}

	private void createComponents() {
		this.setWidth("100%");
		this.setHeight("100%");
		this.bind();
	}

	private void refresh() {
		this.clear();

		for (ViewDisplay viewDisplay : this.display.getViews()) {
			HTMLPanel container = new HTMLPanel("");
			container.setStyleName("section");
			this.add(container);

            cosmos.gwt.presenters.Presenter.Builder builder = Builder.getNodeViewBuilder();
            if (!builder.canBuild(viewDisplay, "document")) continue;

			Widget widget = builder.build(viewDisplay, "document", "document");
            if (widget == null) continue;

			container.add(new HTML("<div class='title'>" + viewDisplay.getLabel() + "</div>"));
			container.add(widget);
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
			return presenter.is(ViewListDisplay.TYPE) && design.equals("document");
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			createNodeViewBuilder();
			return new NodeViewListDocumentWidget((ViewListDisplay) presenter, getLayout(layout), translator);
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
