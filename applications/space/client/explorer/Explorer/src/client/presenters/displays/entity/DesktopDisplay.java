package client.presenters.displays.entity;

import client.core.model.Desktop;
import client.core.model.DesktopView;

public class DesktopDisplay extends NodeDisplay<Desktop, DesktopView> implements EnvironmentDisplay {

	public static final Type TYPE = new Type("DesktopDisplay", EnvironmentDisplay.TYPE);

	public DesktopDisplay(Desktop desktop, DesktopView view) {
		super(desktop, view);
	}

	@Override
	protected void onInjectServices() {
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public boolean isRoot() {
		return true;
	}
}
