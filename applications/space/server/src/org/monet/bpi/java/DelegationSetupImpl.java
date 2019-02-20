package org.monet.bpi.java;

import org.monet.bpi.DelegationSetup;
import org.monet.bpi.types.Date;

public class DelegationSetupImpl implements DelegationSetup {

	private org.monet.space.kernel.model.DelegationSetup wrapped;

	void inject(org.monet.space.kernel.model.DelegationSetup wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public void cancel() {
		this.wrapped.cancel();
	}

	@Override
	public void withDefaultValues(Date suggestedStartDate, Date suggestedEndDate, String comments, boolean urgent) {
		this.wrapped.withDefaultValues(suggestedStartDate.getValue(), suggestedEndDate.getValue(), comments, urgent);
	}

	@Override
	public void withValues(Date suggestedStartDate, Date suggestedEndDate, String comments, boolean urgent) {
		this.wrapped.withValues(suggestedStartDate.getValue(), suggestedEndDate.getValue(), comments, urgent);
	}

}
