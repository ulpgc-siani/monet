package client.core;

import client.core.model.*;
import client.core.model.definition.Ref;
import cosmos.types.Date;

import java.util.HashMap;
import java.util.Map;

import static client.core.model.Index.Attribute;
import static client.core.model.definition.entity.IndexDefinition.ReferenceDefinition.AttributeDefinition;

public class IndexBuilder {

	public static final Attribute[] EmptyAttributes = new Attribute[0];

	public static Index buildIndex(String id, client.core.model.Index.Attribute[] attributes) {
		return new client.core.system.Index(id, attributes);
	}

	public static Index buildIndex() {
		return buildIndex(null, new Attribute[0]);
	}

	public static client.core.model.Index.Attribute buildIndexAttribute(String name, String label) {
		return new client.core.system.Index.Attribute(name, label);
	}

	public static Filter.Option buildFilterOption(String value, String label) {
		return new client.core.system.Filter.Option(value, label);
	}

	public static TaskListIndexEntry buildTaskListIndexEntry(Map<String, Object> values) {
		client.core.system.TaskListIndexEntry entry = new client.core.system.TaskListIndexEntry();

		if (values.containsKey("entity")) entry.setEntity((Task) values.get("entity"));
		if (values.containsKey("type")) entry.setType((Task.Type) values.get("type"));
		if (values.containsKey("owner")) entry.setOwner((User)values.get("owner"));
		if (values.containsKey("sender")) entry.setSender((User) values.get("sender"));
		if (values.containsKey("state")) entry.setState((Task.State) values.get("state"));
		if (values.containsKey("description")) entry.setDescription((String) values.get("description"));
		if (values.containsKey("messages_count")) entry.setMessagesCount((int) values.get("messages_count"));
		if (values.containsKey("progress_image")) entry.setTimeLineImageUrl((String) values.get("progress_image"));
		if (values.containsKey("create_date")) entry.setCreateDate((Date) values.get("create_date"));
		if (values.containsKey("update_date")) entry.setUpdateDate((Date) values.get("update_date"));
		if (values.containsKey("urgent")) entry.setUrgent((boolean) values.get("urgent"));

		return entry;
	}

	public static TaskListIndexEntry buildTaskListIndexEntry(final Task task, final String label) {
		return buildTaskListIndexEntry(new HashMap<String, Object>() {{
			put("entity", task);
			put("label", label);
		}});
	}

	public static NodeIndexEntry buildNodeIndexEntry(Map<String, Object> values) {
		client.core.system.NodeIndexEntry entry = new client.core.system.NodeIndexEntry();

		entry.setTypeFactory(new EntityBuilder.EntityFactory());

		if (values.containsKey("entity")) entry.setEntity((Node) values.get("entity"));
		if (values.containsKey("label")) entry.setLabel((String) values.get("label"));
		if (values.containsKey("geo_referenced")) entry.setGeoReferenced((boolean) values.get("geo_referenced"));
		if (values.containsKey("title")) entry.setTitle((NodeIndexEntry.Attribute) values.get("title"));
		if (values.containsKey("picture")) entry.setPicture((NodeIndexEntry.Attribute) values.get("picture"));
		if (values.containsKey("icon")) entry.setIcon((NodeIndexEntry.Attribute) values.get("icon"));
		if (values.containsKey("lines")) entry.setLines((List<NodeIndexEntry.Attribute>) values.get("lines"));
		if (values.containsKey("lines_below")) entry.setLinesBelow((List<NodeIndexEntry.Attribute>) values.get("lines_below"));
		if (values.containsKey("highlights")) entry.setHighlights((List<NodeIndexEntry.Attribute>) values.get("highlights"));
		if (values.containsKey("footers")) entry.setFooters((List<NodeIndexEntry.Attribute>) values.get("footers"));

		return entry;
	}

	public static NodeIndexEntry buildNodeIndexEntry(final Node entity, final String label) {
		return buildNodeIndexEntry(new HashMap<String, Object>() {{
			put("entity", entity);
			put("label", label);
		}});
	}

	public static NodeIndexEntry.Attribute buildNodeIndexEntryAttribute(String value, final String label) {
		client.core.system.NodeIndexEntry.Attribute attribute = new client.core.system.NodeIndexEntry.Attribute(value);
		attribute.setDefinition(new AttributeDefinition() {
			@Override
			public String getCode() {
				return null;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getLabel() {
				return label;
			}

			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public Type getType() {
				return null;
			}

			@Override
			public boolean is(Type type) {
				return false;
			}

			@Override
			public Precision getPrecision() {
				return null;
			}

			@Override
			public Ref getSource() {
				return null;
			}
		});
		return attribute;
	}

	public static Filter buildFilter(String name, String label) {
		return new client.core.system.Filter(name, label);
	}

	public static Order buildOrder(String name, String label, Order.Mode mode) {
		return new client.core.system.Order(name, label, mode);
	}
}
