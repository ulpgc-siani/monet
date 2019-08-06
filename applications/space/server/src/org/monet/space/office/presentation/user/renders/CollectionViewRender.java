package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.CollectionDefinition;
import org.monet.metamodel.Definition;
import org.monet.metamodel.NodeViewProperty;
import org.monet.metamodel.SetDefinition.SetViewProperty;
import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.SelectProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CollectionViewRender extends SetViewRender {

	public CollectionViewRender() {
		super();
	}

	@Override
	protected ArrayList<String> getViewSelects(SetViewProperty view) {
		ArrayList<Ref> selectList = view.getSelect() != null ? view.getSelect().getNode() : new ArrayList<Ref>();
		ArrayList<String> result = new ArrayList<String>();

		if (selectList.size() <= 0) {
			ArrayList<Ref> addList = collectionDefinitionAdds(this.definition);

			for (Ref add : addList) {
				for (Definition addDefinition : this.dictionary.getAllImplementersOfNodeDefinition(add.getValue())) {
					if (addDefinition.isDisabled()) continue;
					result.add(addDefinition.getCode());
				}
			}

			return result;
		}

		for (Ref select : selectList)
			result.add(this.dictionary.getDefinitionCode(select.getValue()));

		return result;
	}

	protected Map<String, Map<String, Object>> toolbarNodesMap(SetViewProperty view) {
		SelectProperty selectDefinition = view.getSelect();
		CollectionDefinition definition = (CollectionDefinition) this.definition;
		ArrayList<Ref> addList = definition.getAdd().getNode();

		if (selectDefinition != null && selectDefinition.getNode().size() > 0) {
			ArrayList<Ref> selectList = selectDefinition.getNode();
			Map<String, Map<String, Object>> result = new HashMap<>();
			for (Ref select : selectList) {
				Definition child = this.dictionary.getDefinition(select.getValue());
				result.put(child.getCode(), nodeMapOf(child));
			}
			return result;
		} else if (definition.getToolbar() != null && definition.getToolbar().getAddOperation() != null) {
			return nodeMapOf(definition.getToolbar().getAddOperation().getEnable());
		}

		return nodeMapOf(addList);
	}

	@Override
	protected String initToolbarOperations(NodeViewProperty viewDefinition) {
		ArrayList<HashMap<String, Object>> operations = new ArrayList<HashMap<String, Object>>();
		String operationsValue = super.initToolbarOperations(viewDefinition);
		String nodeId = this.node.getId();

		if (!allowEdition(node))
			return operationsValue;

		for (Map<String, Object> nodeMap : toolbarNodesMap((SetViewProperty) viewDefinition).values()) {
			HashMap<String, Object> localMap = new HashMap<String, Object>();

			localMap.put("code", nodeMap.get("code"));
			localMap.put("label", LibraryString.cleanSpecialChars((String) nodeMap.get("label")));
			localMap.put("idParent", this.node.getId());
			localMap.put("description", LibraryString.cleanSpecialChars((String) nodeMap.get("description")));
			localMap.put("command", "addnode(blank," + nodeMap.get("code") + ",null,null," + nodeId + ")");

			if (this.isOwnedPrototypesSystemView(viewDefinition)) {
				localMap.put("commandPrototype", "addprototype(" + nodeMap.get("code") + ",null,null," + nodeId + ")");
			} else if (this.isSharedPrototypesSystemView(viewDefinition))
				localMap.put("commandPrototype", "addprototype(" + nodeMap.get("code") + ",null,null,null)");

			operations.add(localMap);
		}

		if (!this.isPrototypesSystemView(viewDefinition)) {
			for (Node prototype : this.node.getPrototypes()) {
				HashMap<String, Object> localMap = new HashMap<String, Object>();
				String description = prototype.getDescription();

				localMap.put("code", prototype.getId());
				localMap.put("label", prototype.getLabel());
				localMap.put("idParent", this.node.getId());
				localMap.put("description", description != null ? LibraryString.cleanSpecialChars(description) : "");
				localMap.put("command", "copynode(" + prototype.getId() + ",edit.html?mode=page," + this.node.getId() + ")");

				operations.add(localMap);
			}
		}

		for (HashMap<String, Object> add : operations) {
			String blockName = "toolbar$operation.add";

			if (this.isPrototypesSystemView(viewDefinition))
				blockName = "toolbar$operation.addPrototype";

			operationsValue += block(blockName, add);
		}

		return operationsValue;
	}

	@Override
	protected String initAddList(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String addList = "";

		for (Map<String, Object> nodeMap : toolbarNodesMap(viewDefinition).values()) {
			map.put("code", nodeMap.get("code"));
			map.put("label", LibraryString.cleanSpecialChars((String) nodeMap.get("label")));
			map.put("description", LibraryString.cleanSpecialChars((String) nodeMap.get("description")));
			map.put("command", "addnode(blank," + nodeMap.get("code") + ",null,null," + this.node.getId() + ")");
			addList += block("addList$item", map);
			map.clear();
		}

		if (!this.isPrototypesSystemView(viewDefinition)) {
			for (Node prototype : this.node.getPrototypes()) {
				String description = prototype.getDescription();

				map.put("code", prototype.getId());
				map.put("label", prototype.getLabel());
				map.put("description", description != null ? LibraryString.cleanSpecialChars(description) : "");
				map.put("command", "copynode(" + prototype.getId() + ",edit.html?mode=page," + this.node.getId() + ")");
				addList += block("addList$item", map);
				map.clear();
			}
		}

		return addList;
	}

	@Override
	protected String initMagnets(HashMap<String, Object> viewMap, SetViewProperty view) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String magnets = "";

		for (String codeNode : nodesMap(view).keySet()) {
			map.put("code", codeNode);
			magnets += block("magnet", map);
		}

		return magnets;
	}

	@Override
	protected String initView(String codeView) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		SetViewProperty view = (SetViewProperty) this.definition.getNodeView(codeView);

		if (this.node.requirePartnerContext() && this.node.getPartnerContext() == null) {
			String result = this.initPartnerContext();
			if (result != null) return result;
		}

		String result = initViewFromCode(codeView, view, map);
		if (result != null) return result;

		this.initMap(map, view);
		map.put("clec", "clec");

		if (this.isSystemView(view)) {
			return this.initSystemView(map, view);
		}

		this.initContent(map, view);

		return block("view", map);
	}

	@Override
	protected void init() {
		loadCanvas("view.node.collection");
		super.init();
	}

}
