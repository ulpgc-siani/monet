package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.ContainerDefinition;
import org.monet.metamodel.ContainerDefinitionBase;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.model.Node;

import java.lang.reflect.Type;

public class ContainerSerializer extends NodeSerializer<ContainerDefinition> {

	public ContainerSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(Node node, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(node, type, jsonSerializationContext);

		result.add("children", serializeChildren(node, jsonSerializationContext));

		return result;
	}

	private JsonObject serializeChildren(Node node, JsonSerializationContext jsonSerializationContext) {
		ContainerDefinition definition = (ContainerDefinition)node.getDefinition();
		ContainerDefinitionBase.ContainProperty containDefinition = definition.getContain();
		JsonArray result = new JsonArray();

		if (containDefinition == null)
			return toListObject(result);

		for (Ref containRef : containDefinition.getNode()) {
			String definitionCode = helper.getDictionary().getDefinitionCode(containRef.getValue());
			Node childNode = helper.loadContainerChildNode(node, definitionCode);
			result.add(serializeNode(childNode));
		}

		return toListObject(result);
	}

}
