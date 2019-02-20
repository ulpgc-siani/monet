package org.monet.space.kernel.model;

import org.monet.space.office.ApplicationOffice;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonetLink {

	public static final String ID = "id";
	public static final String TYPE = "type";
	public static final String EDIT_MODE = "editMode";
	public static final String VIEW = "view";

	private static final String PATTERN = "ml://([^\\.]*)\\.([^/]*)(/edit)?";
	private static final String ENTITY_URL = "%s/%s/%s/%s%s";

	public enum Type {
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
	@Attribute(name = VIEW, required = false)
	private String view = null;

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

	public MonetLink withView(String view) {
		this.view = view;
		return this;
	}

	public String getView() {
		return this.view;
	}

	@Override
	public String toString() {
		String view = (this.view!=null) ? "." + this.view : "";

		switch (this.type) {
			case Node:
				return String.format("ml://node.%s%s/%s", this.id, view, (this.isEditMode) ? "edit" : "");
			case Task:
				return String.format("ml://task.%s%s", this.id, view);
			case User:
				return String.format("ml://user.%s%s", this.id, view);
			case News:
				return String.format("ml://news.0%s", view);
			default:
				return "";
		}
	}

	public static MonetLink from(String link) {

		if (link == null || link.isEmpty())
			return null;

		Pattern pattern = Pattern.compile(PATTERN);
		Matcher matcher = pattern.matcher(link);

		if (!matcher.find())
			return null;

		boolean editMode = matcher.group(3) != null;
		String entityId = entityId(matcher.group(2));
		String viewId = viewId(matcher.group(2));
		MonetLink result = fromType(matcher.group(1), editMode, entityId);

		if (result == null)
			return null;

		if (viewId != null)
			result.withView(viewId);

		return result;
	}

	private static MonetLink fromType(String type, boolean editMode, String entityId) {
		MonetLink result = null;

		if (type.equalsIgnoreCase("node"))
			result = new MonetLink(Type.Node, entityId, editMode);
		else if (type.equalsIgnoreCase("task"))
			result =new MonetLink(Type.Task, entityId, editMode);
		else if (type.equalsIgnoreCase("user"))
			result = new MonetLink(Type.Task, entityId, editMode);

		return result;
	}

	private static String entityId(String entityPart) {
		return entityPart.split("\\.")[0];
	}

	private static String viewId(String entityPart) {

		if (entityPart.indexOf(".") == -1)
			return null;

		return entityPart.substring(entityPart.indexOf(".") + 1);
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

	public String toOfficeUrl() {
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		String editMode = this.isEditMode() ? "&edit.html?mode=page" : "";
		return String.format(ENTITY_URL, businessUnit.getDistribution().getSpace().getDeployUri(), ApplicationOffice.NAME, this.getType().toString().toLowerCase(), this.getId(), editMode);
	}

}
