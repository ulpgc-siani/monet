package client.services.callback;

public interface Callback<T> {
	void success(T object);
	void failure(String error);
}
