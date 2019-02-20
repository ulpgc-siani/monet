package client.core.model.workmap;

import client.core.model.Instance;

public interface Place<T extends Action> extends Instance {

	ClassName CLASS_NAME = new ClassName("Place");

	enum Type {
		DELEGATION, SEND_JOB, LINE, EDITION, ENROLL, WAIT
	}

	String getCode();
	T getAction();
	void setAction(T action);

}