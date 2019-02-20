package client.services.http.serializers.fields;

import client.core.model.Field;
import client.core.model.List;
import client.core.model.MultipleField;
import client.core.model.definition.entity.FieldDefinition;
import client.services.http.serializers.Serializer;
import com.google.gwt.json.client.*;

public abstract class AbstractFieldSerializer<F extends Field<FD, V>, FD extends FieldDefinition, V> implements Serializer<F, JSONValue, JSONArray> {

	@Override
	public JSONObject serialize(F element) {
		JSONObject result = new JSONObject();

		result.put("code", new JSONString(element.getCode()));
		result.put("type", new JSONString(getType(element)));
		result.put("multiple", JSONBoolean.getInstance(element.isMultiple()));

		if (element.isMultiple())
			result.put("value", serializeValueMultiple((MultipleField) element));
		else
			result.put("value", serializeValue(element));

		return result;
	}

	protected String getType(F element) {
		return element.getType().toString().replace("multiple_","");
	}

	protected abstract JSONValue serializeValue(F field);

	public <MF extends MultipleField<F, FD, V>> JSONArray serializeValueMultiple(MF field) {
		JSONArray result = new JSONArray();
		int pos = 0;

		for (F itemField : field.getAll()) {
			result.set(pos, serializeValue(itemField));
			pos++;
		}

		return result;
	}

	@Override
	public JSONArray serializeList(List<F> element) {
		return null;
	}
}
