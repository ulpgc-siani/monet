package client.services.http.serializers;

import client.core.model.List;

public interface Serializer<T, R, RL> {
	R serialize(T element);
	RL serializeList(List<T> element);
}
