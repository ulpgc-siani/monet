package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.metamodel.Definition;
import org.monet.metamodel.DesktopDefinition;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.NodeViewProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Node;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DesktopSerializer extends NodeSerializer<DesktopDefinition> {

	public DesktopSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(Node node, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(node, type, jsonSerializationContext);

		result.add("singletons", serializeSingletons(node, jsonSerializationContext));

		return result;
	}

	private JsonObject serializeSingletons(Node node, JsonSerializationContext jsonSerializationContext) {
		Dictionary dictionary = helper.getDictionary();
		DesktopDefinition desktopDefinition = (DesktopDefinition)node.getDefinition();
		JsonArray result = new JsonArray();

		for (NodeViewProperty viewDefinition : desktopDefinition.getViewDefinitionList()) {
			ArrayList<Ref> links = ((DesktopDefinition.ViewProperty) viewDefinition).getShow().getLink();

			for (Ref link : links) {
				Definition definition = dictionary.getDefinition(link.getValue());

				if (! (definition instanceof NodeDefinition))
					continue;

				String linkCode = dictionary.getDefinitionCode(link.getValue());
				Node singleton = helper.locateNode(linkCode);
				result.add(serializeNode(singleton));
			}
		}

		return toListObject(result);
	}

}
