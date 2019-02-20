package client.core.model;

public interface IndexEntry<T extends Entity> {
	T getEntity();
	String getLabel();
}
