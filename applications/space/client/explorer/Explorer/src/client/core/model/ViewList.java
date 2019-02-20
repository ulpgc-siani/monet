package client.core.model;

public interface ViewList<T extends View> extends List<T> {

	T getDefaultView();
	void update(T view);

}
