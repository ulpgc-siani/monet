package org.monet.space.kernel.model.map;

import java.io.OutputStream;


public interface MapLayerWriter {
	public void init(OutputStream output);

	public void close();

	public String getResultLayer();

	public void addLocation(Location location);

	public void addLocationList(LocationList locationList);
}
