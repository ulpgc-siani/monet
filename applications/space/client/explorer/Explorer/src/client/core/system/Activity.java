package client.core.system;

import client.core.model.definition.entity.ActivityDefinition;

public class Activity<Definition extends ActivityDefinition> extends Process<Definition> implements client.core.model.Activity<Definition> {

	public Activity() {
	}

	public Activity(String id, String label) {
		super(id, label);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.Activity.CLASS_NAME;
	}

}
