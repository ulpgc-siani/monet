package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.bpi.JobSetup;
import org.monet.bpi.types.Date;

public class JobSetupImpl implements JobSetup {

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
