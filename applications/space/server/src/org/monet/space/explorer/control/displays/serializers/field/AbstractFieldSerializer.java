package org.monet.space.explorer.control.displays.serializers.field;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.*;
import org.monet.metamodel.internal.Ref;
import org.monet.space.explorer.control.displays.serializers.ExplorerSerializer;
import org.monet.space.kernel.model.*;

import java.util.*;

public abstract class AbstractFieldSerializer<T extends FieldProperty> extends ExplorerSerializer implements FieldSerializer<T> {

	public AbstractFieldSerializer() {
		super(null);
	}

	public void setHelper(Helper helper) {
		this.helper = helper;
	}

	private static Map<Class<? extends FieldProperty>, FieldSerializer> fieldSerializers = new HashMap<Class<? extends FieldProperty>, FieldSerializer>() {{
		put(BooleanFieldProperty.class, new BooleanFieldSerializer());
		put(CheckFieldProperty.class, new CheckFieldSerializer());
		put(DateFieldProperty.class, new DateFieldSerializer());
		put(FileFieldProperty.class, new FileFieldSerializer());
		put(LinkFieldProperty.class, new LinkFieldSerializer());
		put(SummationFieldProperty.class, new SummationFieldSerializer());
		put(MemoFieldProperty.class, new MemoFieldSerializer());
		put(NodeFieldProperty.class, new NodeFieldSerializer());
		put(NumberFieldProperty.class, new NumberFieldSerializer());
		put(PictureFieldProperty.class, new FileFieldSerializer());
		put(CompositeFieldProperty.class, new CompositeFieldSerializer());
		put(SelectFieldProperty.class, new SelectFieldSerializer());
		put(SerialFieldProperty.class, new SerialFieldSerializer());
		put(TextFieldProperty.class, new TextFieldSerializer());
		put(UriFieldProperty.class, new UriFieldSerializer());
	}};

	@Override
	public JsonElement serialize(Field field, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();
		FieldProperty fieldDefinition = field.getFieldDefinition();

		result.addProperty("code", fieldDefinition.getCode());
		result.addProperty("type", fieldDefinition.getType());

		if (fieldDefinition.isMultiple())
			result.addProperty("multiple", fieldDefinition.isMultiple());

		result.addProperty("label", helper.getLanguage().getModelResource(fieldDefinition.getLabel()));

		if (fieldDefinition.isMultiple())
			result.add("value", serializeValueMultiple(field, jsonSerializationContext));
		else
			result.add("value", serializeValue(field, jsonSerializationContext));

		return result;
	}

	public static FieldSerializer<FieldProperty> getFieldSerializer(FieldProperty fieldDefinition, Helper helper) {
		AbstractFieldSerializer serializer = (AbstractFieldSerializer) fieldSerializers.get(fieldDefinition);

		if (serializer == null)
			for (Class<?> clazz : fieldSerializers.keySet())
				if (clazz.isAssignableFrom(fieldDefinition.getClass())) {
					serializer = (AbstractFieldSerializer) fieldSerializers.get(clazz);
					break;
				}

		serializer.setHelper(helper);

		return serializer;
	}

	private JsonArray serializeValueMultiple(Field field, JsonSerializationContext jsonSerializationContext) {
		JsonArray result = new JsonArray();
		Attribute attribute = field.getAttribute();
		FieldProperty fieldDefinition = field.getFieldDefinition();

		List<Attribute> attributeList = attribute.getAttributeList().searchAllByCode(fieldDefinition.getCode());
		for (Attribute attributeItem : attributeList) {
			Field itemField = new Field(field.getNode(), attributeItem, field.getFieldDefinition());
			result.add(serializeValue(itemField, jsonSerializationContext));
		}

		return result;
	}

	protected abstract JsonElement serializeValue(Field field, JsonSerializationContext jsonSerializationContext);

	protected String getIndicatorValue(Attribute attribute, String code) {
		if (attribute == null)
			return "";
		return attribute.getIndicatorValue(code);
	}

	protected Source loadSource(Attribute attribute) {
		String sourceId = getIndicatorValue(attribute, Indicator.SOURCE);

		if (sourceId.isEmpty())
			return null;

		return helper.loadSource(sourceId);
	}

	protected Source locateSource(Attribute attribute, Ref sourceRef) {
		String sourceId = getIndicatorValue(attribute, Indicator.SOURCE);

		if (sourceId.isEmpty()) {
			if (sourceRef != null) {
				String codeSource = helper.getDictionary().getDefinitionCode(sourceRef.getValue());
				return helper.locateSource(codeSource, null);
			}
			return null;
		}

		return helper.loadSource(sourceId);
	}

	protected String getSourceFrom(Node node, Object from) {
		String fromParameter = "";

		if (from == null)
			return fromParameter;

		if (from instanceof String)
			fromParameter = (String) from;
		else if (from instanceof Ref) {
			String fieldName = ((Ref) from).getValue();
			fromParameter = node.getFieldValue(fieldName);
		}

		return fromParameter;
	}

	protected LinkedHashSet<String> getFilters(Field field, boolean mask) {
		LinkedHashSet<String> filtersSet = new LinkedHashSet<String>();
		FieldProperty fieldDefinition = field.getFieldDefinition();
		ArrayList<Object> tagList = null;

		if (fieldDefinition.isCheck()) {
			CheckFieldProperty.SelectProperty selectDefinition = ((CheckFieldProperty) fieldDefinition).getSelect();
			if (selectDefinition != null && selectDefinition.getFilter() != null)
				tagList = selectDefinition.getFilter().getTag();
		} else if (fieldDefinition.isSelect()) {
			SelectFieldProperty.SelectProperty selectDefinition = ((SelectFieldProperty) fieldDefinition).getSelect();
			if (selectDefinition != null && selectDefinition.getFilter() != null)
				tagList = selectDefinition.getFilter().getTag();
		}

		if (tagList == null)
			return filtersSet;

		for (Object tag : tagList) {
			String filterValue = "";

			if (tag instanceof String)
				filterValue = (String) tag;
			else if (tag instanceof Ref) {
				String fieldName = ((Ref) tag).getValue();
				Node node = field.getNode();
				filterValue = (mask) ? "_field:" + ((FormDefinition) node.getDefinition()).getField(fieldName).getCode() : node.getFieldValue(fieldName);
			}

			if (filterValue.isEmpty()) continue;

			filtersSet.add(filterValue);
		}

		return filtersSet;
	}

}
