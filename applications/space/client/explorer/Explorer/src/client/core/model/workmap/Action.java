package client.core.model.workmap;

import client.core.model.definition.entity.PlaceDefinition;
import client.core.model.Instance;

public interface Action<Definition extends PlaceDefinition.ActionDefinition> extends Instance {

	ClassName CLASS_NAME = new ClassName("Action");

	enum Type {
		DELEGATION, SEND_JOB, LINE, EDITION, ENROLL, WAIT;

		public static Type fromString(String type) {
			return Type.valueOf(type.toUpperCase());
		}
	}

	Definition getDefinition();
	void setDefinition(Definition definition);

}