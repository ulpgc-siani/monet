package org.monet.bpi.java;

import org.monet.bpi.ContestantRequest;
import org.monet.space.kernel.machines.ttm.model.Message;

public class ContestantRequestImpl extends ProviderResponseImpl implements ContestantRequest {

	public ContestantRequestImpl(Message message) {
		super(message);
	}

}
