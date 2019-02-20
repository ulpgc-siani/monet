package org.monet.metamodel;

import org.monet.metamodel.interfaces.IsInitiable;

public class DesktopDefinition extends DesktopDefinitionBase implements IsInitiable {

	public void init() {
		for (ViewProperty view : this._viewPropertyMap.values()) {
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

	protected void initRulesMap() {
		super.initRulesMap();
		for (RuleLinkProperty rule : this._ruleLinkPropertyList)
			this.rulesMap.put(rule.getCode(), rule);
	}
}
