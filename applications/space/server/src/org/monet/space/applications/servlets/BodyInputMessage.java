package org.monet.space.applications.servlets;

import org.monet.space.kernel.utils.StreamHelper;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BodyInputMessage extends HttpInputMessage {
    private HashMap<String, String> parameters;

    public BodyInputMessage(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        try {
            readParameters();
        }
        catch (Exception exception) {
        }
        return parameters.containsKey(name) ? parameters.get(name) : null;
    }

    private Map<String, String> readParameters() throws IOException {

        if (parameters != null)
            return parameters;

        parameters = new HashMap<>();
        ByteArrayOutputStream rawData = rawData(request);
        final String[] urlParams = new String(rawData.toByteArray()).split("&"); //$NON-NLS-1$

        for (final String param : urlParams) {
            final int equalsPos = param.indexOf('=');
            if (equalsPos != -1) {
                parameters.put(param.substring(0, equalsPos), param.substring(equalsPos + 1));
            }
        }

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            parameters.put(name, request.getParameter(name));
        }

        return parameters;
    }

    private ByteArrayOutputStream rawData(HttpServletRequest request) throws IOException {
        ByteArrayOutputStream rawData = new ByteArrayOutputStream();
        StreamHelper.copyData(request.getInputStream(), rawData);
        return rawData;
    }

}
