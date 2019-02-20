package org.monet.space.kernel.model;

public class Pair<T, U> {

	private T first;
	private U second;
	private Object[] extra;

	public Pair(T first, U second, Object... extra) {
		this.first = first;
		this.second = second;
		this.extra = extra;
	}

	public T getFirst() {
		return first;
	}

	public void setFirst(T first) {
		this.first = first;
	}

	public U getSecond() {
		return second;
	}

	public void setSecond(U second) {
		this.second = second;
	}

	public Object[] getExtra() {
		return extra;
	}

	public void setExtra(Object[] extra) {
		this.extra = extra;
	}

}
