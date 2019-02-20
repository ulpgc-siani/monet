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
import org.monet.space.explorer.control.displays.ReportDisplay;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.explorer.model.Report;

import java.io.IOException;
import java.util.Map;

public class LoadIndexFilterOptionsReportAction extends LoadIndexFilterAction<ReportDisplay> {
    private LayerProvider layerProvider;

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    public void execute() throws IOException {
        String indexDefinitionCode = dialog.getIndexDefinition().getCode();
        String attributeCode = dialog.getFilter().getName();
        Map<String, Integer> values = layerProvider.getNodeLayer().loadReferenceAttributeValuesCount(getParentId(), indexDefinitionCode, attributeCode, null, getFilterAttributesDefinition());

        display.write(new Report(values));
    }

}