package client.core.system.types;

public class Link implements client.core.model.types.Link {
	private String id;
	private String label;

	public Link() {
	}

	public Link(String id, String label) {
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

    @Override
    public boolean equals(Object link) {
        return link instanceof Link && id.equals(((Link) link).id);
    }
}
