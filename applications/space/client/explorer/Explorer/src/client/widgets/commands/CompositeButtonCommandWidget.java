package client.widgets.commands;

import client.presenters.operations.CompositeOperation;
import client.services.TranslatorService;
import com.google.gwt.user.client.ui.Button;

public class CompositeButtonCommandWidget extends CompositeCommandWidget<Button> {

	public CompositeButtonCommandWidget(CompositeOperation operation, TranslatorService translator, String layout) {
		super(operation, translator, new Button(), layout);
	}

	@Override
	protected void fillText() {
		component.setText(operation.getLabel());
	}
}
