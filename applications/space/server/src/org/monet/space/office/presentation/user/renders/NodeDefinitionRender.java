package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.NodeDefinition;
import org.monet.space.kernel.library.LibraryString;

import java.util.HashMap;

public class NodeDefinitionRender extends OfficeRender {
	protected NodeDefinition definition;

	public NodeDefinitionRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.definition = (NodeDefinition) target;
	}

	private String getNodeType() {

		if (this.definition.isForm()) return "form";
		else if (this.definition.isDesktop()) return "desktop";
		else if (this.definition.isContainer()) return "container";
		else if (this.definition.isCollection()) return "collection";
		else if (this.definition.isCatalog()) return "catalog";
		else if (this.definition.isDocument()) return "document";

		return "notype";
	}

	@Override
	protected void init() {
		loadCanvas("node");

		this.initProperties();
		this.initViews();
		this.initForm();
		this.initCollection();
		this.initBehaviour();
	}

	protected void initProperties() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("code", this.definition.getCode());
		map.put("type", this.getNodeType());
		map.put("label", LibraryString.cleanSpecialChars(this.definition.getLabelString()));
		map.put("description", LibraryString.cleanSpecialChars(this.definition.getDescriptionString()));
		map.put("environment", this.definition.isEnvironment() ? "environment" : "");

		addMark("properties", block("properties", map));
	}

	protected void initViews() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		addMark("views", block("views", map));
	}

	protected void initCollection() {
		addMark("collection", "");
	}

	protected void initForm() {
		addMark("form", "");
	}

	protected void initBehaviour() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		addMark("behaviour", block("behaviour", map));
	}

}
