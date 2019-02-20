package org.monet.metamodel;

import org.monet.metamodel.interfaces.IsInitiable;

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

}