package client.core.model.workmap;

import client.core.model.definition.entity.PlaceDefinition.LineActionDefinition;
import cosmos.types.Date;

public interface LineAction extends Action<LineActionDefinition> {

	ClassName CLASS_NAME = new ClassName("LineAction");

	Date getDueDate();
	Stop getDueStop();
	Stop[] getStops();

	interface Stop {
		String getCode();
		String getLabel();
	}

}