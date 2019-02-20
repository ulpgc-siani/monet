package org.monet.grided.core.serializers.json.impl;

import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.monet.grided.constants.PropertyNames;
import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.ModelVersion;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.model.State;
import org.monet.grided.core.serializers.json.JSONSerializer;
import org.monet.grided.core.serializers.xml.Logo;
import org.monet.grided.core.serializers.xml.Organization;
import org.monet.grided.core.serializers.xml.space.SpaceData;
import org.monet.grided.core.serializers.xml.space.SpaceData.FederationStatement;
import org.monet.grided.core.serializers.xml.space.SpaceData.PublicationFrontComponentStatement;
import org.monet.grided.core.serializers.xml.space.SpaceData.PublicationFrontComponentStatement.PublicationCubeStatement;
import org.monet.grided.core.serializers.xml.space.SpaceData.PublicationFrontComponentStatement.PublicationMapStatement;
import org.monet.grided.core.serializers.xml.space.SpaceData.PublicationFrontComponentStatement.PublicationServiceStatement;
import org.monet.grided.core.serializers.xml.space.SpaceData.PublicationFrontComponentStatement.PublicationThesaurusStatement;
import org.monet.grided.core.services.space.data.ServiceType;

import com.google.inject.Inject;

public class JSONSpaceSerializer implements JSONSerializer<JSONObject, Space> {
    
    private JSONModelVersionSerializer modelVersionSerializer;

    @Inject
    public JSONSpaceSerializer(JSONModelVersionSerializer serializer) {
        this.modelVersionSerializer = serializer;
    }
    
    @Override
    public JSONObject serialize(Space space) {
        JSONObject json = new JSONObject();

        json.put(PropertyNames.ID, space.getId());
        json.put(PropertyNames.NAME, space.getName());      
        json.put(PropertyNames.LABEL, space.getData().getLabel());        
        json.put(PropertyNames.LOGO, space.getData().getLogoFilename());
        json.put(PropertyNames.URL, space.getData().getUrl()); 
        json.put(PropertyNames.DATAWAREHOUSE, space.getData().getDatawarehouse());
        
        if (space.getModelVersion() != null) {
            json.put(PropertyNames.MODEL_VERSION, this.modelVersionSerializer.serialize(space.getModelVersion()));
        }
        
        JSONObject jsonState = new JSONObject();                
        jsonState.put(PropertyNames.RUNNING, space.isRunning());
        jsonState.put(PropertyNames.TIME, space.getState().getRunningFrom());
        
        json.put(PropertyNames.STATE, jsonState);
        
        Federation federation = space.getFederation();

        JSONObject jsonFederation = new JSONObject();
        jsonFederation.put(PropertyNames.ID, federation.getId());
        jsonFederation.put(PropertyNames.NAME, federation.getName());

        Server server = federation.getServer();

        JSONObject jsonServer = new JSONObject();
        jsonServer.put(PropertyNames.ID, server.getId());
        jsonServer.put(PropertyNames.NAME, server.getName());
        jsonServer.put(PropertyNames.IP, server.getIp());

        jsonFederation.put(PropertyNames.SERVER, jsonServer);
        json.put(PropertyNames.FEDERATION, jsonFederation);

        json.put(PropertyNames.SERVICES, new JSONArray());

        SpaceData data = space.getData();
        Iterator<? extends PublicationFrontComponentStatement> iterator = data.getPublicationFrontComponentStatements().iterator();

        JSONArray jsonServiceStatements = new JSONArray();
        while (iterator.hasNext()) {
            JSONObject jsonServiceStatement = new JSONObject();
            PublicationFrontComponentStatement statement = iterator.next();

            String type = "";
            if (statement instanceof PublicationServiceStatement)
                type = ServiceType.service.toString();
            else if (statement instanceof PublicationThesaurusStatement)
                type = ServiceType.thesaurus.toString();
            else if (statement instanceof PublicationCubeStatement)
                type = ServiceType.cube.toString();
            else if (statement instanceof PublicationMapStatement)
                type = ServiceType.map.toString();

            jsonServiceStatement.put("name", statement.getName());
            jsonServiceStatement.put(PropertyNames.TYPE, type);
            jsonServiceStatements.add(jsonServiceStatement);
        }
        json.put(PropertyNames.SERVICES, jsonServiceStatements);

        return json;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Space unserialize(String text) {
        JSONObject json = (JSONObject) net.sf.json.JSONSerializer.toJSON(text);

        String federationText = json.getString(PropertyNames.FEDERATION);
        JSONObject jsonFederation = (JSONObject) net.sf.json.JSONSerializer.toJSON(federationText);

        String serverText = jsonFederation.getString(PropertyNames.SERVER);
        JSONObject jsonServer = (JSONObject) net.sf.json.JSONSerializer.toJSON(serverText);

        Server server = new Server(Long.valueOf(jsonServer.getString(PropertyNames.ID)), jsonServer.getString(PropertyNames.NAME), jsonServer.getString(PropertyNames.IP));
        Federation federation = new Federation(server);
        federation.setId(jsonFederation.getLong(Params.ID));
        federation.setName(jsonFederation.getString(PropertyNames.NAME));

        Space space = new Space(federation);
        space.setId(json.getLong(PropertyNames.ID));
        space.setName(json.getString(PropertyNames.NAME));
        
        String modelVersionText = json.getString(PropertyNames.MODEL_VERSION);                
        ModelVersion modelVersion = this.modelVersionSerializer.unserialize(modelVersionText); 
        space.setModelVersion(modelVersion);        
        
        String stateText = json.getString(PropertyNames.STATE);        
        JSONObject jsonState = (JSONObject) net.sf.json.JSONSerializer.toJSON(stateText);        
        boolean running = jsonState.getBoolean(PropertyNames.RUNNING);
        
        State state = State.stopped();
        if (running) {
          state = State.running(jsonState.getLong(PropertyNames.TIME));    
        }        
        space.setState(state);
        
        SpaceData data = new SpaceData();
                
        Organization organization = new Organization(json.getString(PropertyNames.NAME), json.getString(PropertyNames.URL));        
        organization.setLabel(json.getString(PropertyNames.LABEL));                        
        organization.setLogo(new Logo(json.getString(PropertyNames.LOGO)));
        organization.setUrl(json.getString(PropertyNames.URL));       
        data.setOrganization(organization);
        
        data.setDatawarhouse(json.getBoolean(PropertyNames.DATAWAREHOUSE));
        
        FederationStatement federationStatement = new FederationStatement(server.getIp(), jsonFederation.getString(PropertyNames.NAME));
        data.setFederationStatement(federationStatement);

        JSONArray jsonServices = json.getJSONArray(PropertyNames.SERVICES);

        Iterator iter = jsonServices.iterator();

        while (iter.hasNext()) {
            JSONObject jsonService = (JSONObject) iter.next();
            String statementName = jsonService.getString(PropertyNames.NAME);
            String type = jsonService.getString(PropertyNames.TYPE);
            boolean published = jsonService.getBoolean(PropertyNames.PUBLISHED);
            ServiceType serviceType = ServiceType.valueOf(type);

            if (!published)
                continue;

            switch (serviceType) {
            case thesaurus:
                data.addPublicationThesaurusStatement(new PublicationThesaurusStatement(statementName));
                break;
            case service:
                data.addPublicationServiceStatement(new PublicationServiceStatement(statementName));
                break;
            case cube:
                data.addPublicationCubeStatements(new PublicationCubeStatement(statementName));
                break;
            case map:
                data.addPublicationMapStatement(new PublicationMapStatement(statementName));
                break;
            }
        }

        space.setData(data);
        return space;
    }
}
