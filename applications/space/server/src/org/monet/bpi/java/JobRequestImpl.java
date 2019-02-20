package org.monet.bpi.java;

import org.monet.bpi.JobRequest;
import org.monet.bpi.Schema;
import org.monet.bpi.types.Location;
import org.monet.bpi.types.location.GeometryFactory;
import org.monet.space.kernel.machines.ttm.model.Message;

public class JobRequestImpl extends ProviderRequestImpl implements JobRequest {

	public JobRequestImpl(Message message) {
		super(message);
	}

	@Override
	public void setLabel(String label) {
		this.message.setSubject(label);
	}

	@Override
	public void setLocation(Location location) {
		org.monet.space.kernel.model.map.Location locationImpl = new org.monet.space.kernel.model.map.Location();
		locationImpl.setGeometry(GeometryFactory.extract(location.getGeometry()));
		locationImpl.setDescription(location.getLabel());
		locationImpl.setCreateDate(location.getTimestamp());
		this.message.setLocation(locationImpl);
	}

	@Override
	public void setDefault(Schema schema) {
		this.message.setDefaultValues(schema);
	}

}
