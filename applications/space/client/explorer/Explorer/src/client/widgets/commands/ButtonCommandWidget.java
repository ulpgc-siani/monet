package client.widgets.commands;

import client.presenters.operations.Operation;
import client.services.TranslatorService;
import com.google.gwt.user.client.ui.Button;

public class ButtonCommandWidget extends CommandWidget<Operation, Button> {

	public ButtonCommandWidget(Operation operation, TranslatorService translator, String layout) {
		super(operation, translator, new Button(), layout);
        init();
	}

	@Override
	protected void fillText() {
		component.setText(operation.getLabel());
	}
}
