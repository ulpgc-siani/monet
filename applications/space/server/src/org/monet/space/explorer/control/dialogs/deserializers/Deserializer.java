package org.monet.space.explorer.control.dialogs.deserializers;

import java.util.List;

public interface Deserializer<T, D, DL> {
	public T deserialize(String content);
	public T deserialize(D object);
	public List<T> deserializeList(String content);
	public List<T> deserializeList(DL object);
}
