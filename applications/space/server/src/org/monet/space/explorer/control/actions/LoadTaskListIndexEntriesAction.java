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
import org.monet.space.explorer.control.dialogs.LoadTaskListIndexEntriesDialog;
import org.monet.space.explorer.control.displays.TaskListIndexEntryDisplay;
import org.monet.space.explorer.model.*;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.DataRequest.SortBy;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.TaskList;
import org.monet.space.kernel.model.TaskSearchRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadTaskListIndexEntriesAction extends Action<LoadTaskListIndexEntriesDialog, TaskListIndexEntryDisplay> {
    private LayerProvider layerProvider;

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    public void execute() throws IOException {
        TaskSearchRequest request = new TaskSearchRequest();
        String type = null, state = null, role = null, urgent = null, owner = null;

        for (Filter filter : dialog.getFilters()) {
            if (filter.getName().equals("type")) type = filter.getOptions().get(0).getValue();
            else if (filter.getName().equals("state")) state = filter.getOptions().get(0).getValue();
            else if (filter.getName().equals("role")) role = filter.getOptions().get(0).getValue();
            else if (filter.getName().equals("urgent")) urgent = filter.getOptions().get(0).getValue();
            else if (filter.getName().equals("owner")) owner = filter.getOptions().get(0).getValue();
        }

        if (dialog.getCondition() != null)
            request.setCondition(dialog.getCondition());

        request.addParameter(Task.Parameter.TYPE, type);
        request.addParameter(Task.Parameter.STATE, state);
        request.addParameter(Task.Parameter.INBOX, dialog.getInbox());
        request.addParameter(Task.Parameter.SITUATION, dialog.getSituation());
        request.addParameter(Task.Parameter.ROLE, role);
        request.addParameter(Task.Parameter.URGENT, urgent);
        request.addParameter(Task.Parameter.OWNER, owner);
        request.setSortsBy(getSortsBy());
        request.setStartPos(dialog.getStart());
        request.setLimit(dialog.getLimit());

        TaskLayer taskLayer = layerProvider.getTaskLayer();
        Account account = getAccount();
        TaskList taskList = taskLayer.searchTasks(account, request);
        taskList.setTotalCount(taskLayer.searchTasksCount(account, request));

        display.writeList(toExplorerList(taskList));
    }

    private List<SortBy> getSortsBy() {
        List<Order> orders = dialog.getOrders();
        List<SortBy> result = new ArrayList<>();

        for (final Order order : orders)
            result.add(new SortBy() {
                @Override
                public String attribute() {
                    return order.getName();
                }

                @Override
                public String mode() {
                    return order.getMode();
                }
            });

        return result;
    }

    private ExplorerList toExplorerList(TaskList taskList) {
        ExplorerList result = new ExplorerList<>();
        result.setTotalCount(taskList.getTotalCount());

        if (result.getTotalCount() > 0)
            for (final Task task : taskList.get().values()) {
                result.add(new TaskListIndexEntry() {

                    @Override
                    public String getLabel() {
                        return task.getLabel();
                    }

                    @Override
                    public Task getEntity() {
                        return task;
                    }
                });
            }

        return result;
    }

}