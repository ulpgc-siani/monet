package client.core.model;

public interface Account extends Instance {

	ClassName CLASS_NAME = new ClassName("Account");

	User getUser();
	void setUser(User user);
	String getFullName();
	String getEmail();
	String getPhoto();
	Node getRootNode();
	void setRootNode(Node rootNode);
}
