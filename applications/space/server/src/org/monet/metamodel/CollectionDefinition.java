package org.monet.metamodel;

import org.monet.metamodel.interfaces.IsInitiable;

// CollectionDefinition
// Declaración que se utiliza para modelar una colección

public class CollectionDefinition extends CollectionDefinitionBase implements IsInitiable {

	public void init() {
		for (SetViewProperty view : this._setViewPropertyMap.values()) {
			if (getDefaultView() == null)
				setDefaultView(view);

			if (view.isDefault() && !view.isVisibleWhenEmbedded())
				setDefaultView(view);

			this.viewsMap.put(view.getCode(), view);
			this.viewsMap.put(view.getName(), view);

			if (!view.isVisibleWhenEmbedded())
				this.tabViewList.add(view);

			this.viewList.add(view);
		}
	}

}
