package client.services.http.serializers.fields;

import client.core.model.definition.entity.field.TextFieldDefinition;
import client.core.model.fields.TextField;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import java.util.Map;

public class TextFieldSerializer extends SimpleFieldSerializer<TextField, TextFieldDefinition> {

    @Override
    public JSONObject serialize(TextField element) {
        JSONObject object = super.serialize(element);
        object.put("metas", serializeMetaData(element.getMetaData()));
        return object;
    }

    private JSONValue serializeMetaData(Map<String, String> metaData) {
        final JSONObject object = new JSONObject();
        for (Map.Entry<String, String> entry : metaData.entrySet())
            object.put(entry.getKey(), new JSONString(entry.getValue()));
        return object;
    }
}
