package org.monet.space.applications;

import java.io.File;

public interface OutputMessage {
    void write(String content);
    void write(File file);
}
