package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.serializers.json.impl.JSONErrorResponse;
import org.monet.grided.core.serializers.json.impl.JSONSuccessResponse;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class RemoveFederationsAction extends BaseAction {

    private GridedService service;

    @Inject
    public RemoveFederationsAction(GridedService service) {
        this.service = service;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {        
        String jsonIds = request.getParameter(Params.IDS);
        
        try {
            JSONArray jsonArray = JSONArray.fromObject(jsonIds);
        
            long[] ids = new long[jsonArray.size()];
            for (int i=0; i < jsonArray.size(); i++) {                
                ids[i] = Long.valueOf(jsonArray.getString(i)); 
            }
        
            this.service.deleteFederations(ids);
            
        } catch (Exception ex) {
            sendResponse(response, JSONErrorResponse.ERROR);
            return;
        }
        
        sendResponse(response, JSONSuccessResponse.OK);
    }

}

