package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.agents.AgentImportMonitor;
import org.monet.grided.core.model.Progress;

public class LoadImportProgressAction extends BaseAction {
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        AgentImportMonitor monitor = AgentImportMonitor.getInstance();
        Progress progress = monitor.getProgress();
        sendResponse(response, "{data : {value : 20, estimatedTime: 193938200}}");
    }
}