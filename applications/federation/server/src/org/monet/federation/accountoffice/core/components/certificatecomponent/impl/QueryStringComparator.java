package org.monet.federation.accountoffice.core.components.certificatecomponent.impl;

import java.util.HashMap;
import java.util.Map;

public class QueryStringComparator {

	public static boolean equals(String s1, String s2) {
		Map<String, String> map1 = parse(s1);
		Map<String, String> map2 = parse(s2);

		return map1.equals(map2);
	}

	private static Map<String, String> parse(String queryString) {
		Map<String, String> result = new HashMap<>();

		if (queryString == null || queryString.isEmpty()) return result;

		String[] pairs = queryString.split("&");
		for (String pair : pairs) {
			String[] parts = pair.split("=", 2);
			if (parts.length == 2) {
				result.put(parts[0], parts[1]);
			} else if (parts.length == 1) {
				result.put(parts[0], "");
			}
		}
		return result;
	}

}
