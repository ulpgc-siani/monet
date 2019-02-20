package org.monet.bpi.types;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "option")
public class Check {

	public static final String IS_CHECKED = "is-checked";
	public static final String CODE = "code";

	@Attribute(name = IS_CHECKED)
	private boolean isChecked;
	@Attribute(name = CODE)
	private String code;
	@Text
	private String label;

	private String source;

	public Check() {
	}

	public Check(Check check) {
		this.isChecked = check.isChecked;
		this.code = check.code;
		this.label = check.label;
	}

	public Check(boolean isChecked, String code, String label) {
		this(isChecked, code, label, null);
	}

	public Check(boolean isChecked, String code, String label, String source) {
		this.isChecked = isChecked;
		this.code = code;
		this.label = label;
		this.source = source;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public Term toTerm() {
		Term term = new Term();
		term.setKey(this.code);
		term.setLabel(this.label);
		term.setSource(this.source);
		return term;
	}

	public boolean equals(Check check) {
		return this.isChecked() == check.isChecked() && this.getCode().equals(check.getCode()) && this.getLabel().equals(check.getLabel());
	}

	@Override
	public String toString() {
		return this.label;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
