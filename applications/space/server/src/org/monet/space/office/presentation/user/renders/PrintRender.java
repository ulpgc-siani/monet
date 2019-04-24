package org.monet.space.office.presentation.user.renders;

import java.util.Date;

public abstract class PrintRender extends OfficeRender {
	private Range range;

	protected static final int WordLengthOffset = 10;

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

	int getWordLength(String label) {
		if (label == null)
			return 0;

		String[] labelArray = label.split(" ");
		int result = 0;
		for (int i=0; i<labelArray.length; i++) {
			if (labelArray[i].length() > result)
				result = labelArray[i].length();
		}

		return result + WordLengthOffset;
	}

}
