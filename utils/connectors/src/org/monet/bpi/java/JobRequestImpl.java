package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.bpi.JobRequest;
import org.monet.bpi.Schema;
import org.monet.bpi.types.Location;

public class JobRequestImpl extends ProviderRequestImpl implements JobRequest {

	@Override
	public void setLabel(String label) {
		throw new NotImplementedException();
	}

	@Override
	public void setLocation(Location location) {
		throw new NotImplementedException();
	}

	@Override
	public void setDefault(Schema schema) {
		throw new NotImplementedException();
	}

}
