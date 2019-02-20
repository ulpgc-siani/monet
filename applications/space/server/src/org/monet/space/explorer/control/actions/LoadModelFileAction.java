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
import org.monet.space.explorer.control.dialogs.FileDialog;
import org.monet.space.explorer.control.displays.Display;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.BusinessModel;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.core.constants.ErrorCode;

import java.io.IOException;

public class LoadModelFileAction extends Action<FileDialog, Display> {
    private BusinessUnit businessUnit;
    private MimeTypes mimeTypes;

    @Inject
    public void inject(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    @Inject
    public void inject(MimeTypes mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    public void execute() throws IOException {
        String filename = dialog.getFilename();
        BusinessModel businessModel = businessUnit.getBusinessModel();
        String absoluteFilename = businessModel.getAbsoluteFilename(filename);
        String formatCode = mimeTypes.getFromFilename(filename);

        if (formatCode == null) {
            throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_BUSINESS_MODEL_FILE);
        }

        filename = filename.replace(Strings.BAR45, Strings.UNDERLINED);

        display.setContentType(formatCode);
        display.addHeader("Content-Disposition", "inline; filename=" + filename);
        display.getOutputStream().write(AgentFilesystem.getBytesFromFile(absoluteFilename));
        display.getOutputStream().flush();
    }

}