package client.presenters.operations;

import client.core.model.Dashboard;
import client.core.model.View;

public class ShowDashboardOperation extends ShowOperation<Dashboard, View> {

	public static final Type TYPE = new Type("ShowDashboardOperation", Operation.TYPE);

	public ShowDashboardOperation(Context context, Dashboard dashboard, View view) {
		super(context, dashboard, view);
	}

	@Override
	public String getMenuLabel(boolean fullLabel) {
		return null;
	}

	@Override
	public String getDefaultLabel() {
		return null;
	}

	@Override
	public void doExecute() {
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return false;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public String getDescription() {
		return getEntity().getLabel();
	}
}
