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


import org.monet.metamodel.SourceDefinition;
import org.monet.space.explorer.control.dialogs.LoadSourceTermsDialog;
import org.monet.space.explorer.control.displays.TermDisplay;
import org.monet.space.explorer.model.ExplorerList;
import org.monet.space.kernel.model.DataRequest;
import org.monet.space.kernel.model.Source;
import org.monet.space.kernel.model.Term;
import org.monet.space.kernel.model.TermList;

import java.io.IOException;

public class LoadSourceTermsAction extends Action<LoadSourceTermsDialog, TermDisplay> {

    public void execute() throws IOException {
        display.writeList(terms());
    }

    private ExplorerList<Term> terms() {
        return toExplorerList(terms(dataRequest()));
    }

    private DataRequest dataRequest() {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setStartPos(dialog.getStart());
        dataRequest.setLimit(dialog.getLimit());
        dataRequest.setCondition(dialog.getCondition());
        dataRequest.addParameter(DataRequest.MODE, dialog.getMode());
        dataRequest.addParameter(DataRequest.FLATTEN, dialog.getFlatten());
        dataRequest.addParameter(DataRequest.DEPTH, dialog.getDepth());
        return dataRequest;
    }

    private TermList terms(DataRequest dataRequest) {
        Source<SourceDefinition> source = dialog.getSource();
        if (dialog.hasCondition())
            return source.searchTerms(dataRequest);
        return source.loadTerms(dataRequest, isOnlyEnabled());
    }

    private boolean isOnlyEnabled() {
        return !(dialog.getMode() != null && dialog.getMode().equals(DataRequest.Mode.TREE));
    }

    private ExplorerList<Term> toExplorerList(TermList termList) {
        ExplorerList<Term> result = new ExplorerList<>();
        result.setTotalCount(termList.getTotalCount());

        for (Term term : termList)
            result.add(term);

        return result;
    }
}