package client.services.http.builders;

import client.core.system.DashboardView;
import client.core.system.NodeView;
import client.services.http.HttpInstance;

public class DashboardViewBuilder extends ViewBuilder<client.core.model.DashboardView> {
	@Override
	public client.core.model.DashboardView build(HttpInstance instance) {
		if (instance == null)
			return null;

		DashboardView view = new DashboardView() {
			@Override
			public ClassName getClassName() {
				return null;
			}
		};
		initialize(view, instance);
		return view;
	}

}
