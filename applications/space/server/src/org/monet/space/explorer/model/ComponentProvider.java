package org.monet.space.explorer.model;

import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentSecurity;

public interface ComponentProvider {
	ComponentSecurity getComponentSecurity();
	ComponentFederation getComponentFederation();
	ComponentDocuments getComponentDocuments();
}
