package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.NodeFieldProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;
import org.monet.space.kernel.model.Node;
import org.monet.space.office.configuration.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodeFieldViewRender extends FieldViewRender {

	public NodeFieldViewRender() {
		super();
	}

	@Override
	protected String getFieldType() {
		return "node";
	}

	@Override
	protected String initBody(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, Attribute attribute, boolean isTemplate) {
		Configuration configuration = Configuration.getInstance();
		HashMap<String, Object> map = new HashMap<String, Object>();
		String idNode = this.getIndicatorValue(attribute, Indicator.CODE);
		String nodeContent = "";
		NodeFieldProperty fieldDefinition = (NodeFieldProperty) this.definition;
		List<Ref> addList = fieldDefinition.getAdd() != null ? fieldDefinition.getAdd().getNode() : new ArrayList<Ref>();
		NodeFieldProperty.ContainProperty containDefinition = fieldDefinition.getContain();
		String nameNode = null;
		Node node = null;

		if ((!idNode.isEmpty()) && (!idNode.equals("-1")))
			node = this.renderLink.loadNode(idNode);

		map.put("nodeTypes", "");
		map.put("allowAdd", "false");
		if (addList.size() > 0) {
			nameNode = fieldDefinition.getAdd().getNode().get(0).getValue();
			map.put("allowAdd", "true");
		} else if (containDefinition != null)
			nameNode = containDefinition.getNode().getValue();

		if (nameNode != null) {
			NodeDefinition definition = this.dictionary.getNodeDefinition(nameNode);
			map.put("nodeTypes", definition.getCode());
			map.put("codeView", "form");
			if (definition.getDefaultView() != null)
				map.put("codeView", definition.getDefaultView().getCode());
			declarationsMap.put("concreteDeclarations", (definition.isDocument()) ? block("field.node$concreteDeclarations.document", map) : block("field.node$concreteDeclarations", map));
		}

		if (node != null) {

			if (node.isDocument()) {
				HashMap<String, Object> localMap = new HashMap<String, Object>();
				localMap.put("id", node.getId());
				localMap.put("label", node.getDefinition().getLabelString());
				localMap.put("previewImageSource", configuration.getApiUrl() + "?op=previewnode&id=" + node.getId() + "&thumb=1&r=" + Math.random());
				map.put("render(view.node)", block("field.node$node.document", localMap));
			} else {
				String codeView = node.getDefinition().getDefaultView().getCode();
				OfficeRender render = this.rendersFactory.get(node, this.template, this.renderLink, account);

				render.setParameters(this.getParameters());
				render.setParameter("view", codeView);
				render.setParameter("isRoot", "false");
				map.put("render(view.node)", render.getOutput());
			}

			nodeContent = block("field.node$node", map);
			map.clear();
		} else {
			nodeContent = block("field.node$node.empty", map);
		}

		map.put("id", id);
		map.put("code", this.getIndicatorValue(attribute, Indicator.CODE));
		map.put("value", this.getIndicatorValue(attribute, Indicator.VALUE));
		map.put("node", nodeContent);

		return block("field.node", map);
	}

}
