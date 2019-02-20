package org.monet.grided.core.serializers.xml;

import org.monet.grided.core.persistence.filesystem.Data;

public interface XMLSerializer<R extends Data> {
    public String serialize(R object);
    public R unSerialize(String xml);
}

