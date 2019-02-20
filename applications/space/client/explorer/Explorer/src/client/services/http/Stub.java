package client.services.http;

import client.core.model.Field;
import client.services.Dialog;
import client.services.callback.Callback;

import static client.core.model.Instance.ClassName;

public interface Stub {
	String getRequestUrl(final Dialog dialog);
	<T> void request(final Dialog dialog, final ClassName className, final Callback<T> callback);
	void requestString(final Dialog dialog, final Callback<String> callback);
	void requestFile(final Dialog dialog, final Callback<String> callback);

    Field buildField(String field);
    Object buildFieldValue(String fieldType, String value);

}
