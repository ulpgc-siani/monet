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

package org.monet.space.mobile.library;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class LibraryRequest extends org.monet.space.applications.library.LibraryRequest {

	public static final String OAUTH_CALLBACK = "oauth_callback";
	public static final String OAUTH_SIGNATURE = "oauth_signature";
	public static final String OAUTH_VERSION = "oauth_version";
	public static final String OAUTH_NONCE = "oauth_nonce";
	public static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
	public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
	public static final String OAUTH_TIMESTAMP = "oauth_timestamp";
	public static final String OAUTH_TOKEN = "oauth_token";
	public static final String OAUTH_VERIFIER = "oauth_verifier";

	public static Map<String, String> extractAuthElements(HttpServletRequest httpRequest) {
		return extractAuthElements(new org.monet.http.HttpRequest(httpRequest));
	}

	public static Map<String, String> extractAuthElements(org.monet.http.Request httpRequest) {
		StringBuilder authorizationHeader = new StringBuilder(httpRequest.getHeader("authorization").substring(6));

		Map<String, String> elements = new HashMap<String, String>();
		while (authorizationHeader.length() > 0) {
			int commaIndex = authorizationHeader.indexOf(",");
			if (commaIndex == -1) commaIndex = authorizationHeader.length();

			String element = authorizationHeader.substring(0, commaIndex);

			int equalsIndex = element.indexOf("=");
			if (equalsIndex == -1) equalsIndex = element.length();

			String key = element.substring(0, equalsIndex).trim();
			String value = element.substring(equalsIndex + 2, element.length() - 1);

			authorizationHeader.delete(0, commaIndex + 1);

			elements.put(key, value);
		}
		return elements;
	}
}