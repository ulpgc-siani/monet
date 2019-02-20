package org.monet.space.applications.servlets;

import javax.servlet.http.HttpServletRequest;

public class RequestInputMessage extends HttpInputMessage {

    public RequestInputMessage(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        return request.getParameter(name);
    }

}
