package org.monet.bpi;

public interface TimeoutSetup {
	void withSeconds(int seconds);

	void withMinutes(int minutes);

	void withHours(int hours);

	void withDays(int days);
}
