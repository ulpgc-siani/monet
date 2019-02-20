package client.core.system.types;

public class Term implements client.core.model.types.Term {
	private String value;
	private String label;
    private String flattenLabel;

    public Term() {
	}

	public Term(String value, String label) {
		this.value = value;
		this.label = label;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

    public String getFlattenLabel() {
        if (flattenLabel == null || flattenLabel.isEmpty())
            return label;
        return flattenLabel;
    }

    public void setFlattenLabel(String flattenLabel) {
        this.flattenLabel = flattenLabel;
    }

	@Override
	public boolean equals(Object term) {
		if (!(term instanceof client.core.model.types.Term))
			return false;

		return ((Term)term).getValue().equals(getValue());
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

	@Override
	public boolean isLeaf() {
		return true;
	}
}
