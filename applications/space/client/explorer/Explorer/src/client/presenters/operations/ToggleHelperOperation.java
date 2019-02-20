package client.presenters.operations;

import client.presenters.displays.ApplicationDisplay;
import client.presenters.displays.HelperDisplay;
import client.services.TranslatorService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ToggleHelperOperation extends HelperOperation {

	public static final Type TYPE = new Type("ToggleHelperOperation", HelperOperation.TYPE);

	public ToggleHelperOperation(Context context) {
		super(context);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called.");

		ApplicationDisplay display = context.getCanvas();
		HelperDisplay helperDisplay = display.getHelperDisplay();

		if (helperDisplay.isVisible()) {
			hideHelper();
			return;
		}

		showHelper();
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(TranslatorService.OperationLabel.TOGGLE_HELPER);
	}

	private void hideHelper() {
		HideHelperOperation operation = new HideHelperOperation(context);
		operation.inject(services);
		operation.execute();
	}

	private void showHelper() {
		if (entity == null)
			entity = services.getAccountService().load().getRootNode();

		refresh();
	}
}
