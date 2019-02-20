package client.presenters.operations;

import client.core.model.Entity;
import client.presenters.displays.ApplicationDisplay;
import client.presenters.displays.HelperDisplay;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class HelperOperation extends Operation {
	protected Entity entity;

	public static final Type TYPE = new Type("HelperOperation", Operation.TYPE);

	public HelperOperation(Context context) {
		super(context);
	}

	public Entity getEntity() {
		return this.entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, getType().toString() + " called. Entity: " + entity.getLabel());

		refresh();
	}

	protected void refresh() {
		refreshCanvas();
		executePerformed();
	}

	protected void refreshCanvas() {
		ApplicationDisplay display = context.getCanvas();
		HelperDisplay helperDisplay = display.getHelperDisplay();

        if (display.existsChild(HelperDisplay.TYPE))
			display.removeChild(HelperDisplay.TYPE);

		display.addChild(helperDisplay);
		helperDisplay.setEntity(entity);
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}
}
