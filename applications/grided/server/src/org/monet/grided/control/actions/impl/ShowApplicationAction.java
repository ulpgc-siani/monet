package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.ui.views.ViewParams;
import org.monet.grided.ui.views.impl.ApplicationView;

import com.google.inject.Inject;

public class ShowApplicationAction extends BaseAction {
    
    private ApplicationView applicationView;
    private Configuration configuration;    
    
    @Inject
    public ShowApplicationAction(Configuration configuration) {
        this.configuration = configuration;        
    }
    
    @Inject
    public void injectApplicationView(ApplicationView applicationView) {
        this.applicationView = applicationView;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {        
        //this.applicationView.addContextParameter(ViewParams.URL, request.getRequestURL().toString());
        this.applicationView.addContextParameter(ViewParams.PORT, request.getServerPort()); 
        this.applicationView.addContextParameter(ViewParams.LANGUAGE, this.configuration.getProperty(Configuration.LANGUAGE));
        this.applicationView.addContextParameter(ViewParams.API_URL, request.getRequestURL().toString());
        // admin federation login
        // imagespath
        
        String content = this.applicationView.render();        
        sendResponse(response, content);        
    }
}