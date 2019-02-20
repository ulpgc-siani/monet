package org.monet.bpi.java;

import org.monet.bpi.ContestantResponse;
import org.monet.space.kernel.machines.ttm.model.Message;

public class ContestantResponseImpl extends ProviderRequestImpl implements ContestantResponse {

	public ContestantResponseImpl(Message message) {
		super(message);
	}

}
