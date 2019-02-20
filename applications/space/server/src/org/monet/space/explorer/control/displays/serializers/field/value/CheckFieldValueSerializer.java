package org.monet.space.explorer.control.displays.serializers.field.value;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.CheckFieldProperty;
import org.monet.metamodel.TermProperty;
import org.monet.metamodel.ThesaurusDefinition;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Indicator;

import java.util.List;

public class CheckFieldValueSerializer extends FieldValueSerializer {

	public CheckFieldValueSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(Field field, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();
		result.add("terms", serializeCheckList(field));
		return result;
	}

	private JsonObject serializeCheckList(Field field) {
		Attribute attribute = field.getAttribute();
		JsonArray result = new JsonArray();

		if (attribute == null)
			return toListObject(result);

		for (Attribute optionAttribute : attribute.getAttributeList().get().values())
			result.add(serializeCheck(field, optionAttribute));

		return toListObject(result);
	}

	private JsonObject serializeCheck(Field field, Attribute attribute) {
		JsonObject result = new JsonObject();
		String checked = getIndicatorValue(attribute, Indicator.CHECKED);
		String code = getIndicatorValue(attribute, Indicator.CODE);

		result.addProperty("value", code);
		result.addProperty("label", getIndicatorValue(attribute, Indicator.VALUE));
		result.addProperty("type", getCheckType(field, code));
		result.addProperty("checked", checked.equals("true") || checked.equals("yes"));

		return result;
	}

	private String getCheckType(Field field, String code) {
		CheckFieldProperty fieldDefinition = field.getFieldDefinition();

		if (isFilledViaBpi(fieldDefinition)) {
			return "none";
		}

		TermProperty term = findTermProperty(code, getTermProperties(fieldDefinition));

		if (term == null)
			return "none";

		if (term.getTermPropertyList().size() <= 0)
			return "check";

		if (term.isCategory())
			return "category";

		return "super_check";
	}

	private boolean isFilledViaBpi(CheckFieldProperty definition) {
		return definition.getSource() == null && (definition.getTerms() == null || definition.getTerms().getTermPropertyList().isEmpty());
	}

	private List<TermProperty> getTermProperties(CheckFieldProperty fieldDefinition) {
		if (fieldDefinition.getSource() == null)
			return fieldDefinition.getTerms().getTermPropertyList();
		ThesaurusDefinition definition = helper.getDictionary().getThesaurusDefinition(fieldDefinition.getSource().getValue());
		return definition.getTerms().getTermPropertyList();
	}

	private TermProperty findTermProperty(String key, List<TermProperty> terms) {

		for (TermProperty child : terms) {
			if (child.getKey().equals(key))
				return child;

			TermProperty foundInChildren = findTermProperty(key, child.getTermPropertyList());
			if (foundInChildren != null)
				return foundInChildren;
		}

		return null;
	}
}
