package client.core.model.definition.entity;

import client.core.model.definition.Definition;
import client.core.model.definition.Ref;

public interface PlaceDefinition extends Definition {
	ActionDefinition getAction();

	interface ActionDefinition extends Definition {
	}

	interface DelegationActionDefinition extends ActionDefinition {
		Ref getProvider();
	}

	interface LineActionDefinition extends ActionDefinition {
		TimeoutDefinition getTimeout();
		LineStopDefinition[] getStop();

		interface TimeoutDefinition {
			Ref getTake();
		}

		interface LineStopDefinition {
			String getCode();
			String getLabel();
		}

	}

	interface EditionActionDefinition extends ActionDefinition {
	}

	interface WaitActionDefinition extends ActionDefinition {
	}

}
