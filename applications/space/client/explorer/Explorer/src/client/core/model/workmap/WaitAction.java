package client.core.model.workmap;

import client.core.model.definition.entity.PlaceDefinition.WaitActionDefinition;
import cosmos.types.Date;

public interface WaitAction extends Action<WaitActionDefinition> {

	ClassName CLASS_NAME = new ClassName("WaitAction");

	enum Step {
		CONFIGURE, RE_CONFIGURE;

		@Override
		public String toString() {
			return super.toString().replace("_", "-").toLowerCase();
		}
	}

	enum Scale {
		HOUR, DAY, MONTH, YEAR;

		@Override
		public String toString() {
			return super.toString().replace("_", "-").toLowerCase();
		}
	}

	Date getDueDate();
	Step getStep();

}