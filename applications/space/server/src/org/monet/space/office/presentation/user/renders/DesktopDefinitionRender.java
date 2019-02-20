package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.DesktopDefinition;

import java.util.HashMap;

public class DesktopDefinitionRender extends NodeDefinitionRender {
	protected DesktopDefinition definition;

	public DesktopDefinitionRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		super.setTarget(target);
		this.definition = (DesktopDefinition) target;
	}

	protected void initCollection() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		super.initCollection();
		map.put("nodes", "");

		addMark("collection", block("collection", map));
	}

}
