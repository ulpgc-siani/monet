package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.*;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.BusinessModel;

import java.util.HashMap;

public class BusinessModelDefinitionRender extends OfficeRender {
	protected BusinessModel businessModel;

	public BusinessModelDefinitionRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.businessModel = (BusinessModel) target;
	}

	@Override
	protected void init() {
		loadCanvas("businessmodel");

		addMark("code", this.businessModel.getProject().getName());

		this.initDefinitions();
	}

	protected String getType(Definition definition) {
		if (definition instanceof DesktopDefinition) return "desktop";
		if (definition instanceof ContainerDefinition) return "container";
		if (definition instanceof CollectionDefinition) return "collection";
		if (definition instanceof CatalogDefinition) return "catalog";
		if (definition instanceof FormDefinition) return "form";
		if (definition instanceof DocumentDefinition) return "document";
		if (definition instanceof TaskDefinition) return "task";
		return "undefined";
	}

	protected void initDefinitions() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String definitions = "", definitionTypes = "";

		for (Definition definition : this.businessModel.getDictionary().getAllDefinitions()) {
			map.put("code", definition.getCode());
			map.put("type", this.getType(definition));
			map.put("label", LibraryString.cleanSpecialChars(definition.getLabelString()));
			map.put("description", LibraryString.cleanSpecialChars(definition.getDescriptionString()));

			if (definition instanceof NodeDefinition) {
				NodeDefinition nodeDefinition = (NodeDefinition) definition;
				map.put("isComponent", nodeDefinition.isComponent() ? "true" : "false");
			} else map.put("isComponent", "false");

			definitions += block("definition", map);
			definitionTypes += block("definitionType", map);

			map.clear();
		}

		addMark("definitionList", definitions);
		addMark("definitionTypeList", definitionTypes);
	}

}
