package client.presenters.displays.entity;

import client.core.model.Form;
import client.core.model.FormView;

public class FormDisplay extends NodeDisplay<Form, FormView> {

	public static final Type TYPE = new Type("FormDisplay", NodeDisplay.TYPE);

	public FormDisplay(Form form, FormView view) {
		super(form, view);
	}

	@Override
	protected void onInjectServices() {
	}

	@Override
	public Type getType() {
		return TYPE;
	}
}
