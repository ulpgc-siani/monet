package org.monet.http;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class HttpResponse implements Response {

    private final HttpServletResponse response;

    public HttpResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void setContentType(String type) {
        this.response.setContentType(type);
    }

    @Override
    public void setContentLength(int length) {
        this.response.setContentLength(length);
    }

    @Override
    public void setHeader(String name, String value) {
        this.response.setHeader(name, value);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return this.response.getOutputStream();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return this.response.getWriter();
    }
}
