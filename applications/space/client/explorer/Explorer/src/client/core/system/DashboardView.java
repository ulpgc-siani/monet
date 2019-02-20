package client.core.system;

public abstract class DashboardView extends View implements client.core.model.DashboardView {

	public DashboardView() {
	}

	public DashboardView(client.core.model.Key key, String label, boolean isDefault) {
		super(key, label, isDefault);
	}

}
