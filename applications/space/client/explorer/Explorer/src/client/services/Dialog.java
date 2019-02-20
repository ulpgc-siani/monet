package client.services;

public interface Dialog {
	String getOperation();
	String getEntityId();
	void add(String name, Object value);
	String serialize();
}
