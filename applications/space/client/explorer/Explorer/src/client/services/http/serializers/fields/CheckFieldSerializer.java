package client.services.http.serializers.fields;

import client.core.model.definition.entity.field.CheckFieldDefinition;
import client.core.model.fields.CheckField;
import client.core.model.types.Check;
import client.core.model.types.CheckList;
import client.core.model.types.CompositeCheck;
import com.google.gwt.json.client.*;

public class CheckFieldSerializer extends AbstractFieldSerializer<CheckField, CheckFieldDefinition, CheckList> {
	@Override
	protected JSONValue serializeValue(CheckField field) {
		JSONObject result = new JSONObject();

		result.put("terms", serializeCheckList(flattenList(field.getValue())));

		if (field.getSource() != null)
			result.put("source", new JSONString(field.getSource().getId()));

		return result;
	}

	private CheckList flattenList(CheckList list) {
		CheckList result = new client.core.system.types.CheckList();
		if (list == null) return result;
		for (Check check : list) {
			result.add(check);
			if (!check.isLeaf()) {
				result.addAll(flattenList(((CompositeCheck)check).getChecks()));
			}
		}
		return result;
	}

	private JSONArray serializeCheckList(CheckList checkList) {
		JSONArray result = new JSONArray();
		int pos = 0;

		for (Check check : checkList) {
			result.set(pos++, serializeCheck(check));
		}

		return result;
	}

	private JSONObject serializeCheck(Check check) {
		JSONObject instance = new JSONObject();
		instance.put("value", new JSONString(check.getValue()));
		instance.put("label", new JSONString(check.getLabel()));
		instance.put("checked", JSONBoolean.getInstance(check.isChecked()));
		return instance;
	}
}
