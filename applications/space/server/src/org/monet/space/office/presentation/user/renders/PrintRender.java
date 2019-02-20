package org.monet.space.office.presentation.user.renders;

import java.util.Date;

public abstract class PrintRender extends OfficeRender {
	private Range range;

	public PrintRender() {
		super();
	}

	public final void setRange(Range range) {
		this.range = range;
	}

	public final Range getRange() {
		return this.range;
	}

	public abstract boolean isTimeConsumptionExcessive();

	public interface Range {
		String attribute();
		Date from();
		Date to();
	}

	protected int maxColumnPercentageWith(int countColumns) {
		return 100/countColumns;
	}

}
