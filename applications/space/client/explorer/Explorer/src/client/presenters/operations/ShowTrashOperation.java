package client.presenters.operations;

import client.core.model.Entity;
import client.core.model.View;
import client.services.TranslatorService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowTrashOperation extends ShowOperation<Entity, View> {

	public static final Type TYPE = new Type("ShowTrashOperation", Operation.TYPE);

	public ShowTrashOperation(Context context, View view) {
		super(context, null, view);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called.");
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getMenuLabel(boolean fullLabel) {
		TranslatorService service = services.getTranslatorService();
		return service.translate(TranslatorService.Label.TRASH);
	}

	@Override
	public String getDefaultLabel() {
		TranslatorService service = services.getTranslatorService();
		return service.translate(TranslatorService.OperationLabel.SHOW_TRASH);
	}

	@Override
	public String getDescription() {
		return null;
	}
}
