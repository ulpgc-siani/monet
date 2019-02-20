package org.monet.space.explorer.control.dialogs.deserializers.field;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.monet.space.explorer.control.dialogs.deserializers.AbstractDeserializer;
import org.monet.space.kernel.model.Attribute;

import java.util.HashMap;
import java.util.Map;

public class AbstractFieldDeserializer extends AbstractDeserializer<Attribute, JsonObject> implements FieldDeserializer {

	public AbstractFieldDeserializer() {
		super(null);
	}

	public AbstractFieldDeserializer(Helper helper) {
		super(helper);
	}

	private static Map<String, FieldDeserializer> fieldDeserializers = new HashMap<String, FieldDeserializer>() {{
		put("boolean", new BooleanFieldDeserializer());
		put("check", new CheckFieldDeserializer());
		put("date", new DateFieldDeserializer());
		put("file", new FileFieldDeserializer());
		put("link", new LinkFieldDeserializer());
		put("summation", new SummationFieldDeserializer());
		put("memo", new MemoFieldDeserializer());
		put("node", new NodeFieldDeserializer());
		put("number", new NumberFieldDeserializer());
		put("picture", new FileFieldDeserializer());
		put("composite", new CompositeFieldDeserializer());
		put("select", new SelectFieldDeserializer());
		put("serial", new SerialFieldDeserializer());
		put("text", new TextFieldDeserializer());
		put("uri", new UriFieldDeserializer());
	}};

	@Override
	public Attribute deserialize(String content) {
		return deserialize((JsonObject) new JsonParser().parse(content));
	}

	@Override
	public Attribute deserialize(JsonObject object) {
		String type = object.get("type").getAsString();
		boolean multiple = object.get("multiple").getAsBoolean();

		Attribute attribute = new Attribute();
		attribute.setCode(object.get("code").getAsString());

		if (multiple)
			deserializeValueMultiple(attribute, object.getAsJsonArray("value"));
		else
			deserializeValue(attribute, object.get("value"));

		return attribute;
	}

	@Override
	public void deserializeValue(Attribute attribute, JsonElement value) {
	}

	@Override
	public void deserializeValueMultiple(Attribute attribute, JsonArray value) {
		for (int i = 0; i < value.size(); i++) {
			Attribute childAttribute = new Attribute();

			childAttribute.setCode(attribute.getCode());
			deserializeValue(childAttribute, value.get(i));

			attribute.getAttributeList().add(childAttribute);
		}
	}

	public static Attribute deserialize(String field, Helper helper) {
        AbstractFieldDeserializer deserializer = (AbstractFieldDeserializer) fieldDeserializers.get(getFieldType(field));
		deserializer.setHelper(helper);
		return deserializer.deserialize((JsonObject) new JsonParser().parse(field));
	}

    public static String getFieldType(String field) {
        return ((JsonObject) new JsonParser().parse(field)).get("type").getAsString();
    }
}
