package client.presenters.operations;

import client.core.model.Entity;
import client.core.model.View;
import client.presenters.displays.EntityDisplay;
import client.presenters.displays.view.ViewDisplay;
import cosmos.presenters.Presenter;

public abstract class RefreshViewOperation<E extends Entity, V extends View> extends Operation {
	protected E entity;

	public static final Type TYPE = new Type("RefreshViewOperation", cosmos.presenters.Operation.TYPE);

	protected RefreshViewOperation(Context context, E entity) {
		super(context);
		this.entity = entity;
	}

	public E getEntity() {
		return entity;
	}

	protected abstract V getViewToRefresh();

	protected void refresh(E entity) {
		refreshCanvas(entity);
	}

	private void refreshCanvas(E entity) {
		ViewDisplay viewDisplay = new ViewDisplay.Builder().build(entity, getViewToRefresh());
		Type type = EntityDisplay.TYPE;
		Presenter display = context.getCanvas();

		if (display.existsChild(type))
			display.removeChild(type);

		display.addChild(viewDisplay);

		this.executePerformed();
	}
}
