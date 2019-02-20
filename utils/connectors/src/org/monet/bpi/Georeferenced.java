package org.monet.bpi;

import org.monet.bpi.types.Location;

public interface Georeferenced {

	public Location getLocation();

	public void setLocation(Location location);

}
