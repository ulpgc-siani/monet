package org.monet.space.explorer.control.dialogs;

import com.google.inject.Inject;
import org.monet.space.explorer.control.dialogs.constants.Parameter;
import org.monet.space.explorer.control.dialogs.deserializers.ExplorerDeserializer;
import org.monet.space.explorer.control.displays.Display;
import org.monet.space.explorer.control.displays.HttpDisplay;
import org.monet.space.explorer.model.Language;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dictionary;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class HttpDialog<T> implements Dialog<T> {
	protected HttpServletRequest request;
	protected Dictionary dictionary;
	protected Language language;

	public void inject(HttpServletRequest request) {
		this.request = request;
	}

	@Inject
	public void inject(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	@Inject
	public void inject(Language language) {
		this.language = language;
	}

	@Override
	public <O> O get(String name) {
		return (O)request.getParameter(name);
	}

	public String getString(String name) {
		return (String)get(name);
	}

	public boolean getBoolean(String name) {
		return Boolean.valueOf(getString(name));
	}

	public int getInt(String name) {
		try {
			return Integer.valueOf((String)get(name));
		} catch (NumberFormatException oException) {
			return 0;
		}
	}

	@Override
	public String getEntityId() {
		return getString(Parameter.ID);
	}

	@Override
	public String[] getEntityIds() {
		return getString(Parameter.ID).split(",");
	}

	public String getOperation() {
		return (String)get(Parameter.OPERATION);
	}

	@Override
	public String getUrl() {
		String result = request.getRequestURL().toString();

		String forwardedProto = request.getHeader("X-Forwarded-Proto");
		if (forwardedProto != null && forwardedProto.equals("https"))
			result = result.replace("http:", "https:");

		return result;
	}

	@Override
	public String getQueryString() {
		return request.getQueryString();
	}

	@Override
	public String getRemoteAddress() {
		return request.getRemoteAddr();
	}

	@Override
	public Map<String, String> getParameters() {
		Enumeration<?> parameters = request.getParameterNames();
		Map<String, String> result = new HashMap<>();

		while (parameters.hasMoreElements()) {
			String key = (String) parameters.nextElement();
			String value = this.request.getParameter(key);
			result.put(key, value);
		}

		return result;
	}

	@Override
	public String getHeader(String name) {
		return request.getHeader(name);
	}

	@Override
	public String getSessionId() {
		return request.getSession().getId();
	}

	@Override
	public String getLanguage() {
		return request.getLocale().getLanguage();
	}

	@Override
	public TimeZone getTimeZone() {
		String timeZoneValue = request.getParameter("timezone");

		if (timeZoneValue == null)
			return BusinessUnit.getTimeZone();

		if (Integer.parseInt(timeZoneValue) >= 0)
			timeZoneValue = "+" + timeZoneValue;

		return TimeZone.getTimeZone("GMT" + timeZoneValue);
	}

	@Override
	public <T extends Display> void dispatch(String url, T display) throws RuntimeException {
		if (! (display instanceof HttpDisplay))
			throw new RuntimeException();

		try {
			request.getRequestDispatcher(url).include(request, ((HttpDisplay)display).getResponse());
		} catch (ServletException | IOException e) {
			throw new RuntimeException(e);
		}
    }

	public HttpServletRequest getRequest() {
		return request;
	}

	protected ExplorerDeserializer.Helper createDeserializerHelper() {
		return new ExplorerDeserializer.Helper() {
			@Override
			public Dictionary getDictionary() {
				return dictionary;
			}

			@Override
			public Language getLanguage() {
				return language;
			}
		};
	}

}
