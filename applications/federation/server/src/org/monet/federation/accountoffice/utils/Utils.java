package org.monet.federation.accountoffice.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

public class Utils {

	public static String getAddress(HttpServletRequest request) {
		String forwardedFor = request.getHeader("X-Forwarded-For");

        if (forwardedFor != null) {
            String[] forwardedForArray = forwardedFor.split(",");
            return forwardedForArray[0];
        }

		return request.getRemoteAddr();
	}

	public static boolean containsHeader(HttpServletRequest request, String header) {
        return request.getHeader(header) != null;
	}

    public static String getRequestURL(HttpServletRequest request) {
        String result = request.getRequestURL().toString();

        String forwardedProto = request.getHeader("X-Forwarded-Proto");
        if (forwardedProto != null && forwardedProto.equals("https"))
            result = result.replace("http:", "https:");

        return result;
    }

    public static String getBaseUrl(HttpServletRequest request) {
        String requestUrl = Utils.getRequestURL(request);
        URI requestUri = null;

        try {
            requestUri = new URI(requestUrl);
        } catch (URISyntaxException e) {
            return requestUrl;
        }

        int port = requestUri.getPort();

        return (isSecure(requestUrl)?"https://":"http://") + requestUri.getHost() + (port!=80 && port!=-1?":"+port:"") + request.getContextPath();
    }

	public static String getRequestFederationURL(HttpServletRequest request, String baseUrl) {
		String url = baseUrl.substring(0, baseUrl.lastIndexOf("/"));
		String pathRequest = request.getRequestURI();
		return url + pathRequest;
	}

	public static void sendRedirect(HttpServletResponse response, String location) {
		response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", location);
		response.setContentType("text/html");
		response.addDateHeader("Expires", 0);

		response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0

		response.setDateHeader("Expires", 0);
	}

    private static boolean isSecure(String requestUrl) {
        return requestUrl != null && requestUrl.indexOf("https:") != -1;
    }

}
