package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoadImporterTypesAction extends BaseAction {
        
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        //TODO return importer types
        sendResponse(response, "{data : [{id:1, label:'importer1'}, {id:2, label:'importer2'}]}");
    }
}

