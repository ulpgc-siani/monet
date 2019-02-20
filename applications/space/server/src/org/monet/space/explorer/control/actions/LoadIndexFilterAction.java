/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2014  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.explorer.control.actions;


import com.google.inject.Inject;
import org.monet.metamodel.*;
import org.monet.metamodel.internal.Ref;
import org.monet.space.explorer.control.dialogs.LoadIndexFilterDialog;
import org.monet.space.explorer.control.displays.Display;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.model.Dictionary;

import java.util.ArrayList;
import java.util.List;

import static org.monet.space.explorer.control.dialogs.LoadIndexDialog.*;

public abstract class LoadIndexFilterAction<Dsp extends Display> extends Action<LoadIndexFilterDialog, Dsp> {
    protected LayerProvider layerProvider;
    private Dictionary dictionary;

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    @Inject
    public void inject(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    protected String getParentId() {
        Scope scope = dialog.getScope();

        if (scope instanceof NodeScope)
            return ((NodeScope)scope).getSet().getId();

        if (scope instanceof FieldScope)
            return layerProvider.getNodeLayer().locateNodeId(((FieldScope)scope).getSingleton());

        return null;
    }

    protected List<SetDefinitionBase.SetViewPropertyBase.FilterProperty> getFilterAttributesDefinition() {
        if (dialog.getScope() instanceof FieldScope)
            return new ArrayList<>();

        NodeScope scope = dialog.getScope();
        return scope.getSetView().getFilterList();
    }

    protected List<String> getFilterNodes() {

        if (dialog.getScope() instanceof FieldScope)
            return new ArrayList<>();

        List<String> result = new ArrayList<>();
        NodeScope scope = dialog.getScope();

        if (scope.getSet().isCatalog())
            result = getCatalogFilterNodes((CatalogDefinition) scope.getSet().getDefinition(), scope.getSetView());
        else if (scope.getSet().isCollection())
            result = getCollectionFilterNodes((CollectionDefinition) scope.getSet().getDefinition(), scope.getSetView());

        return result;
    }

    protected List<String> getCollectionFilterNodes(CollectionDefinition collectionDefinition, SetDefinition.SetViewProperty viewDefinition) {
        SetDefinitionBase.SetViewPropertyBase.SelectProperty selectDefinition = viewDefinition.getSelect();

        if (selectDefinition != null && selectDefinition.getNode().size() > 0)
            return getFilterNodes(selectDefinition.getNode());
        else
            return getFilterNodes(collectionDefinition.getAdd().getNode());
    }

    protected List<String> getCatalogFilterNodes(CatalogDefinition catalogDefinition, SetDefinition.SetViewProperty viewDefinition) {
        SetDefinitionBase.SetViewPropertyBase.SelectProperty selectDefinition = viewDefinition.getSelect();

        if (selectDefinition != null && selectDefinition.getNode().size() > 0)
            return getFilterNodes(selectDefinition.getNode());

        return new ArrayList<>();
    }

    protected List<String> getFilterNodes(ArrayList<Ref> refList) {
        List<String> result = new ArrayList<>();

        for (Ref select : refList) {
            Definition definition = dictionary.getDefinition(select.getValue());
            if (definition.isDisabled())
                continue;
            result.add(definition.getCode());
        }

        return result;
    }

}