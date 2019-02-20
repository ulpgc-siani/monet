package org.monet.grided.control.actions.impl;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.PublicationService;
import org.monet.grided.core.model.PublicationServiceList;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.serializers.json.impl.JSONPublicationServiceListSerializer;
import org.monet.grided.core.serializers.xml.space.SpaceData.PublicationFrontComponentStatement;
import org.monet.grided.core.services.adapters.MonetGridedAdapter;
import org.monet.grided.core.services.grided.GridedService;
import org.monet.grided.core.services.space.SpaceService;

import com.google.inject.Inject;

public class LoadServicesAction extends BaseAction {

    private GridedService gridedService;
    private SpaceService monetService;
    private MonetGridedAdapter adapter;
    private JSONPublicationServiceListSerializer serializer;

    @Inject
    public LoadServicesAction(GridedService gridedService, SpaceService monetService, MonetGridedAdapter adapter, JSONPublicationServiceListSerializer serializer) {
        this.gridedService = gridedService;
        this.monetService = monetService;        
        this.adapter = adapter;
        this.serializer = serializer;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {        
        String spaceId = request.getParameter(Params.SPACE_ID);
        
        Space space = this.gridedService.loadSpace(Long.valueOf(spaceId));
        Server server = space.getFederation().getServer();
                     
        PublicationServiceList services = this.adapter.adapt(this.monetService.loadServices(server.getIp()));
        
        Iterator<? extends PublicationFrontComponentStatement> iter = space.getData().getPublicationFrontComponentStatements().iterator();
        
        while (iter.hasNext()) {
           PublicationFrontComponentStatement statement = iter.next();           
           PublicationService service = services.getItemWith(statement.getName(), statement.getType());
                     
           if (service != null) {               
               service.publish();
           }
        }         
        
        sendResponse(response, this.serializer.serialize(services));        
    }    
}

