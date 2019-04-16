package org.monet.http;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class HttpRequest implements Request {

    private final HttpServletRequest request;

    public HttpRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Locale getLocale() {
        return this.request.getLocale();
    }

    @Override
    public String getSessionId() {
        return this.request.getSession().getId();
    }

    @Override
    public String getRequestURL() {
        return this.request.getRequestURL().toString();
    }

    @Override
    public String getHeader(String name) {
        return this.request.getHeader(name);
    }

    @Override
    public String getContextPath() {
        return this.request.getContextPath();
    }

    @Override
    public Object getAttribute(String name) {
        return this.request.getAttribute(name);
    }

    @Override
    public String getParameter(String name) {
        return this.request.getParameter(name);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.request.getInputStream();
    }

    @Override
    public String getRemoteAddr() {
        return this.request.getRemoteAddr();
    }
}