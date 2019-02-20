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
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.space.explorer.control.dialogs.DefinitionDialog;
import org.monet.space.explorer.control.displays.DefinitionDisplay;
import org.monet.space.kernel.model.Dictionary;

import java.io.IOException;

public class LoadDefinitionAction extends Action<DefinitionDialog, DefinitionDisplay> {
    private Dictionary dictionary;

    @Inject
    public void injectDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void execute() throws IOException {
        if (dialog.getKey().equals(DescriptorDefinition.CODE))
            display.write(new DescriptorDefinition());
        else
            display.write(dictionary.getDefinition(dialog.getKey()));
    }
}