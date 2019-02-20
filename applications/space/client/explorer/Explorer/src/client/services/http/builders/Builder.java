package client.services.http.builders;

import client.core.model.List;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public interface Builder<T, L extends List<T>> {
	T build(HttpInstance instance);
	void initialize(T object, HttpInstance instance);
	L buildList(HttpList instances);
}
