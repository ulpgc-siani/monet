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
import org.monet.metamodel.EditionActionProperty;
import org.monet.space.explorer.control.dialogs.TaskDialog;
import org.monet.space.explorer.control.displays.TaskDisplay;
import org.monet.space.explorer.control.exceptions.ReadEntityPermissionException;
import org.monet.space.explorer.model.Language;
import org.monet.space.kernel.machines.ttm.behavior.ProcessBehavior;
import org.monet.space.kernel.machines.ttm.model.ValidationResult;
import org.monet.space.kernel.model.Task;

import java.io.IOException;
import java.util.Map;

public class SolveTaskEditionAction extends Action<TaskDialog, TaskDisplay> {
    private Language language;

    @Inject
    public void inject(Language language) {
        this.language = language;
    }

    public void execute() throws IOException {
        Task task = dialog.getTask();

        if (!componentProvider.getComponentSecurity().canRead(task, getAccount()))
            throw new ReadEntityPermissionException();

        ProcessBehavior process = task.getProcess();
        EditionActionProperty editionActionProperty = process.getCurrentPlace().getEditionActionProperty();

        if (editionActionProperty == null) {
            display.writeError(org.monet.space.explorer.model.Error.TASK_EDITION_PLACE_INCORRECT);
            return;
        }

        ValidationResult validation = process.solveEditionAction();
        if (validation.isValid()) {
            display.write(task);
            return;
        }

        StringBuilder errorMessage = new StringBuilder();
        Map<String, String> errorsMap = validation.getErrors();

        for (String fieldCode : errorsMap.keySet())
            errorMessage.append(fieldCode).append(": ").append(errorsMap.get(fieldCode)).append("<br/>");

        String message = errorMessage.toString();
        if (message.isEmpty())
            message = language.getErrorMessage(org.monet.space.explorer.model.Error.TASK_EDITION);

        display.writeError(message);
    }

}