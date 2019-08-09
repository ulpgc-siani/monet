package org.monet.space.applications;

import org.monet.http.Request;
import org.monet.http.Response;

import java.util.HashMap;

public interface ServiceAction {
    void setRequest(Request request);
    void setResponse(Response response);
    void setParameters(HashMap<String, Object> parameters);
    String execute();
}
