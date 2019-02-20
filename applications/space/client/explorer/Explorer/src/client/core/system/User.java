package client.core.system;

public class User implements client.core.model.User {
	private String id;
	private String fullName;
	private String email;
	private String photo;

	public User() {
	}

	public User(String fullName, String email, String photo) {
		this.fullName = fullName;
		this.email = email;
		this.photo = photo;
	}

	@Override
	public String getId() {
		return id;
	}

    public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getFullName() {
		return fullName;
	}

    public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String getEmail() {
		return email;
	}

    public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getPhoto() {
		return photo;
	}

    public void setPhoto(String photo) {
		this.photo = photo;
	}

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof User) && id.equals(((User) obj).id);
    }
}
