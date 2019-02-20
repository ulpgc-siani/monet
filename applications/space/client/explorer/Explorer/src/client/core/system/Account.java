package client.core.system;

import client.core.model.Node;
import client.core.model.User;

public class Account implements client.core.model.Account {
	private User user;
	private Node rootNode;

	public Account() {
	}

	public Account(User user, Node rootNode) {
		this.user = user;
		this.rootNode = rootNode;
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.Account.CLASS_NAME;
	}

	@Override
	public User getUser() {
		return this.user;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String getFullName() {
		return this.user.getFullName();
	}

	@Override
	public String getEmail() {
		return this.user.getEmail();
	}

	@Override
	public String getPhoto() {
		return this.user.getPhoto();
	}

	@Override
	public Node getRootNode() {
		return this.rootNode;
	}

	@Override
	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}

}
