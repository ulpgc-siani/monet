package org.monet.space.explorer.control.dialogs;

import org.monet.space.explorer.control.displays.Display;

import java.util.Map;
import java.util.TimeZone;

public interface Dialog<T> {
	<O extends Object> O get(String name);

	String getEntityId();
	String[] getEntityIds();
	String getUrl();
	String getQueryString();
	String getRemoteAddress();
	Map<String, String> getParameters();

	String getHeader(String name);
	String getSessionId();
	String getLanguage();
	TimeZone getTimeZone();
	<T extends Display> void dispatch(String url, T display) throws RuntimeException;
}
