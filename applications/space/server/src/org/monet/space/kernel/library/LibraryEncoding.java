package org.monet.space.kernel.library;

import java.net.URLDecoder;
import java.net.URLEncoder;

public abstract class LibraryEncoding {

	public static String encode(String data) {
		try {
			if (data == null)
				return null;

			return URLEncoder.encode(data, "utf-8");
		} catch (Exception exception) {
			return data;
		}
	}

	public static String decode(String data) {
		try {
			if (data == null)
				return null;

			return URLDecoder.decode(data, "utf-8");
		} catch (Exception exception) {
			return data;
		}
	}

}