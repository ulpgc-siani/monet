package org.monet.federation.accountservice.client.utils;

import javax.servlet.http.HttpServletRequest;

public class Utils {
	public static String getAddr(HttpServletRequest request) {
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
