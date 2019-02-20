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
import org.monet.space.explorer.control.dialogs.CommandDialog;
import org.monet.space.explorer.control.displays.CommandDisplay;
import org.monet.space.explorer.control.exceptions.WriteEntityPermissionException;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.agents.AgentUserClient;
import org.monet.space.kernel.model.ClientOperation;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.Node;

import java.io.IOException;

public class ExecuteCommandAction extends Action<CommandDialog, CommandDisplay> {
    private AgentUserClient agentUserClient;

    @Inject
    public void inject(AgentUserClient agentUserClient) {
        this.agentUserClient = agentUserClient;
    }

    public void execute() throws IOException {
        Node node = dialog.getNode();
        Long threadId = Thread.currentThread().getId();

        if (!componentProvider.getComponentSecurity().canWrite(node, getAccount()))
            throw new WriteEntityPermissionException();

        MonetEvent event = new MonetEvent(MonetEvent.NODE_EXECUTE_COMMAND, null, node);
        event.addParameter(MonetEvent.PARAMETER_COMMAND, dialog.getCommand());
        AgentNotifier.getInstance().notify(event);

        ClientOperation operation = agentUserClient.getOperationForUser(threadId);
        String message = agentUserClient.getMessageForUser(threadId);

        agentUserClient.clear(threadId);

        if (operation != null)
            display.write(operation);
        else {
            if (message != null)
                display.writeError(message);
            else
                display.write(new ClientOperation("void", null));
        }
    }

}