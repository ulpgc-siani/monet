package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.layers.DatastoreLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.CubeFact;
import org.monet.space.kernel.model.Datastore;

import java.util.Date;

public class ActionAddDatastoreCubeFact extends Action {

	public ActionAddDatastoreCubeFact() {
	}

	@Override
	public String execute() {
		String name = (String) this.parameters.get(Parameter.NAME);
		String cube = (String) this.parameters.get(Parameter.CUBE);
		String data = (String) this.parameters.get(Parameter.DATA);
		DatastoreLayer datastoreLayer = ComponentDatawareHouse.getInstance().getDatastoreLayer();

		Datastore datastore = datastoreLayer.load(name);
		CubeFact cubeFact = new CubeFact(new Date());
		cubeFact.deserializeFromXML(LibraryEncoding.decode(data), null);
		datastoreLayer.insertFact(datastore, datastore.getCube(cube), cubeFact);

		return MessageCode.DATASTORE_CUBE_FACT_ADDED;
	}

}
