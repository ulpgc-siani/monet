package org.monet.space.explorer.model;

import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentSecurity;

public class MonetComponentProvider implements ComponentProvider {

	@Override
	public ComponentSecurity getComponentSecurity() {
		return ComponentSecurity.getInstance();
	}

	@Override
	public ComponentFederation getComponentFederation() {
		return ComponentFederation.getInstance();
	}

	@Override
	public ComponentDocuments getComponentDocuments() {
		return ComponentDocuments.getInstance();
	}
}
