package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.DocumentDefinition;
import org.monet.space.office.core.model.Language;
import org.monet.space.office.core.constants.LabelCode;

import java.util.HashMap;

public class DocumentPageRender extends NodePageRender {

	public DocumentPageRender() {
		super();
	}

	@Override
	protected void initControlInfo() {
		super.initControlInfo();
		addMark("children", "");
		addMark("addList", "");
	}

	@Override
	protected void initConfiguration() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String operations = "";

		super.initConfiguration();

		map.put("idNode", this.node.getId());
		operations += block("operation.download", map);

		addMark("operations", operations);
	}

	@Override
	protected void init() {
		loadCanvas("page.node.document");
		super.init();
	}

	@Override
	protected void initTabs() {
		HashMap<String, Object> map = new HashMap<>();
		DocumentDefinition definition = (DocumentDefinition) this.node.getDefinition();
		String tabsList = "";
		String idRevision = this.getParameterAsString("idrevision");

		map.put("code", "default");
		map.put("label", Language.getInstance().getLabel(LabelCode.DOCUMENT_PREVIEW));
		map.put("visible", "true");
		map.put("idNode", this.node.getId());
		map.put("from", this.getParameterAsString("from"));
		map.put("codeView", "default");
		map.put("template", template);

		OfficeRender nodeRender = this.rendersFactory.get(this.node, template, this.renderLink, account);
		nodeRender.setParameters(this.getParameters());
		nodeRender.setParameter("view", "default");
		map.put("render(view.node)", nodeRender.getOutput());

		tabsList += block("tab", map);

		if (definition.getSignatures() != null)
			tabsList += this.initSignsTab();

		if (definition.isGeoreferenced() && idRevision.isEmpty())
			tabsList += this.initMapTab();

		map.put("defaultTab", "default");
		map.put("tabsList", tabsList);

		addMark("tabs", block("tabs", map));
	}

	protected String initSignsTab() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		OfficeRender nodeRender = this.rendersFactory.get(this.node, "preview.html?mode=view", this.renderLink, account);
		nodeRender.setParameters(this.getParameters());
		nodeRender.setParameter("view", "signs");

		map.put("class", this.account.canSign(this.node) ? "blue highlight" : "orange");
		map.put("render(view.node)", nodeRender.getOutput());

		return block("tab.signs", map);
	}

}
