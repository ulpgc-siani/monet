package client.widgets.entity.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import cosmos.gwt.widgets.CosmosHtmlPanel;

import java.util.ArrayList;
import java.util.List;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class FieldLabel extends CosmosHtmlPanel {

	private final List<LabelHandler> clickLabelHandlers = new ArrayList<>();
	private final List<LabelHandler> clickDescriptionHandlers = new ArrayList<>();
	private HTMLPanel label;
	private GQuery description;
	private GQuery warning;

	public FieldLabel(String layout, String label) {
		super(layout);
		create(label);
		bindKeepingStyles(this.label, toRule(StyleName.LABEL));
	}

	public void addOnClickLabelHandler(LabelHandler handler) {
		clickLabelHandlers.add(handler);
	}

	public void addOnClickDescriptionHandler(LabelHandler handler) {
		clickDescriptionHandlers.add(handler);
	}

	private void create(String label) {
		createLabel(label);
		createDescription();
		createWarning();
	}

	private void createLabel(String label) {
		this.label = new HTMLPanel(label);
		this.label.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				FieldLabel.this.notify(FieldLabel.this.clickLabelHandlers);
			}
		}, ClickEvent.getType());
	}

	private void createDescription() {
		description = $(getElement()).find(toRule(StyleName.DESCRIPTION)).hide();
		this.description.click(new Function() {
			@Override
			public void f() {
				FieldLabel.this.notify(clickDescriptionHandlers);
			}
		});
	}

	private void notify(List<LabelHandler> handlers) {
		for (LabelHandler handler : handlers) {
			handler.handle();
		}
	}

	public void showDescription() {
		description.show();
	}

	public void hideDescription() {
		description.hide();
	}

	public void isRequired() {
		InlineHTML required = new InlineHTML("*");
		required.addStyleName(StyleName.REQUIRED);
		label.add(required);
	}

	public void setDescription(String description) {
		if (description == null || description.isEmpty()) return;
		this.description.attr("title", description);
	}

	public void showWarning(String message) {
		if (message != null && !message.isEmpty())
			warning.show().attr("title", message);
		else
			hideWarning();
	}

	public void hideWarning() {
		warning.hide();
	}

	private void createWarning() {
		warning = $(getElement()).find(toRule(StyleName.WARNING)).hide();
	}

	private interface StyleName {
		String DESCRIPTION = "description";
		String LABEL = "label";
		String REQUIRED = "required";
		String WARNING = "warning";
	}

	public interface LabelHandler {
		void handle();
	}
}
