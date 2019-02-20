package client.core.system.definition.views;

import client.core.system.definition.entity.EntityDefinition;

public class ViewDefinition extends EntityDefinition implements client.core.model.definition.views.ViewDefinition {
	private boolean isDefault;
	private Design design;

	@Override
	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public Design getDesign() {
		return design;
	}

	public void setDesign(Design design) {
		this.design = design;
	}
}
