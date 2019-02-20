package org.monet.grided.core.persistence.filesystem.readers;

import java.io.File;

public interface DataReader<T> {

    public T read(File file);
}

