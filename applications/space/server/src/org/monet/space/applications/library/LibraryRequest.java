/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.applications.library;

import edu.emory.mathcs.backport.java.util.Collections;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.monet.http.HttpRequest;
import org.monet.http.Request;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.utils.StreamHelper;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.Map.Entry;

public class LibraryRequest {

	private static final String PREFIX = "lr_";
	private static final String PARAMETERS_VARIABLE = "r";
	private static final String PROCESSED_VARIABLE = "r";
	private static final Configuration configuration = Configuration.getInstance();

	private static class RequestEntry implements Entry<String, Object> {
		private String key;
		private Object value;

		public RequestEntry(String key, Object value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public String getKey() {
			return key;
		}

		@Override
		public Object getValue() {
			return value;
		}

		@Override
		public Object setValue(Object value) {
			this.value = value;
			return value;
		}
	}

	public static Boolean processParameters(HttpServletRequest request) {
		String[] parametersArray;
		String parameters;
		Integer pos, index;

		parameters = request.getParameter(LibraryRequest.PARAMETERS_VARIABLE);
		if (parameters == null) {
			request.setAttribute(LibraryRequest.PROCESSED_VARIABLE, true);
			return true;
		}

		parameters = LibrarySerializer64.readString(parameters);
		parameters = parameters.replaceAll("&amp;", "::MONET_AMPERSAND::");
		parametersArray = parameters.split(Strings.AMPERSAND);

		for (pos = 0; pos < parametersArray.length; pos++) {
			parametersArray[pos] = parametersArray[pos].replaceAll("::MONET_AMPERSAND::", "&amp;");
			index = parametersArray[pos].indexOf("=");
			request.setAttribute(LibraryRequest.PREFIX + parametersArray[pos].substring(0, index), parametersArray[pos].substring(index + 1));
		}

		request.setAttribute(LibraryRequest.PROCESSED_VARIABLE, true);

		return true;
	}

	@SuppressWarnings("unchecked")
	private static void parseMultiPartParameters(HttpServletRequest request, ArrayList<Entry<String, Object>> requestParameters) {
		FileItemFactory factory = new DiskFileItemFactory();
		List<FileItem> list = new LinkedList<FileItem>();
		ServletFileUpload uploadsManager = new ServletFileUpload(factory);
		Iterator<FileItem> iterator;

		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
		}

		if (!ServletFileUpload.isMultipartContent(request)) return;

		try {
			factory = new DiskFileItemFactory();
			uploadsManager = new ServletFileUpload(factory);
			list = uploadsManager.parseRequest(request);
			iterator = list.listIterator();

			while (iterator.hasNext()) {
				FileItem fileItem = iterator.next();
				if (fileItem.isFormField()) {
					try {
						requestParameters.add(new RequestEntry(fileItem.getFieldName(), StreamHelper.toString(fileItem.getInputStream()).trim()));
					} catch (Exception e) {
					}
				} else {
					try {
                        requestParameters.add(new RequestEntry(fileItem.getFieldName(), new InputStreamBody(fileItem.getInputStream(), ContentType.parse(fileItem.getContentType()), fileItem.getName())));
					} catch (Exception e) {
					}
				}

			}
		} catch (FileUploadException oException) {
			AgentLogger.getInstance().error(oException);
			return;
		}
	}

	public static HashMap<String, Object> parametersToMap(ArrayList<Entry<String, Object>> parameters) {
		HashMap<String, Object> requestParameters = new HashMap<>();

		try {

			for (Map.Entry<String, Object> entry : parameters) {
				requestParameters.put(entry.getKey(), entry.getValue() instanceof InputStreamBody ? ((InputStreamBody) entry.getValue()).getInputStream() : entry.getValue());
			}
		} catch (Exception exception) {
			AgentLogger.getInstance().error(exception);
		}

		return requestParameters;
	}

	public static ArrayList<Entry<String, Object>> parametersToList(HttpServletRequest request) {
		ArrayList<Entry<String, Object>> requestParameters = new ArrayList<Entry<String, Object>>();

		try {

			for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
				String[] aValues = entry.getValue();
				if (aValues.length <= 0) continue;
				requestParameters.add(new RequestEntry(entry.getKey(), aValues[0].trim()));
			}

			LibraryRequest.parseMultiPartParameters(request, requestParameters);
		} catch (Exception exception) {
			AgentLogger.getInstance().error(exception);
		}

		return requestParameters;
	}

	@SuppressWarnings("unchecked")
	public static Enumeration<String> getParameterNames(HttpServletRequest request) {
		Enumeration<String> oEnumeration = request.getAttributeNames();
		HashSet<String> hsResult = new HashSet<String>();
		String sName;

		if (!configuration.encriptParameters()) return request.getParameterNames();

		while (oEnumeration.hasMoreElements()) {
			sName = oEnumeration.nextElement();
			if (sName.indexOf(LibraryRequest.PREFIX) != -1) hsResult.add(sName);
		}

		return Collections.enumeration(hsResult);
	}

	public static String getParameter(String parameter, HttpServletRequest request) {
		if (!configuration.encriptParameters()) return request.getParameter(parameter);
		if (request.getParameter(parameter) != null) return request.getParameter(parameter);
		if (request.getAttribute(LibraryRequest.PROCESSED_VARIABLE) == null) LibraryRequest.processParameters(request);
		return (String) request.getAttribute(LibraryRequest.PREFIX + parameter);
	}

	public static TimeZone getTimeZone(HttpServletRequest request) {
		String timeZoneValue = request.getParameter("timezone");

		if (timeZoneValue == null)
			return BusinessUnit.getTimeZone();

		if (Integer.parseInt(timeZoneValue) >= 0)
			timeZoneValue = "+" + timeZoneValue;

		return TimeZone.getTimeZone("GMT" + timeZoneValue);
	}

	public static String getRequestURL(HttpServletRequest request) {
		return getRequestURL(new HttpRequest(request));
	}

    public static String getRequestURL(Request request) {
        String result = request.getRequestURL();

        String forwardedProto = request.getHeader("X-Forwarded-Proto");
        if (forwardedProto != null && forwardedProto.equals("https"))
            result = result.replace("http:", "https:");

        return result;
    }

	public static String getBaseUrl(HttpServletRequest request) {
		return getBaseUrl(new HttpRequest(request));
	}

    public static String getBaseUrl(Request request) {
        String requestUrl = LibraryRequest.getRequestURL(request);
        URI requestUri = null;

        try {
            requestUri = new URI(requestUrl);
        } catch (URISyntaxException e) {
            return requestUrl;
        }

        int port = requestUri.getPort();

        return isSecure(requestUrl)?"https://":"http://" + requestUri.getHost() + (port != 80?":"+port:"") + request.getContextPath();
    }

    private static boolean isSecure(String requestUrl) {
        return requestUrl != null && requestUrl.indexOf("https:") != -1;
    }

	public static String getRealIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.isEmpty()) ip = request.getHeader("X-Forwarded-For");
		if (ip != null && !ip.isEmpty()) {
			int indexComma = ip.indexOf(',');
			if (indexComma == -1) return ip;
			else return ip.substring(0, indexComma);
		}
		return request.getRemoteAddr();
	}

}