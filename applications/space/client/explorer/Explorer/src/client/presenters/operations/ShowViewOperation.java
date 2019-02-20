package client.presenters.operations;

import client.core.model.Showable;
import client.core.model.Entity;
import client.core.model.View;
import client.presenters.displays.view.ViewDisplay;
import cosmos.presenters.Presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowViewOperation extends ShowOperation {

	public static final Type TYPE = new Type("ShowViewOperation", Operation.TYPE);

	public ShowViewOperation(Context context, Entity entity, View view) {
		super(context, entity, view);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Entity: " + entity.getLabel() + ", View: " + view.getLabel());

		this.refreshCanvas(context.getReferral());
	}

	private void refreshCanvas(cosmos.presenters.Operation from) {
		Presenter presenter = new ViewDisplay.Builder().build(entity, view);
		Type type = presenter.getType();
		Presenter display = context.getCanvas();

		if (display.existsChild(type))
			display.removeChild(type);

		display.addChild(presenter);

		this.executePerformed();
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		return view.getLabel();
	}

	@Override
	public String getDescription() {
		return null;
	}

}
