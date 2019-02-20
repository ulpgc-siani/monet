package org.monet.bpi.java;

import org.monet.bpi.CustomerResponse;
import org.monet.space.kernel.machines.ttm.model.Message;

public class CustomerResponseImpl extends ProviderRequestImpl implements CustomerResponse {

	public CustomerResponseImpl(Message message) {
		super(message);
	}

}
