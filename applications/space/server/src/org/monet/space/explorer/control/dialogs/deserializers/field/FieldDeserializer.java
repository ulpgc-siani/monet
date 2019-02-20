package org.monet.space.explorer.control.dialogs.deserializers.field;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.monet.space.kernel.model.Attribute;

public interface FieldDeserializer {
	public Attribute deserialize(String value);
	public void deserializeValue(Attribute attribute, JsonElement value);
	public void deserializeValueMultiple(Attribute attribute, JsonArray value);
}
