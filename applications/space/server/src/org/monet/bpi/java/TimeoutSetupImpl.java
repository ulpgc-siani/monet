package org.monet.bpi.java;

import org.monet.bpi.TimeoutSetup;

public class TimeoutSetupImpl implements TimeoutSetup {

	private org.monet.space.kernel.model.TimeoutSetup wrapped;

	void inject(org.monet.space.kernel.model.TimeoutSetup wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public void withSeconds(int seconds) {
		this.wrapped.withSeconds(seconds);
	}

	@Override
	public void withMinutes(int minutes) {
		this.wrapped.withMinutes(minutes);
	}

	@Override
	public void withHours(int hours) {
		this.wrapped.withHours(hours);
	}

	@Override
	public void withDays(int days) {
		this.wrapped.withDays(days);
	}

}
