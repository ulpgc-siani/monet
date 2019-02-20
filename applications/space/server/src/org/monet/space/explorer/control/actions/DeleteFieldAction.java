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
import net.minidev.json.JSONObject;
import org.monet.space.explorer.control.dialogs.DeleteFieldDialog;
import org.monet.space.explorer.control.displays.DefaultDisplay;
import org.monet.space.explorer.control.exceptions.WriteEntityPermissionException;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.explorer.model.PathResolver;
import org.monet.space.kernel.agents.AgentPushService;
import org.monet.space.kernel.listeners.ListenerPushService;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.PushClient;

import java.io.IOException;

public class DeleteFieldAction extends Action<DeleteFieldDialog, DefaultDisplay> {

    private LayerProvider layerProvider;

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    public void execute() throws IOException {
        Node node = dialog.getNode();

        if (!componentProvider.getComponentSecurity().canWrite(node, getAccount()))
            throw new WriteEntityPermissionException();

        PathResolver resolver = new PathResolver();
        String parentPath = dialog.getParentPath();

        Attribute parentAttribute = resolver.resolveAttribute(node, parentPath);
        if (parentAttribute == null)
            display.writeError(String.format(org.monet.space.explorer.model.Error.NODE_PARENT_FIELD_NOT_FOUND, parentPath));

        parentAttribute.getAttributeList().delete(dialog.getPos());

        layerProvider.getNodeLayer().saveNode(node);

        new Thread(new Runnable() {
            @Override
            public void run() {
                notifyViewers(AgentPushService.getInstance().getClientView(getAccount().getId(), idSession, dialog.getInstanceId()));
            }
        }).run();

        display.write("OK");
    }


    private void notifyViewers(PushClient client) {
        AgentPushService.getInstance().pushToViewers(client, PushClient.generateViewId(dialog.getNode()), ListenerPushService.PushClientMessages.DELETE_FIELD, jsonInfo());
    }

    private JSONObject jsonInfo() {
        JSONObject observer = new JSONObject();
        observer.put("entity", dialog.getEntityId());
        observer.put("field", dialog.getParentPath());
        observer.put("position", String.valueOf(dialog.getPos()));
        return observer;
    }

}