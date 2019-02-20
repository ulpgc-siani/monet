package client.presenters.operations;

import client.presenters.displays.ApplicationDisplay;
import client.presenters.displays.LayoutDisplay;
import client.presenters.displays.MessageDisplay;
import client.services.Services;

public abstract class Operation extends cosmos.presenters.Operation {
	protected Services services;
	protected String label;

	public static final Type TYPE = new Type("Operation", cosmos.presenters.Operation.TYPE);

	protected Operation(Context context) {
		super(context);
	}

	@Override
	protected void beforeExecute() {
		getMessageDisplay().hideMessage();
	}

	public void inject(Services services) {
		this.services = services;
	}

	public String getName() {
		return this.getType().toString().toLowerCase().replace("operation", "");
	}

	public abstract String getDefaultLabel();

	public String getMenuLabel(boolean fullLabel) {
		return getLabel();
	}

	public MessageDisplay getMessageDisplay() {

		if (context.getCanvas() != null)
			return ((ApplicationDisplay)context.getCanvas().getRootDisplay()).getMessageDisplay();

		return ((ApplicationDisplay)getRootDisplay()).getMessageDisplay();
	}

	public LayoutDisplay getLayoutDisplay() {

		if (context.getCanvas() != null)
			return ((ApplicationDisplay)context.getCanvas().getRootDisplay()).getLayoutDisplay();

		return ((ApplicationDisplay)getRootDisplay()).getLayoutDisplay();
	}

	public String getLabel() {
		if (label != null)
			return label;

		return getDefaultLabel();
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
