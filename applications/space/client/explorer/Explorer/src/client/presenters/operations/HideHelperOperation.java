package client.presenters.operations;

import client.services.TranslatorService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HideHelperOperation extends HelperOperation {

	public static final Type TYPE = new Type("HideHelperOperation", HelperOperation.TYPE);

	public HideHelperOperation(Context context) {
		super(context);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		setEntity(null);
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called.");
		refresh();
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(TranslatorService.OperationLabel.HIDE_HELPER);
	}

}
