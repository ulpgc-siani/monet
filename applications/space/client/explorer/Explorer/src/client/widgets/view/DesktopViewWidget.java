package client.widgets.view;

import client.core.model.definition.views.ViewDefinition;
import client.presenters.displays.view.DesktopViewDisplay;
import client.presenters.operations.EntityOperation;
import client.presenters.operations.Operation;
import client.services.TranslatorService;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElementAndKeepStyles;

public class DesktopViewWidget extends HTMLPanel {
	private final DesktopViewDisplay display;
	private VerticalPanel operationsPanel;

	public DesktopViewWidget(DesktopViewDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		this.display = display;
		create();
		refresh();
	}

	private void create() {
		operationsPanel = new VerticalPanel();
		bind();
	}

	private void refresh() {
		refreshLabel();
		refreshOperations();
	}

	private void refreshLabel() {
		$(getElement()).find(toRule(StyleName.LABEL)).html(display.getLabel());
	}

	private void refreshOperations() {
		operationsPanel.clear();
		for (EntityOperation operation : display.getOperations())
			operationsPanel.add(createOperationPanel(operation));
	}

	private VerticalPanel createOperationPanel(EntityOperation operation) {
		VerticalPanel operationPanel = new VerticalPanel();
		operationPanel.addStyleName(StyleName.OPERATION);
		operationPanel.add(createLabel((Operation) operation));
		operationPanel.add(createDescription(operation));
		return operationPanel;
	}

	private void bind() {
		bindWidgetToElementAndKeepStyles(this, operationsPanel, $(getElement()).find(toRule(StyleName.OPERATIONS)).get(0));
		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private static String getHtml(String layout, TranslatorService translator) {
		return translator.translateHTML(layout);
	}

	private Anchor createLabel(final Operation operation) {
		Anchor operationAnchor = new Anchor(operation.getLabel());

		operationAnchor.addStyleName(StyleName.LABEL);
		operationAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				operation.execute();
			}
		});

		return operationAnchor;
	}

	private Widget createDescription(EntityOperation operation) {
		Label label = new Label(operation.getDescription());
		label.addStyleName(StyleName.DESCRIPTION);
		return label;
	}

	public static class Builder extends NodeViewWidget.Builder {
		public static final String LAYOUT = ViewDefinition.Design.LIST.toString();

		public static void register() {
			registerBuilder(DesktopViewDisplay.TYPE.toString(), new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new DesktopViewWidget((DesktopViewDisplay) presenter, getLayout("view-desktop", layout), translator);
		}
	}

	public interface StyleName {
		String LABEL = "label";
		String DESCRIPTION = "description";
		String OPERATIONS = "operations";
		String OPERATION = "operation";
	}
}
