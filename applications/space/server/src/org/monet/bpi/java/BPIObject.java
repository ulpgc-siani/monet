package org.monet.bpi.java;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BPIObject {

	@SuppressWarnings("rawtypes")
	Class getGenericType(int position, Class<?> clazz) {
		Type type = clazz.getGenericSuperclass();
		while (!(type instanceof ParameterizedType)) {
			clazz = clazz.getSuperclass();
			type = clazz.getGenericSuperclass();
		}
		ParameterizedType paramType = (ParameterizedType) type;
		return (Class) paramType.getActualTypeArguments()[position];
	}

}
