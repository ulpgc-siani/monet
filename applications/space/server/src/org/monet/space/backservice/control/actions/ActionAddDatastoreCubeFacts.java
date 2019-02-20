package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.layers.DatastoreLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.CubeFactList;
import org.monet.space.kernel.model.Datastore;

public class ActionAddDatastoreCubeFacts extends Action {

	public ActionAddDatastoreCubeFacts() {
	}

	@Override
	public String execute() {
		String name = (String) this.parameters.get(Parameter.NAME);
		String cube = (String) this.parameters.get(Parameter.CUBE);
		String data = (String) this.parameters.get(Parameter.DATA);
		DatastoreLayer datastoreLayer = ComponentDatawareHouse.getInstance().getDatastoreLayer();

		Datastore datastore = datastoreLayer.load(name);
		CubeFactList cubeFactList = new CubeFactList();
		cubeFactList.deserializeFromXML(LibraryEncoding.decode(data), false);
		datastoreLayer.insertFacts(datastore, datastore.getCube(cube), cubeFactList);

		return MessageCode.DATASTORE_CUBE_FACT_ADDED;
	}

}
