package client.core.model;

public interface MonetLink {

	enum Type {
		Node,
		Task,
		User,
		News;

		public static Type fromLink(String link) {

			if (link.contains("node"))
				return Type.Node;

			if (link.contains("task"))
				return Type.Task;

			if (link.contains("user"))
				return Type.User;

			return null;
		}
	}

	String getId();
	String getLabel();
	Type getType();
	void fromString(String linkValue, String label);

}
