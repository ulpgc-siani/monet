package org.monet.bpi;

import org.monet.bpi.java.MonetLinkImpl;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class MonetLink {

	public static final String ID = "id";
	public static final String TYPE = "type";
	public static final String EDIT_MODE = "editMode";

	public static enum Type {
		Node,
		Task,
		User,
		News
	}

	@Attribute(name = ID)
	private String id;
	@Text
	private String label;
	@Attribute(name = TYPE)
	private Type type;
	@Attribute(name = EDIT_MODE, required = false)
	private boolean isEditMode = false;

	public MonetLink() {
	}

	public MonetLink(Type type, String id) {
		this.id = id;
		this.type = type;
	}

	public MonetLink(Type type, String id, boolean isEditMode) {
		this(type, id);
		this.isEditMode = isEditMode;
	}

	public MonetLink(Type type, String id, String label) {
		this(type, id);
		this.label = label;
	}

	public MonetLink(Type type, String id, String label, boolean isEditMode) {
		this(type, id, isEditMode);
		this.label = label;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		switch (this.type) {
			case Node:
				return String.format("ml://node.%s%s", this.id, (this.isEditMode) ? "edit" : "");
			case Task:
				return String.format("ml://task.%s", this.id);
			case User:
				return String.format("ml://user.%s", this.id);
			case News:
				return String.format("ml://news.0");
			default:
				return "";
		}
	}

	public static MonetLink from(String link) {

		if (link == null || link.isEmpty())
			return null;

		boolean editMode = false;
		if (link.indexOf("edit") != -1)
			editMode = true;

		String objectId = link.substring(link.indexOf(".") + 1);
		if (editMode)
			objectId = link.substring(link.indexOf(".") + 1, link.lastIndexOf("/"));

		if (link.indexOf("node") > -1)
			return new MonetLink(Type.Node, objectId, editMode);

		if (link.indexOf("task") > -1)
			return new MonetLink(Type.Task, objectId, editMode);

		if (link.indexOf("user") > -1)
			return new MonetLink(Type.Task, objectId, editMode);

		return null;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isEditMode() {
		return this.isEditMode;
	}

	public void setIsEditMode(boolean value) {
		this.isEditMode = value;
	}

	public static MonetLink forNode(String nodeId) {
		return new MonetLinkImpl(Type.Node, nodeId, null, false);
	}

	public static MonetLink forNode(String nodeId, boolean editMode) {
		return new MonetLinkImpl(Type.Node, nodeId, null, editMode);
	}

	public static MonetLink forTask(String taskId) {
		return new MonetLinkImpl(Type.Task, taskId, null, false);
	}

	public static MonetLink forNode(String nodeId, String label) {
		return new MonetLinkImpl(Type.Node, nodeId, label, false);
	}

	public static MonetLink forNode(String nodeId, String label, boolean editMode) {
		return new MonetLinkImpl(Type.Node, nodeId, label, editMode);
	}

	public static MonetLink forTask(String taskId, String label) {
		return new MonetLinkImpl(Type.Task, taskId, label, false);
	}

}
