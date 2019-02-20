package client.widgets.commands;

import client.presenters.operations.Operation;
import client.services.TranslatorService;
import com.google.gwt.user.client.ui.Anchor;

public class IconCommandWidget extends CommandWidget<Operation, Anchor> {

	public IconCommandWidget(Operation operation, TranslatorService translator, String layout) {
		super(operation, translator, new Anchor(), layout);
        init();
	}

	@Override
	protected void fillText() {
	}
}
