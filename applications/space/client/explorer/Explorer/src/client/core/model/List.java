package client.core.model;

public interface List<T> extends Iterable<T>, java.util.List<T> {

	T get(Key key);
	T get(String key);

	void addAll(List<T> elements);
	void addAll(T[] elements);

	void removeAll(List<T> elements);

	int getTotalCount();
	void setTotalCount(int totalCount);
}
