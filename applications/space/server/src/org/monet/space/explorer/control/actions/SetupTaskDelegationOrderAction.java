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
import org.monet.bpi.java.FieldBooleanImpl;
import org.monet.bpi.java.FieldDateImpl;
import org.monet.bpi.java.FieldTextImpl;
import org.monet.bpi.types.Date;
import org.monet.metamodel.DelegationActionProperty;
import org.monet.metamodel.internal.TaskOrderDefinition;
import org.monet.space.explorer.control.dialogs.TaskDialog;
import org.monet.space.explorer.control.displays.TaskDisplay;
import org.monet.space.explorer.control.exceptions.ReadEntityPermissionException;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.agents.AgentUserClient;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.machines.ttm.behavior.ProcessBehavior;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.TaskOrder;

import java.io.IOException;

public class SetupTaskDelegationOrderAction extends Action<TaskDialog, TaskDisplay> {
    private LayerProvider layerProvider;
    private AgentUserClient agentUserClient;

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    @Inject
    public void inject(AgentUserClient agentUserClient) {
        this.agentUserClient = agentUserClient;
    }

    public void execute() throws IOException {
        TaskLayer taskLayer = layerProvider.getTaskLayer();
        NodeLayer nodeLayer = layerProvider.getNodeLayer();
        Task task = dialog.getTask();
        Long threadId = Thread.currentThread().getId();

        if (!componentProvider.getComponentSecurity().canRead(task, getAccount()))
            throw new ReadEntityPermissionException();

        ProcessBehavior process = task.getProcess();
        DelegationActionProperty delegationActionProperty = process.getCurrentPlace().getDelegationActionProperty();

        if (delegationActionProperty == null) {
            display.writeError(org.monet.space.explorer.model.Error.TASK_DELEGATION_PLACE_INCORRECT);
            return;
        }

        String orderId = process.getCurrentProvider().getModel().getOrderId();
        TaskOrder order = taskLayer.loadTaskOrder(orderId);
        Node node = nodeLayer.loadNode(order.getSetupNodeId());
        Date startDate = FieldDateImpl.get(node.getAttribute(TaskOrderDefinition.SuggestedStartDateProperty.CODE));
        Date endDate = FieldDateImpl.get(node.getAttribute(TaskOrderDefinition.SuggestedEndDateProperty.CODE));

        order.setSuggestedStartDate(startDate != null ? startDate.getValue() : null);
        order.setSuggestedEndDate(endDate != null ? endDate.getValue() : null);
        order.setComments(FieldTextImpl.get(node.getAttribute(TaskOrderDefinition.CommentsProperty.CODE)));
        order.setUrgent(FieldBooleanImpl.get(node.getAttribute(TaskOrderDefinition.UrgentProperty.CODE)));

        taskLayer.saveTaskOrder(order);
        process.setupDelegationAction();

        agentUserClient.clear(threadId);

        display.write(task);
    }

}