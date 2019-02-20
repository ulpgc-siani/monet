package client.core.model.workmap;

import client.core.model.Instance;

public interface WorkMap extends Instance {
	ClassName CLASS_NAME = new ClassName("WorkMap");

	Place getPlace();
}
