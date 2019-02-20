package org.monet.bpi.java;

import org.monet.bpi.WaitSetup;
import org.monet.bpi.types.Date;

public class WaitSetupImpl implements WaitSetup {

	private org.monet.space.kernel.model.WaitSetup wrapped;

	void inject(org.monet.space.kernel.model.WaitSetup wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public void cancel() {
		this.wrapped.cancel();
	}

	@Override
	public void withDate(Date date) {
		this.wrapped.withDate(date);
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

	@Override
	public void withWeeks(int weeks) {
		this.wrapped.withWeeks(weeks);
	}

	@Override
	public void withMonths(int months) {
		this.wrapped.withMonths(months);
	}

	@Override
	public void withYears(int years) {
		this.wrapped.withYears(years);
	}

}
