package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.bpi.DelegationSetup;
import org.monet.bpi.types.Date;

public class DelegationSetupImpl implements DelegationSetup {

	@Override
	public void cancel() {
		throw new NotImplementedException();
	}

	@Override
	public void withDefaultValues(Date suggestedStartDate, Date suggestedEndDate, String comments, boolean urgent) {
		throw new NotImplementedException();
	}

	@Override
	public void withValues(Date suggestedStartDate, Date suggestedEndDate, String comments, boolean urgent) {
		throw new NotImplementedException();
	}

}
