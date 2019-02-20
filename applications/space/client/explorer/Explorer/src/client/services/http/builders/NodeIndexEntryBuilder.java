package client.services.http.builders;

import client.core.model.List;
import client.core.model.Node;
import client.core.system.MonetList;
import client.core.system.NodeIndexEntry;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import com.google.gwt.core.client.JsArray;

public class NodeIndexEntryBuilder extends IndexEntryBuilder<Node, client.core.model.NodeIndexEntry> implements Builder<client.core.model.NodeIndexEntry, List<client.core.model.NodeIndexEntry>> {

	@Override
	public client.core.model.NodeIndexEntry build(HttpInstance instance) {
		if (instance == null)
			return null;

		NodeIndexEntry entry = new NodeIndexEntry();
		initialize(entry, instance);
		return entry;
	}

	@Override
	public void initialize(client.core.model.NodeIndexEntry object, HttpInstance instance) {
		super.initialize(object, instance);

		AttributeBuilder creator = new AttributeBuilder();
		NodeIndexEntry entry = (NodeIndexEntry)object;
		entry.setTypeFactory(new EntityBuilder<>());
		entry.setGeoReferenced(instance.getBoolean("geoReferenced"));
		entry.setTitle(creator.build(instance.getObject("title")));
		entry.setPicture(creator.build(instance.getObject("picture")));
		entry.setIcon(creator.build(instance.getObject("icon")));
		entry.setHighlights(creator.buildList(instance.getList("highlights")));
		entry.setLines(creator.buildList(instance.getList("lines")));
		entry.setLinesBelow(creator.buildList(instance.getList("linesBelow")));
		entry.setFooters(creator.buildList(instance.getList("footers")));
	}

	@Override
	public List<client.core.model.NodeIndexEntry> buildList(HttpList instance) {
		List<client.core.model.NodeIndexEntry> result = new MonetList<>();
		JsArray<HttpInstance> items = instance.getItems();

		for (int i = 0; i < items.length(); i++)
			result.add(build(items.get(i)));

		result.setTotalCount(instance.getTotalCount());

		return result;
	}

	@Override
	protected EntityBuilder getEntityBuilder() {
		return new NodeBuilder();
	}

	private class AttributeBuilder implements Builder<client.core.model.NodeIndexEntry.Attribute, List<client.core.model.NodeIndexEntry.Attribute>> {

		@Override
		public client.core.model.NodeIndexEntry.Attribute build(HttpInstance instance) {
			if (instance == null)
				return null;

			NodeIndexEntry.Attribute attribute = new NodeIndexEntry.Attribute();
			initialize(attribute, instance);
			return attribute;
		}

		@Override
		public void initialize(client.core.model.NodeIndexEntry.Attribute object, HttpInstance instance) {
			NodeIndexEntry.Attribute attribute = (NodeIndexEntry.Attribute)object;
			attribute.setCode(instance.getString("code"));
			attribute.setValue(instance.getString("value"));
			attribute.setLink(new OperationBuilder().build(instance.getObject("link")));
		}

		@Override
		public List<client.core.model.NodeIndexEntry.Attribute> buildList(HttpList instance) {
			List<client.core.model.NodeIndexEntry.Attribute> result = new MonetList<>();
			JsArray<HttpInstance> items = instance.getItems();

			for (int i = 0; i < items.length(); i++)
				result.add(build(items.get(i)));

			result.setTotalCount(instance.getTotalCount());

			return result;
		}
	}

	private class OperationBuilder implements Builder<client.core.model.NodeIndexEntry.Operation, List<client.core.model.NodeIndexEntry.Operation>> {

		@Override
		public client.core.model.NodeIndexEntry.Operation build(HttpInstance instance) {
			if (instance == null)
				return null;

			NodeIndexEntry.Operation operation = new NodeIndexEntry.Operation();
			initialize(operation, instance);
			return operation;
		}

		@Override
		public void initialize(client.core.model.NodeIndexEntry.Operation object, HttpInstance instance) {
		}

		@Override
		public List<client.core.model.NodeIndexEntry.Operation> buildList(HttpList instance) {
			return null;
		}
	}

}
