package org.monet.bpi.java;

import org.monet.bpi.CustomerRequest;
import org.monet.space.kernel.machines.ttm.model.Message;

public class CustomerRequestImpl extends ProviderResponseImpl implements CustomerRequest {

	public CustomerRequestImpl(Message message) {
		super(message);
	}

}
