package client.core.model;

import client.core.model.definition.entity.ActivityDefinition;

public interface Activity<Definition extends ActivityDefinition> extends Process<Definition> {

	ClassName CLASS_NAME = new ClassName("Activity");

	ClassName getClassName();

}
