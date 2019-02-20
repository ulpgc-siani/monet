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
import org.monet.metamodel.WaitActionProperty;
import org.monet.metamodel.internal.Time;
import org.monet.space.explorer.control.dialogs.SetupTaskWaitDialog;
import org.monet.space.explorer.control.dialogs.SetupTaskWaitDialog.Scale;
import org.monet.space.explorer.control.displays.TaskDisplay;
import org.monet.space.explorer.control.exceptions.ReadEntityPermissionException;
import org.monet.space.kernel.agents.AgentUserClient;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.machines.ttm.behavior.ProcessBehavior;
import org.monet.space.kernel.model.Task;

import java.io.IOException;
import java.util.Date;

public class SetupTaskWaitAction extends Action<SetupTaskWaitDialog, TaskDisplay> {
    private AgentUserClient agentUserClient;

    @Inject
    public void inject(AgentUserClient agentUserClient) {
        this.agentUserClient = agentUserClient;
    }

    public void execute() throws IOException {
        Task task = dialog.getTask();
        long threadId = Thread.currentThread().getId();

        if (!componentProvider.getComponentSecurity().canRead(task, getAccount()))
            throw new ReadEntityPermissionException();

        ProcessBehavior process = task.getProcess();
        WaitActionProperty editionActionProperty = process.getCurrentPlace().getWaitActionProperty();

        if (editionActionProperty == null) {
            display.writeError(org.monet.space.explorer.model.Error.TASK_WAIT_PLACE_INCORRECT);
            return;
        }

        long timer = process.getTimerDue(process.getCurrentPlace().getCode());
        long finalTimestamp;
        long currentTimestamp = new Date().getTime();

        if (timer == -1)
            finalTimestamp = new Date().getTime();
        else
            finalTimestamp = timer;

        Scale scale = dialog.getScale();
        int value = dialog.getValue();
        if (scale == Scale.HOUR)
            finalTimestamp = (value > 0) ? finalTimestamp + LibraryDate.hoursToMillis(value) : finalTimestamp - LibraryDate.hoursToMillis(value * -1);
        else if (scale == Scale.DAY)
            finalTimestamp = (value > 0) ? finalTimestamp + LibraryDate.daysToMillis(value) : finalTimestamp - LibraryDate.daysToMillis(value * -1);
        else if (scale == Scale.MONTH)
            finalTimestamp = (value > 0) ? finalTimestamp + LibraryDate.monthsToMillis(value) : finalTimestamp - LibraryDate.monthsToMillis(value * -1);
        else if (scale == Scale.YEAR)
            finalTimestamp = (value > 0) ? finalTimestamp + LibraryDate.yearsToMillis(value) : finalTimestamp - LibraryDate.yearsToMillis(value*-1);

        long timeout = finalTimestamp - currentTimestamp;
        if (timeout < 0)
            timeout = 0;

        process.setupWaitAction(new Date(currentTimestamp), new Time(timeout));

        agentUserClient.clear(threadId);

        display.write(task);
    }

}