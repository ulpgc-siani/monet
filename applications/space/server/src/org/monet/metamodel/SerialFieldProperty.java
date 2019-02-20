package org.monet.metamodel;

import org.monet.metamodel.interfaces.IsInitiable;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.library.LibrarySequence;
import org.monet.space.kernel.model.SequenceValue;

import java.util.regex.Matcher;

// SerialFieldDeclaration
// Declaración que se utiliza para modelar un	campo serial

public class SerialFieldProperty extends SerialFieldPropertyBase implements IsInitiable {

	private String REGEXP_SEQUENCE = "%([^S]*)S";
	private String REGEXP_YEAR = "%([^SY]*)Y";

	private Matcher sequenceMatcher;
	private Matcher yearMatcher;
	private boolean isInitialized = false;

	public void init() {
		this.parseFormat();
		this.isInitialized = true;
	}

	private void parseFormat() {
		Matcher sequenceMatcher = java.util.regex.Pattern.compile(REGEXP_SEQUENCE).matcher(this.getSerial().getFormat());
		Matcher yearMatcher = java.util.regex.Pattern.compile(REGEXP_YEAR).matcher(this.getSerial().getFormat());

		if (sequenceMatcher.find())
			this.sequenceMatcher = sequenceMatcher;
		else
			this.sequenceMatcher = null;
		if (yearMatcher.find())
			this.yearMatcher = yearMatcher;
		else
			this.yearMatcher = null;
	}

	public boolean hasSequencePart() {
		return java.util.regex.Pattern.compile(REGEXP_SEQUENCE).matcher(this.getSerial().getFormat()).find();
	}

	public boolean hasYearPart() {
		return java.util.regex.Pattern.compile(REGEXP_YEAR).matcher(this.getSerial().getFormat()).find();
	}

	public String format(SequenceValue sequenceValue) {
		if (!isInitialized)
			this.init();
		String result = this.getSerial().getFormat();
		Integer sequenceLength, yearLength;

		try {

			if (this.sequenceMatcher != null && this.sequenceMatcher.groupCount() == 1 && this.sequenceMatcher.group(1) != null) {
				sequenceLength = Integer.valueOf(this.sequenceMatcher.group(1));
				result = result.replace(this.sequenceMatcher.group(0), LibrarySequence.fillWithZeros(sequenceValue.getValue(), sequenceLength));
			}

			if (this.yearMatcher != null && this.yearMatcher.groupCount() == 1 && this.yearMatcher.group(1) != null) {
				yearLength = Integer.valueOf(this.yearMatcher.group(1));
				result = result.replace(this.yearMatcher.group(0), LibraryDate.getCurrentYear(yearLength));
			}
		} catch (Exception exception) {
			AgentLogger.getInstance().errorInModel("Error in field '" + this._name + "'", exception);
			return result;
		}

		return result;
	}

}