package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.layers.DatastoreLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.Datastore;
import org.monet.space.kernel.model.DimensionComponent;

public class ActionAddDatastoreDimensionComponent extends Action {

	public ActionAddDatastoreDimensionComponent() {
	}

	@Override
	public String execute() {
		String name = (String) this.parameters.get(Parameter.NAME);
		String dimension = (String) this.parameters.get(Parameter.DIMENSION);
		String data = (String) this.parameters.get(Parameter.DATA);
		DatastoreLayer datastoreLayer = ComponentDatawareHouse.getInstance().getDatastoreLayer();

		Datastore datastore = datastoreLayer.load(name);
		DimensionComponent dimensionComponent = new DimensionComponent(dimension);
		dimensionComponent.deserializeFromXML(LibraryEncoding.decode(data), null);
		datastoreLayer.insertComponent(datastore, datastore.getDimension(dimension), dimensionComponent);

		return MessageCode.DATASTORE_DIMENSION_COMPONENT_ADDED;
	}

}
