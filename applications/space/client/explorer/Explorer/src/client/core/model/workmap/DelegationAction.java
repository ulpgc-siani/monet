package client.core.model.workmap;

import client.core.model.definition.entity.PlaceDefinition;
import client.core.model.Node;
import client.core.model.Role;
import cosmos.types.Date;

public interface DelegationAction extends Action<PlaceDefinition.DelegationActionDefinition> {

	enum Message {
		SETUP_ROLE, SENDING_FAILURE, FAILURE_INTERNAL, FAILURE_EXTERNAL, NO_NATURE, NO_ROLES, SETUP_ORDER, SENDING
	}

	ClassName CLASS_NAME = new ClassName("DelegationAction");

	enum Step {
		SETUP_ROLE, SETUP_ORDER, SENDING;

		@Override
		public String toString() {
			return super.toString().replace("_", "-").toLowerCase();
		}

		public static Step fromString(String step) {
			return Step.valueOf(step.toUpperCase());
		}
	}

	Date getFailureDate();
	Role getRole();
	Node getOrderNode();
	Step getStep();

}