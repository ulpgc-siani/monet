package org.monet.space.explorer.control.displays.serializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.FieldProperty;
import org.monet.space.kernel.model.Field;

public interface FieldSerializer<T extends FieldProperty> {
	JsonElement serialize(Field field, JsonSerializationContext jsonSerializationContext);
}
