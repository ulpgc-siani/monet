package client.core.model;

import client.core.model.definition.entity.DashboardDefinition;

public interface Dashboard<Definition extends DashboardDefinition> extends Entity<Definition> {

	ClassName CLASS_NAME = new ClassName("Dashboard");

	ClassName getClassName();

}
