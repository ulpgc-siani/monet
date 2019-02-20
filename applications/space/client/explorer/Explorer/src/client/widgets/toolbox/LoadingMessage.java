package client.widgets.toolbox;

import client.services.TranslatorService;
import com.google.gwt.user.client.ui.Label;

public class LoadingMessage extends Label {

	public LoadingMessage(TranslatorService translator) {
		super(translator.translate(TranslatorService.Label.LOADING));
		addStyleName(Style.LOADING);
	}

	private interface Style {
		String LOADING = "loading";
	}

}
