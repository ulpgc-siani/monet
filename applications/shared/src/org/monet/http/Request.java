package org.monet.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public interface Request {
    Locale getLocale();

    String getSessionId();

    String getRequestURL();

    String getContextPath();

    String getHeader(String name);

    Object getAttribute(String name);

    String getParameter(String name);

    String getRemoteAddr();

    InputStream getInputStream() throws IOException;
}
