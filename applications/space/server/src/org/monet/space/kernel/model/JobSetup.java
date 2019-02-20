package org.monet.space.kernel.model;

import java.util.Date;

public class JobSetup {

	private boolean executed = false;

	private boolean canceled = false;
	private boolean withDefaultValues = false;
	private boolean withValues = false;

	private Date suggestedStartDate;
	private Date suggestedEndDate;
	private String comments;
	private boolean urgent;

	public void cancel() {
		checkExecuted();

		this.canceled = true;
	}

	public void withDefaultValues(Date suggestedStartDate, Date suggestedEndDate, String comments, boolean urgent) {
		checkExecuted();

		this.suggestedStartDate = suggestedStartDate;
		this.suggestedEndDate = suggestedEndDate;
		this.comments = comments;
		this.urgent = urgent;
		this.withDefaultValues = true;
	}

	public void withValues(Date suggestedStartDate, Date suggestedEndDate, String comments, boolean urgent) {
		checkExecuted();

		this.suggestedStartDate = suggestedStartDate;
		this.suggestedEndDate = suggestedEndDate;
		this.comments = comments;
		this.urgent = urgent;
		this.withValues = true;
	}

	private void checkExecuted() {
		if (this.executed)
			throw new RuntimeException("Job already setup");

		this.executed = true;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public boolean isWithDefaultValues() {
		return withDefaultValues;
	}

	public boolean isWithValues() {
		return withValues;
	}

	public Date getSuggestedStartDate() {
		return suggestedStartDate;
	}

	public Date getSuggestedEndDate() {
		return suggestedEndDate;
	}

	public String getComments() {
		return comments;
	}

	public boolean isUrgent() {
		return urgent;
	}

}
