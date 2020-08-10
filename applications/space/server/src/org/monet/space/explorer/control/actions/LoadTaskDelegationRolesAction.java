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
import org.monet.space.explorer.control.dialogs.TaskDialog;
import org.monet.space.explorer.control.displays.RoleDisplay;
import org.monet.space.explorer.control.exceptions.ReadEntityPermissionException;
import org.monet.space.explorer.model.ExplorerList;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Dictionary;

import java.io.IOException;

public class LoadTaskDelegationRolesAction extends Action<TaskDialog, RoleDisplay> {
    private LayerProvider layerProvider;
    private Dictionary dictionary;

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    @Inject
    public void inject(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void execute() throws IOException {

        Task task = dialog.getTask();

        if (!componentProvider.getComponentSecurity().canRead(task, getAccount()))
            throw new ReadEntityPermissionException();

        PlaceProperty currentPlace = task.getProcess().getCurrentPlace();
        ProcessDefinition definition = (ProcessDefinition) task.getDefinition();
        DelegationActionProperty delegationDefinition = currentPlace.getDelegationActionProperty();

        if (delegationDefinition == null) {
            display.writeError(org.monet.space.explorer.model.Error.TASK_DELEGATION_PLACE_INCORRECT);
            return;
        }

        String providerKey = delegationDefinition.getProvider().getValue();
        TaskProviderProperty providerDefinition = definition.getTaskProviderPropertyMap().get(providerKey);
        String roleKey = providerDefinition.getRole().getValue();
        RoleDefinition roleDefinition = dictionary.getRoleDefinition(roleKey);
        Role.Nature nature = getNature(providerDefinition);

        if (nature == null) {
            display.writeError(org.monet.space.explorer.model.Error.TASK_DELEGATION_NO_NATURE);
            return;
        }

        display.writeList(toExplorerList(loadNonExpiredRoleList(roleDefinition.getCode(), nature)));
    }

    private RoleList loadNonExpiredRoleList(String roleCode, Role.Nature nature) {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setCode(roleCode);
        dataRequest.setStartPos(0);
        dataRequest.setLimit(-1);
        dataRequest.addParameter(DataRequest.NATURE, nature.toString());
        dataRequest.addParameter(DataRequest.NON_EXPIRED, "true");

        return layerProvider.getRoleLayer().loadRoleList(dataRequest);
    }

    private Role.Nature getNature(TaskProviderProperty providerDefinition) {
        if (providerDefinition.getExternal() != null && providerDefinition.getInternal() != null)
            return Role.Nature.Both;

        if (providerDefinition.getExternal() != null)
            return Role.Nature.External;

        if (providerDefinition.getInternal() != null)
            return Role.Nature.Internal;

        return null;
    }

    private ExplorerList<Role> toExplorerList(RoleList roleList) {
        ExplorerList<Role> result = new ExplorerList<>();

        result.setTotalCount(roleList.getTotalCount());
        for (Role role : roleList)
            result.add(role);

        return result;
    }

}