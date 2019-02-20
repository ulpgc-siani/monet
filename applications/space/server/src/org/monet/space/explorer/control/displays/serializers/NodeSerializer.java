package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.metamodel.NodeDefinition;
import org.monet.space.explorer.control.displays.serializers.factory.NodeTypeAdapterFactory;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.NodeList;
import org.monet.space.kernel.model.Source;

import java.lang.reflect.Type;
import java.util.Map;

public class NodeSerializer<T extends NodeDefinition> extends EntitySerializer<Node<T>, T> {

	public NodeSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(Node node) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapterFactory(new NodeTypeAdapterFactory(helper));
		builder.registerTypeAdapter(Source.class, new SourceSerializer(helper));
		return builder.create().toJsonTree(node, node.getClass());
	}

	@Override
	public JsonElement serialize(Node node, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(node, type, jsonSerializationContext);

		if (node.isComponent())
			result.addProperty("isComponent", node.isComponent());

		if (node.isLocked())
			result.addProperty("isLocked", node.isLocked());

		result.addProperty("type", node.getDefinition().getType().toString());

		if (node.getNotes().size() > 0)
			result.add("notes", serializeNotes(node.getNotes()));

		if (node.getParent() != null)
			result.add("owner", serializeNode(node.getParent()));

		if (node.getAncestors() != null && node.getAncestors().getCount() > 0)
			result.add("ancestors", serializeAncestors(node.getAncestors(), jsonSerializationContext));

		return result;
	}

	private JsonElement serializeNotes(Map<String, String> notes) {
		Gson gson = new GsonBuilder().create();
		return gson.toJsonTree(notes);
	}

	private JsonObject serializeAncestors(NodeList ancestors, JsonSerializationContext jsonSerializationContext) {
		JsonArray result = new JsonArray();

		for (Node ancestor : ancestors)
			result.add(serializeNode(ancestor));

		return toListObject(result);
	}

	protected JsonObject serializeNode(Node node) {
		JsonObject result = new JsonObject();
		result.addProperty("id", node.getId());
		result.addProperty("type", node.getType().toString());
		result.addProperty("label", node.getLabel());
		result.add("definition", serializeDefinition(node.getDefinition()));
		return result;
	}

}
