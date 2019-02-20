package client.core.model.factory;

import client.core.model.Field;

import static client.core.model.Instance.ClassName;

public interface FieldFactory {
	<Type extends Field<?, ?>> Type createField(String code, String label, Class<?> type);
	<Type extends Field<?, ?>> Type createFieldByClassName(String code, String label, ClassName type);
}
