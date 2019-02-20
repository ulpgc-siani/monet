package org.monet.metamodel;

import org.monet.metamodel.interfaces.IsInitiable;
import org.monet.metamodel.internal.Ref;

import java.util.ArrayList;

// ContainerDefinition
// Declaración que se utiliza para modelar un contenedor

public class ContainerDefinition extends ContainerDefinitionBase implements IsInitiable {

	public void init() {
		ArrayList<ContainerDefinition.ViewProperty> viewsToRemove = new ArrayList<ContainerDefinition.ViewProperty>();
		ArrayList<ContainerDefinition.ViewProperty> viewsToAdd = new ArrayList<ContainerDefinition.ViewProperty>();

		for (org.monet.metamodel.ContainerDefinition.ViewProperty viewDefinition : this._viewPropertyMap.values()) {
			if (viewDefinition.getShow().getComponent() != null) {
				ArrayList<Ref> componentDefinitions = viewDefinition.getShow().getComponent();

				if (componentDefinitions != null && componentDefinitions.size() > 1) {
					viewsToRemove.add(viewDefinition);
					for (Ref componentDefinition : componentDefinitions) {
						org.monet.metamodel.ContainerDefinition.ViewProperty newDefinition = new org.monet.metamodel.ContainerDefinition.ViewProperty();
						String code = viewDefinition.getCode() + componentDefinition.getDefinition();
						String name = viewDefinition.getName() + componentDefinition.getValue();
						newDefinition.merge(viewDefinition);
						newDefinition.setCode(code);
						newDefinition.setName(name);
						viewsToAdd.add(newDefinition);
					}
				}
			}

			if (viewDefinition.isDefault() && !viewDefinition.isVisibleWhenEmbedded())
				this.defaultView = viewDefinition;

			this.viewsMap.put(viewDefinition.getCode(), viewDefinition);
			this.viewsMap.put(viewDefinition.getName(), viewDefinition);

			if (!viewDefinition.isVisibleWhenEmbedded())
				this.tabViewList.add(viewDefinition);

			this.viewList.add(viewDefinition);
		}

		this.deleteTabViews(viewsToRemove);
		this.addTabViews(viewsToAdd);
	}

	public void setDefaultView(org.monet.metamodel.ContainerDefinition.ViewProperty view) {
		this.defaultView = view;
	}

	public void deleteTabViews(ArrayList<ContainerDefinition.ViewProperty> viewsToRemove) {
		for (ContainerDefinition.ViewProperty view : viewsToRemove) {
			this._viewPropertyMap.remove(view.getCode());
			this._viewPropertyMap.remove(view.getName());
			this.viewsMap.remove(view.getCode());
			this.viewsMap.remove(view.getName());
			this.tabViewList.remove(view);
			this.viewList.remove(view);
		}
	}

	public void addTabViews(ArrayList<ContainerDefinition.ViewProperty> viewsToAdd) {
		for (ContainerDefinition.ViewProperty view : viewsToAdd) {
			this._viewPropertyMap.put(view.getCode(), view);
			this._viewPropertyMap.put(view.getName(), view);
			this.viewsMap.put(view.getCode(), view);
			this.viewsMap.put(view.getName(), view);
			this.tabViewList.add(view);
			this.viewList.add(view);
		}
	}

}
