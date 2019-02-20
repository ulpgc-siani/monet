package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.explorer.model.NodeIndexEntry;
import org.monet.space.kernel.model.Node;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NodeIndexEntrySerializer extends IndexEntrySerializer<NodeIndexEntry> {

	public NodeIndexEntrySerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(NodeIndexEntry entry) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(NodeIndexEntry.class, this);
		return builder.create().toJsonTree(entry, NodeIndexEntry.class);
	}

	@Override
	public JsonElement serialize(NodeIndexEntry entry, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(entry, type, jsonSerializationContext);
		IndexDefinition indexDefinition = entry.getDefinition();

		if (indexDefinition instanceof DescriptorDefinition)
			return result;

		IndexDefinition.IndexViewProperty.ShowProperty showDefinition = entry.getViewDefinition().getShow();
		Node entity = entry.getEntity();

		result.addProperty("geoReferenced", entity.getReference().isGeoReferenced());
		result.add("title", getAttribute(entry, indexDefinition, showDefinition.getTitle()));
		result.add("picture", getAttribute(entry, indexDefinition, showDefinition.getPicture()));
		result.add("icon", getAttribute(entry, indexDefinition, showDefinition.getIcon()));
		result.add("highlights", getAttributes(entry, indexDefinition, showDefinition.getHighlight()));
		result.add("lines", getAttributes(entry, indexDefinition, showDefinition.getLine()));
		result.add("linesBelow", getAttributes(entry, indexDefinition, showDefinition.getLineBelow()));
		result.add("footers", getAttributes(entry, indexDefinition, showDefinition.getFooter()));

		return result;
	}

	private JsonElement getAttribute(NodeIndexEntry entry, IndexDefinition indexDefinition, Ref ref) {
		if (ref == null)
			return null;

		String attributeCode = indexDefinition.getAttribute(ref.getValue()).getCode();

		JsonObject attribute = new JsonObject();
		attribute.addProperty("code", attributeCode);
		attribute.addProperty("value", entry.getAttributeValue(attributeCode));

		return attribute;
	}

	private JsonElement getAttributes(NodeIndexEntry entry, IndexDefinition indexDefinition, ArrayList<Ref> refs) {
		JsonArray items = new JsonArray();

		for (Ref ref : refs)
			items.add(getAttribute(entry, indexDefinition, ref));

		JsonObject result = new JsonObject();
		result.addProperty("totalCount", refs.size());
		result.add("items", items);

		return result;
	}

}
