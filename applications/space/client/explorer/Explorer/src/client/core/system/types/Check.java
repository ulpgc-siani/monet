package client.core.system.types;

public class Check implements client.core.model.types.Check {

	private Term term = new Term();
	private boolean checked;

	public Check() {
	}

	public Check(String value, String label, boolean checked) {
		setValue(value);
		setLabel(label);
		this.checked = checked;
	}

	@Override
	public String getValue() {
		return term.getValue();
	}

	@Override
	public void setValue(String value) {
		term.setValue(value);
	}

	@Override
	public String getLabel() {
		return term.getLabel();
	}

	@Override
	public void setLabel(String label) {
		term.setLabel(label);
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public boolean isChecked() {
		return checked;
	}

	@Override
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public final boolean equals(Object check) {
		return check instanceof Check && term.equals(((Check) check).term);
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}
}
