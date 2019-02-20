package org.monet.bpi;

import org.monet.bpi.types.Date;

public interface WaitSetup {

	void cancel();

	void withDate(Date date);

	void withSeconds(int seconds);

	void withMinutes(int minutes);

	void withHours(int hours);

	void withDays(int days);

	void withWeeks(int weeks);

	void withMonths(int months);

	void withYears(int years);
}
