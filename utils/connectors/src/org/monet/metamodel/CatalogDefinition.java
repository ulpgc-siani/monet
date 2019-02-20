package org.monet.metamodel;

import org.monet.metamodel.interfaces.IsInitiable;

public class CatalogDefinition extends CatalogDefinitionBase implements IsInitiable {

	public void init() {
		for (SetViewProperty view : this._setViewPropertyMap.values()) {
			this.viewsMap.put(view.getCode(), view);
			this.viewsMap.put(view.getName(), view);

			if (view.isDefault() && !view.isVisibleWhenEmbedded())
				this.defaultView = view;

			if (!view.isVisibleWhenEmbedded())
				this.tabViewList.add(view);

			this.viewList.add(view);
		}
	}

}
