package org.monet.space.applications.servlets;

import org.monet.space.applications.InputMessage;

import javax.servlet.http.HttpServletRequest;

public abstract class HttpInputMessage implements InputMessage {
    protected final HttpServletRequest request;

    public HttpInputMessage(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public abstract String getParameter(String name);

}
