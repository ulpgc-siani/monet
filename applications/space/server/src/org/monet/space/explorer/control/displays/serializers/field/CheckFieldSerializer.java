package org.monet.space.explorer.control.displays.serializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.CheckFieldProperty;
import org.monet.space.explorer.control.displays.serializers.SourceSerializer;
import org.monet.space.explorer.control.displays.serializers.field.value.CheckFieldValueSerializer;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Source;

public class CheckFieldSerializer extends AbstractFieldSerializer<CheckFieldProperty> {

	@Override
	public JsonElement serializeValue(Field field, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) new CheckFieldValueSerializer(helper).serialize(field, jsonSerializationContext);
        Source source = locateSource(field.getAttribute(), field.<CheckFieldProperty>getFieldDefinition().getSource());

		if (source != null)
			result.add("source", new SourceSerializer(helper).serialize(source, source.getClass(), jsonSerializationContext));

		return result;
	}

	/* implementar en el cliente */
	/*
	private JsonObject serializeCheckList(Field field, JsonSerializationContext jsonSerializationContext) {
		Map<String, Boolean> checkedAttributes = loadCheckedTerms(field);
		TermList termList = loadTermList(field);
		JsonArray result = new JsonArray();
		boolean flatten = isFlatten(field);

		for (Term term : termList) {
			boolean checked = checkedAttributes.containsKey(term.getCode())?checkedAttributes.get(term.getCode()):false;

			if (checked)
				result.add(serializeCheck(term, checked, flatten));
		}

		return toListObject(result);
	}

	private boolean isFlatten(Field field) {
		CheckFieldProperty fieldDefinition = field.getFieldDefinition();
		CheckFieldProperty.SelectProperty selectDefinition = fieldDefinition.getSelect();

		return selectDefinition != null && selectDefinition.getFlatten() != null && selectDefinition.getFlatten().equals(CheckFieldProperty.SelectProperty.FlattenEnumeration.ALL);
	}

	private JsonElement serializeCheck(Term term, boolean checked, boolean flatten) {
		JsonObject result = new JsonObject();
		result.addProperty("value", term.getCode());
		result.addProperty("label", flatten ? term.getFlattenLabel() : term.getLabel());
		result.addProperty("checked", checked);
		return result;
	}

	private Map<String, Boolean> loadCheckedTerms(Field field) {
		Attribute attribute = field.getAttribute();
		Map<String, Boolean> result = new HashMap<>();

		if (attribute == null)
			return result;

		Attribute[] optionAttributes = attribute.getAttributeList().get().values().toArray(new Attribute[0]);
		for (Attribute optionAttribute : optionAttributes) {
			String code = getIndicatorValue(optionAttribute, Indicator.CODE);
			String checked = getIndicatorValue(optionAttribute, Indicator.CHECKED);
			result.put(code, (checked.equals("true") || checked.equals("yes")));
		}

		return result;
	}

	private TermList loadTermList(Field field) {
		Attribute attribute = field.getAttribute();
		CheckFieldProperty fieldDefinition = field.getFieldDefinition();
		TermList termList = new TermList();

		if (fieldDefinition.getTerms() != null)
			termList = loadTermListFromTerms(field);
		else if (fieldDefinition.getSource() != null)
			termList = loadTermListFromSource(field);
		else if (attribute != null)
			termList = new TermList(attribute.getAttributeList());

		return termList;
	}

	private TermList loadTermListFromTerms(Field field) {
		Attribute attribute = field.getAttribute();
		CheckFieldProperty fieldDefinition = field.getFieldDefinition();
		TermList termList = new TermList(fieldDefinition.getTerms().getTermPropertyList());

		if (termList.getCount() == 0)
			termList = new TermList(attribute!=null?attribute.getAttributeList():new AttributeList());

		return termList;
	}

	private TermList loadTermListFromSource(Field field) {
		Attribute attribute = field.getAttribute();
		CheckFieldProperty fieldDefinition = field.getFieldDefinition();
		CheckFieldProperty.SelectProperty selectDefinition = fieldDefinition.getSelect();
		Ref sourceRef = fieldDefinition.getSource();
		Source source = getSource(attribute, sourceRef);
		TermList termList;

		Node node = field.getNode();
		String from = this.getIndicatorValue(attribute, Indicator.FROM);
		if (from.isEmpty())
			from = selectDefinition != null ? getSourceFrom(node, selectDefinition.getRoot()) : this.getSourceFrom(node, null);

		DataRequest dataRequest = new DataRequest();
		dataRequest.addParameter(DataRequest.MODE, (selectDefinition != null && selectDefinition.getFlatten() != null && selectDefinition.getFlatten().equals(CheckFieldProperty.SelectProperty.FlattenEnumeration.ALL)) ? DataRequest.Mode.FLATTEN : DataRequest.Mode.TREE);
		dataRequest.addParameter(DataRequest.FLATTEN, (selectDefinition != null && selectDefinition.getFlatten() != null) ? selectDefinition.getFlatten().toString() : CheckFieldProperty.SelectProperty.FlattenEnumeration.NONE.toString());
		dataRequest.addParameter(DataRequest.DEPTH, (selectDefinition != null && selectDefinition.getDepth() != null) ? String.valueOf(selectDefinition.getDepth()) : null);
		dataRequest.addParameter(DataRequest.FROM, from);
		dataRequest.addParameter(DataRequest.FILTERS, SerializerData.serializeSet(getFilters(field, false)));

		if (source != null)
			termList = helper.loadSourceTerms(source.getId(), dataRequest);
		else
			termList = new TermList();

		return termList;
	}
*/
}
