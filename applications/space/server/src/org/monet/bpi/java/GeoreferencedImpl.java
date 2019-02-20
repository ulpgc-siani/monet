package org.monet.bpi.java;

import org.monet.bpi.types.Location;
import org.monet.bpi.types.location.GeometryFactory;
import org.monet.space.kernel.model.Node;

public class GeoreferencedImpl {

	public static Location getLocation(Node node) {
		if (node.getLocation() == null)
			return null;

		Location location = new Location();
		location.setGeometry(GeometryFactory.build(node.getLocation().getGeometry()));
		location.setTimestamp(node.getLocation().getInternalCreateDate());

		return location;
	}

	public static void setLocation(Node node, Location bpiLocation) {
		org.monet.space.kernel.model.map.Location location = node.getLocation();

		if (location == null)
			location = new org.monet.space.kernel.model.map.Location();

		location.setGeometry(GeometryFactory.extract(bpiLocation.getGeometry()));
		location.setCreateDate(bpiLocation.getTimestamp());
		node.setLocation(location);
	}

}
