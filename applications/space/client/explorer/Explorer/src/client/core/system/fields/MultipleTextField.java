package client.core.system.fields;

import client.core.model.definition.entity.field.TextFieldDefinition;
import client.core.model.List;
import client.core.model.fields.TextField;

public class MultipleTextField extends MultipleField<TextField, TextFieldDefinition, String> implements client.core.model.fields.MultipleTextField {

	public MultipleTextField() {
		super(Type.MULTIPLE_TEXT);
	}

	public MultipleTextField(String code, String label, List<TextField> fieldList) {
		super(code, label, Type.MULTIPLE_TEXT, fieldList);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.fields.MultipleTextField.CLASS_NAME;
	}

	@Override
	public ClassName getClassNameOfValue() {
		return client.core.model.fields.TextField.CLASS_NAME;
	}

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public String getValueAsString() {
		return "";
	}

	@Override
	public void setValue(String s) {
	}

	@Override
	public boolean metaDataIsValid(String value) {
		if (value.isEmpty() || getDefinition() == null || getDefinition().getPatterns() == null || getDefinition().getPatterns().isEmpty()) return true;
		for (TextFieldDefinition.PatternDefinition patternDefinition : getDefinition().getPatterns())
			if (value.matches(patternDefinition.getRegExp()))
				return true;
		return false;
	}
}
