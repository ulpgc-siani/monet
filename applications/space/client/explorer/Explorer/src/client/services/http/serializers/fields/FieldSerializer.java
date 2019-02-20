package client.services.http.serializers.fields;

import client.core.model.Field;
import client.core.model.MultipleField;
import client.core.model.fields.*;
import client.services.http.serializers.AbstractSerializer;
import client.services.http.serializers.Serializer;
import com.google.gwt.json.client.JSONValue;

import java.util.HashMap;
import java.util.Map;

import static client.core.model.Instance.ClassName;

public class FieldSerializer extends AbstractSerializer<Field> {
	private static Map<ClassName, Serializer> serializerMap = new HashMap<ClassName, Serializer>() {{
		put(BooleanField.CLASS_NAME, new BooleanFieldSerializer());
		put(CheckField.CLASS_NAME, new CheckFieldSerializer());
		put(CompositeField.CLASS_NAME, new CompositeFieldSerializer());
		put(DateField.CLASS_NAME, new DateFieldSerializer());
		put(FileField.CLASS_NAME, new FileFieldSerializer());
		put(LinkField.CLASS_NAME, new LinkFieldSerializer());
		put(MemoField.CLASS_NAME, new MemoFieldSerializer());
		put(NodeField.CLASS_NAME, new NodeFieldSerializer());
		put(NumberField.CLASS_NAME, new NumberFieldSerializer());
		put(PictureField.CLASS_NAME, new PictureFieldSerializer());
		put(SelectField.CLASS_NAME, new SelectFieldSerializer());
		put(SerialField.CLASS_NAME, new SerialFieldSerializer());
		put(SummationField.CLASS_NAME, new SummationFieldSerializer());
		put(TextField.CLASS_NAME, new TextFieldSerializer());
		put(UriField.CLASS_NAME, new UriFieldSerializer());
	}};

	@Override
	public JSONValue serialize(Field element) {
		ClassName className = element.getClassName();

		if (element.isMultiple())
			className = ((MultipleField)element).getClassNameOfValue();

		if (!serializerMap.containsKey(className))
			return null;

		return (JSONValue) serializerMap.get(className).serialize(element);
	}

}
