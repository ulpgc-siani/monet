package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.*;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty.ShowProperty.AttachmentsProperty;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty.ShowProperty.AttachmentsProperty.FieldNodeProperty;
import org.monet.metamodel.NodeDefinitionBase.OperationProperty;
import org.monet.metamodel.NodeFieldPropertyBase.AddProperty;
import org.monet.metamodel.SetDefinition.SetViewProperty;
import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.ShowProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.model.*;
import org.monet.space.office.configuration.Configuration;
import org.monet.space.office.core.model.Language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeViewRender extends ViewRender {
	protected Node node;

	public NodeViewRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.node = (Node) target;
	}

	protected String initToolbar(NodeViewProperty viewDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String operations = this.initToolbarOperations(viewDefinition);

		map.put("codeNode", this.node.getCode());
		map.put("idNode", this.node.getId());
		map.put("operations", operations);

		if (viewDefinition != null)
			map.put("codeView", viewDefinition.getCode());

			if (this.isSystemView(viewDefinition) && !this.isPrototypesSystemView(viewDefinition)) {

				if (this.isAttachmentsSystemView(viewDefinition)) {
					FormDefinition formDefinition = (FormDefinition) this.node.getDefinition();
					FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
					AttachmentsProperty attachmentsDefinition = showDefinition.getAttachments();
					org.monet.space.office.core.model.Language language = Language.getInstance();

					for (FieldNodeProperty fieldNodeDefinition : attachmentsDefinition.getFieldNodeList()) {
						Ref field = fieldNodeDefinition.getField();
						Ref exporter = fieldNodeDefinition.getExporter();
						NodeFieldProperty fieldDefinition = (NodeFieldProperty) formDefinition.getField(field.getValue());
						AddProperty addDefinition = fieldDefinition.getAdd();

						if (addDefinition != null) {
							String exporterCode = "";

							if (exporter != null)
								exporterCode = this.dictionary.getDefinitionCode(exporter.getValue());

							for (Ref add : addDefinition.getNode()) {
								HashMap<String, Object> localMap = new HashMap<String, Object>();
								Attribute attribute = this.node.getAttributeList().getFirstByCode(fieldDefinition.getCode());

								if (!fieldDefinition.isMultiple() && attribute != null && attribute.getAttributeList().getCount() > 0)
									continue;

								localMap.put("label", language.getModelResource(fieldDefinition.getLabel()));
								localMap.put("idNode", this.node.getId());
								localMap.put("code", fieldDefinition.getCode());
								localMap.put("path", formDefinition.getFieldPath(fieldDefinition.getCode()));
								localMap.put("multiple", fieldDefinition.isMultiple()?"true":"false");
								localMap.put("codeNode", this.dictionary.getDefinitionCode(add.getValue()));
								localMap.put("exporterCode", exporterCode);

								operations += block("toolbar.systemView.attachments$addNode", localMap);
							}
						}
					}

					for (Ref field : attachmentsDefinition.getFieldFile()) {
						FileFieldProperty fieldDefinition = (FileFieldProperty) formDefinition.getField(field.getValue());
						HashMap<String, Object> localMap = new HashMap<String, Object>();

						localMap.put("label", language.getModelResource(fieldDefinition.getLabel()));
						localMap.put("idNode", this.node.getId());
						localMap.put("code", fieldDefinition.getCode());
						localMap.put("path", formDefinition.getFieldPath(fieldDefinition.getCode()));
						localMap.put("multiple", fieldDefinition.isMultiple());

						operations += block("toolbar.systemView.attachments$addFile", localMap);
					}

					map.put("operations", operations);

					return block("toolbar.systemView.attachments", map);
				} else
					return block("toolbar.systemView", map);
			}

		return block("toolbar", map);
	}

	protected String initToolbarOperations(NodeViewProperty viewDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		StringBuilder operations = new StringBuilder();

		if (!this.node.isPrototype()) {
			Language language = Language.getInstance();
			String nodeId = this.node.getId();
			for (OperationProperty operationDefinition : this.node.getDefinition().getOperationList()) {

				if (!canExecuteOperation(node, operationDefinition))
					continue;

				map.put("id", nodeId);
				map.put("code", operationDefinition.getCode());
				map.put("name", operationDefinition.getName());
				map.put("label", language.getModelResource(operationDefinition.getLabel()));
				map.put("visible", node.getState().existsFlagForOperation(operationDefinition.getCode()) ? "false" : "true");

				operations.append(block("operation", map));
				map.clear();
			}
		}

		return operations.toString();
	}

	protected String initToolbarWithoutView() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("idNode", this.node.getId());

		return block("toolbar", map);
	}

	protected String initNotesSystemView(NodeViewProperty view, HashMap<String, Object> contentMap) {
		contentMap.put("type", "notes");
		return block("content.notes", contentMap);
	}

	protected String initLocationSystemView(HashMap<String, Object> contentMap) {
		contentMap.put("idNode", this.node.getId());
		contentMap.put("codeDefinition", this.node.getDefinition().getCode());
		contentMap.put("codeView", "location");
		contentMap.put("content", block("content.location", contentMap));

		return block("view", contentMap);
	}

	protected String initAncestorView(HashMap<String, Object> contentMap) {
		if (!node.isSet()) return "";
		String index = (String) getParameter("index");
		if (index == null) return "";
		int previous = Integer.parseInt((String) getParameter("index")) - 1;
		int next = Integer.parseInt((String) getParameter("index")) + 1;
		int count = Integer.parseInt((String) getParameter("count"));
		Configuration configuration = Configuration.getInstance();
		contentMap.put("idNode", node.getId());
		contentMap.put("previous", previous);
		contentMap.put("next", next);
		contentMap.put("count", count);
		contentMap.put("themeSource", configuration.getApiUrl() + "?op=loadthemefile");
		contentMap.put("previousBlock", block("view.ancestor$previous." + (previous >= 0 ? "enabled" : "disabled"), contentMap));
		contentMap.put("nextBlock", block("view.ancestor$next." + (next < count ? "enabled" : "disabled"), contentMap));
		return block("view.ancestor", contentMap);
	}

	private boolean existsLinksInToComponentDefinitions(NodeViewProperty viewDefinition) {
		ArrayList<Ref> refList = null;

		if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
			ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
			if (showDefinition.getLinksIn() != null)
				refList = showDefinition.getLinksIn().getNode();
		}

		if (viewDefinition instanceof FormViewProperty) {
			FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
			if (showDefinition.getLinksIn() != null)
				refList = showDefinition.getLinksIn().getNode();
		}

		return existsLinksToComponentDefinitions(refList);
	}

	private boolean existsLinksOutToComponentDefinitions(NodeViewProperty viewDefinition) {
		ArrayList<Ref> refList = null;

		if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
			ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
			if (showDefinition.getLinksOut() != null)
				refList = showDefinition.getLinksOut().getNode();
		}

		if (viewDefinition instanceof FormViewProperty) {
			FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
			if (showDefinition.getLinksOut() != null)
				refList = showDefinition.getLinksOut().getNode();
		}

		return existsLinksToComponentDefinitions(refList);
	}

	private boolean existsLinksToComponentDefinitions(List<Ref> refList) {

		if (refList == null)
			return false;

		for (Ref ref : refList) {
			NodeDefinition definition = this.dictionary.getNodeDefinition(ref.getValue());
			if (definition.isComponent())
				return true;
		}

		return false;
	}

	protected String initLinksInSystemView(NodeViewProperty viewDefinition, HashMap<String, Object> contentMap) {
		contentMap.put("type", "links_in");

		if (this.existsLinksInToComponentDefinitions(viewDefinition))
			return block("content.links$existsLinksToComponents", contentMap);

		return block("content.linksIn", contentMap);
	}

	protected String initLinksOutSystemView(NodeViewProperty viewDefinition, HashMap<String, Object> contentMap) {
		contentMap.put("type", "links_out");

		if (this.existsLinksOutToComponentDefinitions(viewDefinition))
			return block("content.links$existsLinksToComponents", contentMap);

		return block("content.linksOut", contentMap);
	}

	protected String initAttachmentsSystemView(NodeViewProperty viewDefinition, HashMap<String, Object> contentMap) {
		contentMap.put("type", "attachments");
		return block("content.attachments", contentMap);
	}

	protected String initRecentTaskSystemView(NodeViewProperty view, HashMap<String, Object> contentMap) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		TaskList taskList = this.node.getLinkedTasks();

		contentMap.put("type", "tasks");

		if (taskList.getCount() <= 0)
			return block("content.recenttask$empty", map);

		OfficeRender render = this.rendersFactory.get(taskList.get(0), this.template, this.renderLink, account);
		render.setParameter("view", "state");
		map.put("render(view.recenttask)", render.getOutput());

		return block("content.recenttask", map);
	}

	protected String initTasksSystemView(NodeViewProperty view, HashMap<String, Object> contentMap) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		TaskList taskList = this.node.getLinkedTasks();

		contentMap.put("type", "tasks");

		if (taskList.getCount() <= 0)
			return block("content.tasks$empty", map);

		OfficeRender render = this.rendersFactory.get(taskList, this.template, this.renderLink, account);
		render.setParameter("view", "custom");
		map.put("render(view.tasklist)", render.getOutput());

		return block("content.tasks", map);
	}

	protected String initPrototypesSystemView(NodeViewProperty viewDefinition, HashMap<String, Object> contentMap) {
		SetViewProperty setDefinition = ((SetViewProperty) viewDefinition);
		ShowProperty showDefinition = setDefinition.getShow();
		String type = "";

		if (showDefinition.getOwnedPrototypes() != null)
			type = "ownedprototypes";
		else
			type = "sharedprototypes";

		contentMap.put("type", type);
		contentMap.put("addList", "");
		contentMap.put("sortByList", "");
		contentMap.put("groupByList", "");
		return block("content.prototypes", contentMap);
	}

	protected String initViewFromCode(String codeView, ViewProperty view, HashMap<String, Object> map) {
		if (codeView.equals("location")) {
			this.initMapWithoutView(map, "location");
			return this.initLocationSystemView(map);
		} else if (codeView.equals("ancestor")) {
			return this.initAncestorView(map);
		} else if (view == null) {
			map.put("codeView", codeView);
			map.put("labelDefinition", node.getDefinition().getLabelString());
			return block("view.undefined", map);
		}
		return null;
	}

	protected String initSystemView(HashMap<String, Object> viewMap, NodeViewProperty viewDefinition) {

		viewMap.put("clec", "clec");
		viewMap.put("idNode", this.node.getId());
		viewMap.put("codeDefinition", this.node.getDefinition().getCode());
		viewMap.put("codeView", viewDefinition.getCode());

		if (this.isNotesSystemView(viewDefinition))
			viewMap.put("content", this.initNotesSystemView(viewDefinition, viewMap));
		if (this.isLocationSystemView(viewDefinition))
			viewMap.put("content", this.initLocationSystemView(viewMap));
		else if (this.isLinksInSystemView(viewDefinition))
			viewMap.put("content", this.initLinksInSystemView(viewDefinition, viewMap));
		else if (this.isLinksOutSystemView(viewDefinition))
			viewMap.put("content", this.initLinksOutSystemView(viewDefinition, viewMap));
		else if (this.isAttachmentsSystemView(viewDefinition))
			viewMap.put("content", this.initAttachmentsSystemView(viewDefinition, viewMap));
		else if (this.isRecentTaskSystemView(viewDefinition))
			viewMap.put("content", this.initRecentTaskSystemView(viewDefinition, viewMap));
		else if (this.isTasksSystemView(viewDefinition))
			viewMap.put("content", this.initTasksSystemView(viewDefinition, viewMap));
		else if (this.isRevisionsSystemView(viewDefinition)) {
			viewMap.put("type", "revisions");
			viewMap.put("content", block("content.revisions", viewMap));
		} else if (this.isPrototypesSystemView(viewDefinition))
			viewMap.put("content", this.initPrototypesSystemView(viewDefinition, viewMap));

		return block("view", viewMap);
	}

	protected void initMap(HashMap<String, Object> map, NodeViewProperty view) {
		NodeState state = this.node.getState();

		map.put("idNode", this.node.getId());
		map.put("codeNode", this.node.getCode());
		map.put("type", this.getNodeType(this.node));
		map.put("from", this.getParameterAsString("from"));
		map.put("codeView", view != null ? view.getCode() : "");
		map.put("page", this.getParameterAsString("page"));
		map.put("clec", "");
		map.put("toolbar", this.initToolbar(view));
		map.put("timestamp", String.valueOf(this.node.getReference().getUpdateDate().getValue().getTime()));
		map.put("state", state != null ? state.toJson().toString() : "");
	}

	protected void initMapWithoutView(HashMap<String, Object> map, String codeView) {
		map.put("idNode", this.node.getId());
		map.put("codeNode", this.node.getCode());
		map.put("type", this.getNodeType(this.node));
		map.put("from", this.getParameterAsString("from"));
		map.put("codeView", codeView);
		map.put("page", this.getParameterAsString("page"));
		map.put("clec", "");
		map.put("toolbar", this.initToolbarWithoutView());
		map.put("timestamp", String.valueOf(this.node.getReference().getUpdateDate().getValue().getTime()));
	}

	protected String initPartnerContext() {
		List<String> ontologies = this.dictionary.getOntologies(this.node.getDefinition().getCode());

		if (ontologies == null)
			return null;

		Map<String, FederationUnit> partnersMap = this.renderLink.loadSourceListPartners(ontologies);
		HashMap<String, Object> map = new HashMap<>();
		StringBuffer contexts = new StringBuffer();

		if (partnersMap.size() == 0)
			return block("view.requirePartnerContext.empty", map);

		for (FederationUnit partner : partnersMap.values()) {
			if (!partner.complyOntologies(ontologies))
				continue;

			HashMap<String, Object> localMap = new HashMap<String, Object>();
			localMap.put("id", this.node.getId());
			localMap.put("partnerName", partner.getName());
			localMap.put("partnerLabel", partner.getLabel());
			contexts.append(block("view.requirePartnerContext$context", localMap));
		}

		map.put("partnerContexts", contexts.toString());

		return block("view.requirePartnerContext", map);
	}

	@Override
	protected void init() {
		loadCanvas("view.node");
		super.init();
	}

}
