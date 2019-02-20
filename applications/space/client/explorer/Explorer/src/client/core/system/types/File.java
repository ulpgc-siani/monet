package client.core.system.types;

public class File implements client.core.model.types.File {
	private String id;
	private String label;

	public File() {
	}

	public File(String label) {
		this.label = label;
	}

	public File(String id, String label) {
		this.id = id;
		this.label = label;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
