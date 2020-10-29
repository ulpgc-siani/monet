package org.monet.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public interface Response {
    void setContentType(String type);

    void setContentLength(int length);

    void setHeader(String name, String value);

    OutputStream getOutputStream() throws IOException;

    PrintWriter getWriter() throws IOException;

    void setCharacterEncoding(String encoding);
}
