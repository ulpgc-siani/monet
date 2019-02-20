package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.control.log.Logger;
import org.monet.grided.core.constants.Params;
import org.monet.grided.core.serializers.json.impl.JSONSuccessResponse;

import com.google.inject.Inject;

public class SaveLogAction extends BaseAction {

    private Logger logger;

    @Inject
    public SaveLogAction(Logger logger) {
        this.logger = logger;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String message = request.getParameter(Params.MESSAGE);
        this.logger.error(message);           
        sendResponse(response, JSONSuccessResponse.OK);
    }    
}

