package client.widgets.commands;

import client.presenters.operations.CompositeOperation;
import client.services.TranslatorService;
import com.google.gwt.user.client.ui.Anchor;

public class CompositeIconCommandWidget extends CompositeCommandWidget<Anchor> {

	public CompositeIconCommandWidget(CompositeOperation operation, TranslatorService translator, String layout) {
		super(operation, translator, new Anchor(), layout);
	}

	@Override
	protected void fillText() {
	}

}
