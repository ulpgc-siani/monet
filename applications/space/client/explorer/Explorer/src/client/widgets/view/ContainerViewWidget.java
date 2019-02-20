package client.widgets.view;

import client.core.model.definition.views.ViewDefinition;
import client.presenters.displays.view.ContainerViewDisplay;
import client.services.TranslatorService;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

public class ContainerViewWidget extends HTMLPanel {

	public ContainerViewWidget(ContainerViewDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
	}

	private static String getHtml(String layout, TranslatorService translator) {
		return translator.translate(layout);
	}

	public static class Builder extends NodeViewWidget.Builder {
		public static final String LAYOUT = ViewDefinition.Design.LIST.toString();

		public static void register() {
			registerBuilder(ContainerViewDisplay.TYPE.toString(), new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new ContainerViewWidget((ContainerViewDisplay) presenter, getLayout("view-container", layout), translator);
		}
	}

}
