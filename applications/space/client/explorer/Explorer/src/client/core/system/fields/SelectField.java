package client.core.system.fields;

import client.core.model.definition.entity.field.SelectFieldDefinition;
import client.core.model.Source;
import client.core.model.types.Term;

public class SelectField extends Field<SelectFieldDefinition, Term> implements client.core.model.fields.SelectField {
	private Term value;
	private Source source;

	public SelectField() {
		super(Type.SELECT);
	}

	public SelectField(String code, String label) {
		super(code, label, Type.SELECT);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.fields.SelectField.CLASS_NAME;
	}

	@Override
	public Term getValue() {
		return this.value;
	}

	@Override
	public String getValueAsString() {
		return getValue().getValue();
	}

	@Override
	public void setValue(Term value) {
		this.value = value;
	}

	@Override
	public final boolean isMultiple() {
		return false;
	}

	@Override
	public final boolean isNullOrEmpty() {
		Term value = this.getValue();
		return value == null || value.getValue() == null || value.getValue().isEmpty();
	}

	@Override
    public Source getSource() {
        return this.source;
    }

	@Override
	public void setSource(Source source) {
		this.source = source;
	}

	@Override
	public boolean termIsOther(Term term) {
		return !(getDefinition() == null || term == null) && getDefinition().getValueForTermOther().equals(term.getValue());
	}

}
