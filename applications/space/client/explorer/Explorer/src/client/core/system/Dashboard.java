package client.core.system;

import client.core.model.definition.entity.DashboardDefinition;

public class Dashboard<Definition extends DashboardDefinition> extends Entity<Definition> implements client.core.model.Dashboard<Definition> {

	@Override
	public final ClassName getClassName() {
		return client.core.model.Dashboard.CLASS_NAME;
	}

}
