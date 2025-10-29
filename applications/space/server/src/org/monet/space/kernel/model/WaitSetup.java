package org.monet.space.kernel.model;

import org.monet.bpi.types.Date;
import org.monet.metamodel.internal.Time;
import org.monet.space.kernel.library.LibraryDate;

public class WaitSetup {

	private boolean executed;
	private boolean canceled;
	private long timerDue;
	private Time timeAfter;

	public WaitSetup(long timeDue) {
		this.executed = false;
		this.canceled = false;
		this.timerDue = timeDue;
		this.timeAfter = null;
	}

	public void cancel() {
		checkExecuted();

		this.canceled = true;
	}

	public void withDate(Date date) {
		checkExecuted();

		if (date == null)
			return;

		this.timerDue = date.getValue().getTime();
		this.timeAfter = new Time(0);
	}

	private void withMilliseconds(long value) {
		checkExecuted();

		long timerDue = this.getTimerDue();
		long timeLeft = timerDue + value;

		if (timeLeft < 0)
			timeLeft = 0;

		this.timeAfter = new Time(timeLeft);
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

	public void withWeeks(int weeksLeft) {
		this.withMilliseconds(LibraryDate.weeksToMillis(weeksLeft));
	}

	public void withMonths(int monthsLeft) {
		this.withMilliseconds(LibraryDate.monthsToMillis(monthsLeft));
	}

	public void withYears(int yearsLeft) {
		this.withMilliseconds(LibraryDate.yearsToMillis(yearsLeft));
	}

	private void checkExecuted() {
		if (this.executed)
			throw new RuntimeException("Wait already setup");

		this.executed = true;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public long getTimerDue() {

		if (this.timerDue == -1)
			return new java.util.Date().getTime();

		return this.timerDue;
	}

	public Time getTimeAfter() {
		return timeAfter;
	}
}
