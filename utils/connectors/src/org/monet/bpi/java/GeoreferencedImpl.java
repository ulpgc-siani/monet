package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.api.space.backservice.BackserviceApi;
import org.monet.api.space.backservice.impl.model.Node;
import org.monet.bpi.types.Location;
import org.monet.bpi.types.location.GeometryFactory;

public class GeoreferencedImpl {
	private static BackserviceApi api;

	public static void inject(BackserviceApi api) {
		GeoreferencedImpl.api = api;
	}

	public static Location getLocation(Node node) {
		Location location = new Location();
		org.monet.api.space.backservice.impl.model.Location monetLocation = api.getNodeLocation(node.getId());

		if (monetLocation == null)
			return null;

		location.setLabel(monetLocation.getLabel());
		location.setGeometry(GeometryFactory.build(monetLocation.getWkt()));
		location.setTimestamp(monetLocation.getTimestamp());

		return location;
	}

	public static void setLocation(Node node, Location bpilocation) {
		throw new NotImplementedException();
	}

}
