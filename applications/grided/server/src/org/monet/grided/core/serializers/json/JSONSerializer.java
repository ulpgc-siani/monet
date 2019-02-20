package org.monet.grided.core.serializers.json;


public interface JSONSerializer<R, T> {
    public R serialize(T object);
    public T unserialize(String json);
}
