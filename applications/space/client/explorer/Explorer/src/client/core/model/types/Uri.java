package client.core.model.types;

public interface Uri {
	String getValue();
	String getProtocol();
	String getHost();
	int getPort();
	String getPath();
	String toString();
}
