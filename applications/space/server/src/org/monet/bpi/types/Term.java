package org.monet.bpi.types;

import org.apache.commons.lang.NotImplementedException;
import org.monet.bpi.Source;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

import java.util.LinkedHashSet;

public class Term {

	public static final String CODE = "code";
	public static final int TERM = 0;
	public static final int SUPER_TERM = 1;
	public static final int CATEGORY = 2;

	@Attribute(name = CODE)
	private String key;

	@Text(required = false)
	private String label;

	private String sourceCode;
	private Source source;
	private LinkedHashSet<String> tagsSet;
	private int type;

	public Term() {
		this(null, null, null);
	}

	public Term(Term term) {
		this(term.key, term.label);
	}

	public Term(String key) {
		this(key, null, null);
	}

	public Term(String key, String label) {
		this(key, label, null);
	}

	public Term(String key, String label, String source) {
		this.key = key;
		this.label = label;
		this.sourceCode = source;
	}

	public void setKey(String code) {
		this.key = code;
	}

	public String getKey() {
		return key;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public LinkedHashSet<String> getTags() {
		if (this.tagsSet == null)
			this.tagsSet = new LinkedHashSet<>();
		return this.tagsSet;
	}

	public void addTag(String name) {
		if (this.tagsSet == null)
			this.tagsSet = new LinkedHashSet<>();
		this.tagsSet.add(name);
	}

	public void setSource(String source) {
		this.sourceCode = source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public String getSource() {
		return sourceCode;
	}

	public boolean equals(Term obj) {
		return this.key.equals(obj.key) && this.label.equals(obj.label);
	}

	public boolean isTerm(Source source) {
		return source.isTerm(this.key);
	}

	public boolean isSuperTerm(Source source) {
		return source.isTermSuperTerm(this.key);
	}

	public void setIsSuperTerm(Source source, boolean value) {
		this.type = value ? Term.SUPER_TERM : Term.TERM;
	}

	public boolean isCategory(Source source) {
		return source.isTermCategory(this.key);
	}

	public void setIsCategory(Source source, boolean value) {
		this.type = value ? Term.CATEGORY : Term.TERM;
	}

	public boolean isEnabled(Source source) {
		return source.isTermEnabled(this.key);
	}

	public boolean isDisabled(Source source) {
		return source.isTermDisabled(this.key);
	}

	public Term addTerm(Term term) {
		if (this.source == null)
			throw new NotImplementedException();

		this.source.addTerm(term, this);
		term.setSource(this.source);
		return term;
	}

	public Check toCheck() {
		Check check = new Check();
		check.setChecked(false);
		check.setCode(this.key);
		check.setLabel(this.label);
		check.setSource(this.sourceCode);
		return check;
	}

	@Override
	public String toString() {
		return this.label;
	}

}
