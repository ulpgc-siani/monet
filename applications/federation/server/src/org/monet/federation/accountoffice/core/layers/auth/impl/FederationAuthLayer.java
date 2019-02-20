package org.monet.federation.accountoffice.core.layers.auth.impl;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public class FederationAuthLayer {

	String requestParam(HttpServletRequest request, String name) {
		String param = request.getParameter(name);
		try {
			return new String(param.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return param;
		}
	}

}
