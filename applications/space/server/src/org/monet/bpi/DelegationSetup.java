package org.monet.bpi;

import org.monet.bpi.types.Date;

public interface DelegationSetup {

	void cancel();

	void withDefaultValues(Date suggestedStartDate, Date suggestedEndDate, String comments, boolean urgent);

	void withValues(Date suggestedStartDate, Date suggestedEndDate, String comments, boolean urgent);

}
