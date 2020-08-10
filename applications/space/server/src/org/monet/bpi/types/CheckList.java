package org.monet.bpi.types;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Collection;

@Root(name = "check-list")
public class CheckList {

	public static final String SOURCE = "source";

	@ElementList
	private ArrayList<Check> checkList = new ArrayList<Check>();

	@Attribute(name = SOURCE, required = false)
	private String source;

	public CheckList() {
	}

	public void clear() {
		this.checkList.clear();
	}

	public void add(Check check) {
		this.checkList.add(check);
	}

	public void add(boolean isChecked, String code, String label) {
		this.checkList.add(new Check(isChecked, code, label));
	}

	public Check get(int pos) {
		return this.checkList.get(pos);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Collection<Check> getAll() {
		return this.checkList;
	}

	public boolean isChecked(String code) {
		for (Check check : this.checkList) {
			if (check.getCode().equals(code))
				return check.isChecked();
		}
		return false;
	}

	public boolean equals(CheckList checkList) {
		boolean found;
		Collection<Check> checks = this.getAll();

		for (Check check : checkList.getAll()) {
			found = false;
			for (Check currentCheck : checks) {
				if (check.equals(currentCheck)) found = true;
			}
			if (!found) return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return toString(", ");
	}

	public String toString(String separator) {
		StringBuilder builder = new StringBuilder();
		for (Check value : getAll()) {
			builder.append(value.toString());
			builder.append(separator);
		}
		if (builder.length() > 0) {
			int length = builder.length();
			builder.delete(length - separator.length(), length);
		}
		return builder.toString();
	}

	public TermList toTermList() {
	    TermList termList = new TermList();
	    for (Check value : getAll()) {
	        termList.add(termOf(value));
        }
	    return termList;
    }

	private Term termOf(Check value) {
		Term term = new Term();
		term.setKey(value.getCode());
		term.setLabel(value.getLabel());
		return term;
	}
}
