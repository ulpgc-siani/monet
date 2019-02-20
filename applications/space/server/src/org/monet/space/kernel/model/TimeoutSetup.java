package org.monet.space.kernel.model;

import org.monet.metamodel.internal.Time;
import org.monet.space.kernel.library.LibraryDate;

public class TimeoutSetup {
	private Time timeAfter;

	public TimeoutSetup(Time timeAfter) {
		this.timeAfter = timeAfter;
	}

	private void withMilliseconds(long value) {
		this.timeAfter = new Time(value);
	}

	public void withSeconds(int secondsLeft) {
		this.withMilliseconds(LibraryDate.secondsToMillis(secondsLeft));
	}

	public void withMinutes(int minutesLeft) {
		this.withMilliseconds(LibraryDate.minutesToMillis(minutesLeft));
	}

	public void withHours(int hoursLeft) {
		this.withMilliseconds(LibraryDate.hoursToMillis(hoursLeft));
	}

	public void withDays(int daysLeft) {
		this.withMilliseconds(LibraryDate.daysToMillis(daysLeft));
	}

	public Time getTimeAfter() {
		return timeAfter;
	}
}
