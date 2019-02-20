package client.services.http.serializers.fields;

import client.core.model.definition.entity.field.SelectFieldDefinition;
import client.core.model.fields.SelectField;
import client.core.model.types.Term;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class SelectFieldSerializer extends AbstractFieldSerializer<SelectField, SelectFieldDefinition, Term> {
	@Override
	protected JSONValue serializeValue(SelectField field) {
		JSONObject result = new JSONObject();
		Term term = field.getValue();

		if (term != null) {
			result.put("value", new JSONString(term.getValue()));
			result.put("label", new JSONString(term.getLabel()));
			result.put("other", JSONBoolean.getInstance(field.termIsOther(term)));
		} else {
			result.put("value", new JSONString(""));
			result.put("label", new JSONString(""));
			result.put("other", JSONBoolean.getInstance(false));
		}

		if (field.getSource() != null)
			result.put("source", new JSONString(field.getSource().getId()));

		return result;
	}
}
