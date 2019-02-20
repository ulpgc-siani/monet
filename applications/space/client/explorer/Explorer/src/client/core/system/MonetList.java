package client.core.system;

import java.util.ArrayList;

public class MonetList<T> extends ArrayList<T> implements client.core.model.List<T> {
	private int totalCount = -1;

	public MonetList() {
	}

	public MonetList(T... elements) {
		addAll(elements);
	}

	public MonetList(Iterable<T> items) {
		for (T item : items)
			add(item);
	}

	public void setItems(T[] items) {
		for (T item : items)
			add(item);
	}

	@Override
	public T get(client.core.model.Key key) {

		for (T element : this)
			if (element.equals(key))
				return element;

		return null;
	}

	@Override
	public T get(String key) {

		for (T element : this)
			if (element.equals(key))
				return element;

		return null;
	}

	@Override
	public void addAll(client.core.model.List<T> elements) {
		for (T element : elements)
			this.add(element);
	}

	@Override
	public void addAll(T[] elements) {
		for (T element : elements)
			add(element);
	}

	@Override
	public void removeAll(client.core.model.List<T> elements) {
		for (T element : elements)
			if (contains(element))
				remove(element);
	}

	@Override
	public int getTotalCount() {
		return totalCount!=-1?totalCount:size();
	}

	@Override
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
