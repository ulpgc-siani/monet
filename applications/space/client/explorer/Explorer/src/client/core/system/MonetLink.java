package client.core.system;

public class MonetLink implements client.core.model.MonetLink {
	private String id;
	private String label;
	private Type type;

	public MonetLink() {
		this(null, null, null);
	}

	public MonetLink(String id, String label, Type type) {
		this.id = id;
		this.label = label;
		this.type = type;
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
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public void fromString(String link, String label) {
		this.label = label;

		if (link == null || link.isEmpty())
			return;

		boolean editMode = false;
		if (link.indexOf("edit") != -1)
			editMode = true;

		id = link.substring(link.indexOf(".") + 1).replace("/", "");
		if (editMode)
			id = link.substring(link.indexOf(".") + 1, link.lastIndexOf("/"));

		this.type = Type.fromLink(link);
	}
}
