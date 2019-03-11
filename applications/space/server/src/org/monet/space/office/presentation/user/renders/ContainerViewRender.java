package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.ContainerDefinition;
import org.monet.metamodel.ContainerDefinitionBase.ViewProperty.ShowProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.model.Node;

import java.util.HashMap;

public class ContainerViewRender extends NodeViewRender {
	private ContainerDefinition definition;

	public ContainerViewRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.node = (Node) target;
		this.definition = (ContainerDefinition) this.node.getDefinition();
	}

	protected void initContent(HashMap<String, Object> viewMap, ContainerDefinition.ViewProperty viewDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String shows = "";
		ShowProperty showDefinition = viewDefinition.getShow();

		if (showDefinition != null && showDefinition.getComponent() != null) {
			for (Ref componentDefinition : showDefinition.getComponent()) {
				String childDefinitionCode = this.dictionary.getDefinitionCode(componentDefinition.getDefinition());
				String idChild = this.node.getIndicatorValue("[" + childDefinitionCode + "].value");
				String idRevision = this.getParameterAsString("idrevision");
				Node childNode;

				if (!idRevision.isEmpty() && !idRevision.equals("-1"))
					childNode = this.renderLink.loadNodeRevision(idChild, idRevision);
				else
					childNode = this.renderLink.loadNode(idChild);

				String template = (!allowEdition(childNode)) ? block("template.readonly", map) : block("template", map);

				OfficeRender nodeRender = this.rendersFactory.get(childNode, template, this.renderLink, account);
				nodeRender.setParameters(this.getParameters());
				nodeRender.setParameter("view", componentDefinition.getValue());
				map.put("render(view.node)", nodeRender.getOutput());

				shows += block("show", map);
				map.clear();
			}
		}

		map.put("shows", shows);

		viewMap.put("content", block("content", map));
	}

	@Override
	protected String initView(String codeView) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ContainerDefinition.ViewProperty viewDefinition = (ContainerDefinition.ViewProperty) this.definition.getNodeView(codeView);

		if (this.node.requirePartnerContext() && this.node.getPartnerContext() == null) {
			String result = this.initPartnerContext();
			if (result != null) return result;
		}

		boolean isLocationView = codeView.equals("location");
		if (isLocationView) {
			this.initMapWithoutView(map, "location");
			return this.initLocationSystemView(map);
		} else if (viewDefinition == null) {
			map.put("codeView", codeView);
			map.put("labelDefinition", this.definition.getLabelString());
			return block("view.undefined", map);
		}

		this.initMap(map, viewDefinition);

		if (this.isSystemView(viewDefinition)) {
			return this.initSystemView(map, viewDefinition);
		}

		this.initContent(map, viewDefinition);

		return block("view", map);
	}

	@Override
	protected void init() {
		loadCanvas("view.node.container");
		super.init();
	}

}
