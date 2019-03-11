package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.ContainerDefinition;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.NodeDefinitionBase.OperationProperty;
import org.monet.metamodel.NodeViewProperty;
import org.monet.space.kernel.constants.LabelCode;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.library.LibraryDate.Format;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.*;
import org.monet.space.office.core.model.Language;

import java.util.HashMap;
import java.util.List;

public class NodePageRender extends OfficeRender {
	protected Node node;
	protected NodeDefinition definition;

	public NodePageRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.node = (Node) target;
		this.definition = this.node.getDefinition();
	}

	@Override
	protected void init() {
		loadCanvas("page.node");

		addMark("id", this.node.getId());
		addMark("type", this.getNodeType(this.node));
		addMark("mode", this.node.isPrototype() ? "prototype" : "");

		this.initControlInfo();
		this.initConfiguration();
		this.initHeader();
		this.initMessages();
		this.initTabs();
	}

	protected void initControlInfo() {
		String description = this.node.getDescription();
		NodeState state = this.node.getState();

		addMark("code", this.node.getCode());
		addMark("label", this.node.getLabel());
		addMark("description", (description != null && !description.isEmpty()) ? description : "&nbsp;");
		addMark("date", this.node.getCreateDate());
		addMark("ancestorsIds", this.node.getAncestorsIds(","));
		addMark("from", this.getParameterAsString("from"));
		addMark("labelEditable", "false");
		addMark("addList", "");
		addMark("timestamp", String.valueOf(this.node.getReference().getUpdateDate().getValue().getTime()));
		addMark("state", state != null ? state.toJson().toString() : "");
	}

	protected void initHeader() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		User owner = this.node.getOwner();
		String idRevision = this.getParameterAsString("idrevision");
		boolean isRoot = this.account.getRootNode().getId().equals(this.node.getId());

		map.put("createDate", LibraryDate.getDateAndTimeString(this.node.getReference().getCreateDate().getValue(), Language.getCurrent(), Language.getCurrentTimeZone(), Format.INTERNAL, true, Strings.BAR45));
		map.put("owner", owner != null ? owner.getInfo().getFullname() : "");
		map.put("label", this.node.getLabel());
		map.put("shortLabel", LibraryString.shortValue(this.node.getLabel()));
		map.put("shortBreadcrumbsLabel", LibraryString.shortValue(this.node.getLabel(), 30));
		map.put("idNode", this.node.getId());
		map.put("description", this.node.getDescription());
		map.put("shortDescription", LibraryString.shortValue(this.node.getDescription()));
		map.put("flags", (node.isLocked() || !canEditWithRole()) ? block("flag.locked", null) : "");

		this.initPrototyped(map);
		this.initBreadCrumbs(map);

		if (!idRevision.isEmpty()) {
			String revisionBlock = "revision";
			if (!idRevision.equals("-1")) {
				Revision revision = this.renderLink.loadRevision(idRevision);
				map.put("revisionDate", LibraryDate.getDateAndTimeString(revision.getRevisionDate(), Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.INTERNAL, true, "/"));
			} else {
				map.put("revisionDate", "");
				revisionBlock = "revision.current";
			}

			map.put("revision", block(revisionBlock, map));
		} else
			map.put("revision", block("noRevision", map));

		if (isRoot || (this.node.getParent() == null && !this.definition.isSingleton()))
			addMark("header", block("header.desktop", map));
		else
			addMark("header", block("header.other", map));
	}

	protected void initPrototyped(HashMap<String, Object> headerMap) {
		if (this.node.isPrototyped()) {
			Node node = this.renderLink.loadNode(this.node.getPrototypeId());

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("label", this.node.getLabel());
			map.put("prototypeId", this.node.getPrototypeId());
			map.put("prototypeLabel", node.getLabel());
			map.put("shortPrototypeLabel", LibraryString.shortValue(node.getLabel()));
			headerMap.put("prototype", block("prototype", map));
		} else headerMap.put("prototype", "");
	}

	protected void initBreadCrumbs(HashMap<String, Object> headerMap) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String breadCrumbs = "", breadCrumbsRevisions = "";
		Session session = Context.getInstance().getCurrentSession();
		String idRevision = this.getParameterAsString("idrevision");

		map.put("id", this.node.getId());
		map.put("label", this.node.getLabel());
		map.put("shortLabel", LibraryString.shortValue(this.node.getLabel(), 30));

		if (!idRevision.isEmpty())
			breadCrumbsRevisions = block("breadcrumbs$revisions", map);
		else
			breadCrumbsRevisions = block("breadcrumbs$noRevisions", map);

		map.clear();

		if (this.node.isPrototype()) {
			headerMap.put("breadcrumbs", (String) session.getVariable("breadcrumbs"));
			return;
		}

		if (!node.getDefinition().isBreadcrumbsDisabled())
			for (Node ancestor : node.getAncestors()) {
				if (ancestor.isComponent()) continue;
				map.put("id", ancestor.getId());
				map.put("label", ancestor.getLabel());
				map.put("shortLabel", LibraryString.shortValue(ancestor.getLabel(), 30));
				breadCrumbs += block("breadcrumb", map);
				map.clear();
			}

		session.setVariable("breadcrumbs", breadCrumbs);

		headerMap.put("breadcrumbs", breadCrumbs);
		headerMap.put("breadcrumbsRevisions", breadCrumbsRevisions);
	}

	protected boolean hasAncestorLocked() {
		Node parent = this.node.getParent();
		boolean locked = false;

		while (parent != null) {
			if (parent.isLocked()) return true;
			parent = parent.getParent();
		}

		return locked;
	}

	protected void initConfiguration() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String operations = "";
		boolean isRevision = this.getParameter("idrevision") != null;

		if (isRevision) {
			map.put("idNode", this.node.getId());
			operations += block("operation.closerevisions", map);

			map.put("idNode", this.node.getId());
			map.put("idRevision", this.getParameterAsString("idrevision"));
			operations += block("operation.restorerevision", map);

			addMark("operations", operations);
			return;
		}

		if (!this.node.isPrototype()) {
			org.monet.space.office.core.model.Language language = Language.getInstance();
			String nodeId = this.node.getId();
			for (OperationProperty operationDefinition : this.node.getDefinition().getOperationList()) {

				if (!canExecuteOperation(node, operationDefinition))
					continue;

				map.put("id", nodeId);
				map.put("code", operationDefinition.getCode());
				map.put("name", operationDefinition.getName());
				map.put("visible", node.getState().existsFlagForOperation(operationDefinition.getCode()) ? "false" : "true");
				map.put("label", language.getModelResource(operationDefinition.getLabel()));
				operations += block("operation", map);
				map.clear();
			}
		}

		if (!node.isDesktop() && !node.isCatalog() && !node.isLocked() && canEditWithRole()) {
			map.put("idNode", this.node.getId());
			map.put("from", this.getParameterAsString("from"));
			operations += block("operation.editable", map);
			map.clear();
		}

		if (this.node.hasRevisions() && !this.node.isDesktop() && !node.isLocked() && canEditWithRole()) {
			map.put("idNode", this.node.getId());
			map.put("from", this.getParameterAsString("from"));
			operations += block("operation.revisions", map);
			map.clear();
		}

		if (this.definition.isPrototypable() && !node.isLocked() && canEditWithRole()) {
			map.put("idNode", this.node.getId());
			map.put("idParent", this.node.getParentId());
			map.put("from", this.getParameterAsString("from"));
			operations += block("operation.copy", map);
			map.clear();
		}

		addMark("operations", operations);
	}

	protected String initTab(NodeViewProperty viewDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String idRevision = this.getParameterAsString("idrevision");

		if (!hasViewPermissions(viewDefinition))
			return "";

		if (!idRevision.isEmpty() && this.isSystemView(viewDefinition))
			return "";

		if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
			ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
			if (showDefinition.getLocation() != null && !this.node.isGeoReferenced()) return "";
			if ((showDefinition.getNotes() != null || showDefinition.getTasks() != null) && this.node.isPrototype()) return "";
			if (showDefinition.getNotes() != null) return "";
			if (showDefinition.getTasks() != null && (!this.node.hasLinksFromTasks())) return "";
		}

		if (viewDefinition instanceof FormViewProperty) {
			FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
			if (showDefinition.getLocation() != null && !this.node.isGeoReferenced()) return "";
			if ((showDefinition.getNotes() != null || showDefinition.getTasks() != null) && this.node.isPrototype()) return "";
			if (showDefinition.getTasks() != null && (!this.node.hasLinksFromTasks())) return "";
		}

		Language language = Language.getInstance();
		String template = (node.isLocked() || !canEditWithRole() || this.node.getDefinition().isReadonly()) ? block("tab$template.readonly", map) : block("tab$template", map);
		String label = language.getModelResource(viewDefinition.getLabel());

		if (label == null || label.isEmpty())
			label = language.getModelResource(this.node.getLabel());

		if (label == null || label.isEmpty())
			label = block("tab$emptyLabel", new HashMap<String, Object>());

		map.put("code", viewDefinition.getCode());
		map.put("label", label);
		map.put("visible", node.getState().existsFlagForView(viewDefinition.getCode()) ? "false" : "true");
		map.put("idNode", this.node.getId());
		map.put("from", this.getParameterAsString("from"));
		map.put("codeView", viewDefinition.getCode());
		map.put("template", template);
		if (this.getParameter("idrevision") != null) map.put("idRevision", this.getParameterAsString("idrevision"));

		return block("tab.loading", map);
	}

	private boolean hasViewPermissions(NodeViewProperty viewDefinition) {
		NodeViewProperty.ForProperty forDefinition = viewDefinition.getFor();

		if (forDefinition == null)
			return true;

		String roleCode = dictionary.getDefinitionCode(forDefinition.getRole().getValue());
		return account.getRoleList().contains(roleCode);
	}

	protected String initMapTab() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("code", "location");
		map.put("label", Language.getInstance().getLabel(LabelCode.LOCATION));
		map.put("visible", "true");
		map.put("idNode", this.node.getId());
		map.put("from", this.getParameterAsString("from"));
		map.put("codeView", "location");

		return block("tab.loading", map);
	}

	protected void initMessages() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("idNode", this.node.getId());
		addMark("messages", block("messages", map));
	}

	protected void initTabs() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		NodeDefinition definition = this.node.getDefinition();
		List<NodeViewProperty> tabViewDefinitionList = definition.getTabViewDefinitionList();
		String tabsList = "";
		String idRevision = this.getParameterAsString("idrevision");
		int tabsCount = tabViewDefinitionList.size();

		if (this.node.isDesktop() && tabsCount == 1) {
			NodeViewProperty viewDefinition = tabViewDefinitionList.get(0);

			OfficeRender render = this.rendersFactory.get(this.node, this.template, this.renderLink, account);
			render.setParameters(this.getParameters());
			render.setParameter("view", viewDefinition.getCode());
			map.put("render(view.node)", render.getOutput());

			addMark("tabs", block("tabs.empty", map));
			return;
		}

		for (NodeViewProperty viewDefinition : tabViewDefinitionList)
			tabsList += this.initTab(viewDefinition);

		if (definition.isGeoreferenced() && idRevision.isEmpty() && !containsViewWithShowLocation(tabViewDefinitionList)) {
			tabsList += this.initMapTab();
			tabsCount++;
		}

		map.put("hide", (tabsCount == 1) ? "hide" : "");

		String defaultTabCode = (definition.getDefaultView() != null && hasViewPermissions(definition.getDefaultView())) ? definition.getDefaultView().getCode() : null;
		if (defaultTabCode == null)
			defaultTabCode = (definition.getViewDefinitionList().size() > 0) ? definition.getViewDefinitionList().get(0).getCode() : "";

		map.put("defaultTab", defaultTabCode);
		map.put("tabsList", tabsList);

		addMark("tabs", block("tabs", map));
	}

	protected boolean containsViewWithShowLocation(List<NodeViewProperty> tabViewDefinitionList) {
		for (NodeViewProperty viewDefinition : tabViewDefinitionList)
			if (isLocationSystemView(viewDefinition)) return true;
		return false;
	}

}
