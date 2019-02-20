package org.monet.space.explorer.control.displays.serializers;

import org.monet.space.explorer.model.List;

public interface Serializer<T, S> {
	String serialize(T object);
	S serializeObject(T object);
	String serializeList(List object);
}
