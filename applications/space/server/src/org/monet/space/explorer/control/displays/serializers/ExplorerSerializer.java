package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.monet.metamodel.SourceDefinition;
import org.monet.space.explorer.model.Language;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.model.*;

public abstract class ExplorerSerializer {
	protected Helper helper;

	public ExplorerSerializer(Helper helper) {
		this.helper = helper;
	}

	public void setHelper(Helper helper) {
		this.helper = helper;
	}

	public interface Helper {
		Dictionary getDictionary();
		Language getLanguage();
		String constructApiUrl(String operationWithParams);
		Node loadContainerChildNode(Node node, String childDefinitionCode);
		Node locateNode(String code);
		Node loadNode(String id);
		TaskOrder loadTaskOrder(String orderId);
		Source<SourceDefinition> loadSource(String id);
		Source<SourceDefinition> locateSource(String codeSource, FeederUri uri);
		TermList loadSourceTerms(String sourceId, DataRequest dataRequest);
		AgentFilesystem getAgentFilesystem();
	}

	protected <T> JsonObject toListObject(JsonArray items) {
		JsonObject result = new JsonObject();
		result.addProperty("totalCount", items.size());
		result.add("items", items);
		return result;
	}
}
