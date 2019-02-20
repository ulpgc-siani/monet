package client.core.system;

public class UserRole extends Role implements client.core.model.UserRole {

	public UserRole() {
		super(Type.USER);
	}

	public UserRole(String id, String label) {
		super(id, label, Type.USER);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.UserRole.CLASS_NAME;
	}

}
