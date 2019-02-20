package org.monet.grided.core.serializers.json.impl;

import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.monet.grided.constants.PropertyNames;
import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.model.State;
import org.monet.grided.core.serializers.json.JSONSerializer;
import org.monet.grided.core.serializers.xml.Logo;
import org.monet.grided.core.serializers.xml.Organization;
import org.monet.grided.core.serializers.xml.federation.DatabaseConnection;
import org.monet.grided.core.serializers.xml.federation.DatabaseConnection.DatabaseConnectionConfiguration;
import org.monet.grided.core.serializers.xml.federation.FederationData;
import org.monet.grided.core.serializers.xml.federation.FederationData.Authentication;
import org.monet.grided.core.serializers.xml.federation.FederationData.Connection;
import org.monet.grided.core.serializers.xml.federation.FederationData.ConnectionTypes;
import org.monet.grided.core.serializers.xml.federation.LDAPConnection;
import org.monet.grided.core.serializers.xml.federation.LDAPConnection.LDAPConnectionConfiguration;
import org.monet.grided.core.serializers.xml.federation.LDAPConnection.LDAPConnectionConfiguration.Parameter;
import org.monet.grided.core.serializers.xml.federation.MockConnection;

import com.google.inject.Inject;

public class JSONFederationSerializer implements JSONSerializer<JSONObject, Federation> {
       
    private Configuration config;
    private String language;
    
    @Inject
    public JSONFederationSerializer(Configuration config) {
        this.config = config;
        this.language = this.config.getProperty(Configuration.LANGUAGE);
    }
    
    @Override
    public JSONObject serialize(Federation federation) {
        JSONObject json = new JSONObject();
        
        json.put(PropertyNames.ID, federation.getId());
        json.put(PropertyNames.NAME, federation.getName());
        json.put(PropertyNames.LABEL, federation.getData().getLabelText(language));
        json.put(PropertyNames.LOGO, federation.getData().getLogoFilename());
        json.put(PropertyNames.URL, federation.getData().getUrl());        
        
        JSONObject jsonState = new JSONObject();                
        jsonState.put(PropertyNames.RUNNING, federation.isRunning());
        jsonState.put(PropertyNames.TIME, federation.getState().getRunningFrom());
        
        json.put(PropertyNames.STATE, jsonState);
        
        Server server = federation.getServer();
        JSONObject jsonServer = new JSONObject();
        jsonServer.put(PropertyNames.ID, server.getId());
        jsonServer.put(PropertyNames.NAME, server.getName());
        jsonServer.put(PropertyNames.IP, server.getIp());
        json.put(PropertyNames.SERVER, server);
                        
        JSONObject jsonAuthentication = serializeAuthentication(federation);
        json.put(PropertyNames.AUTHENTICATION, jsonAuthentication);
        
        JSONObject jsonConnection = serializeConnection(federation);
        if (jsonConnection != null) json.put(PropertyNames.CONNECTION, jsonConnection);                
        
        Iterator<Space> iter = federation.iterator();
        JSONArray jsonSpaces = new JSONArray(); 

        while (iter.hasNext()) {
            JSONObject jsonSpace = new JSONObject();
            Space space = iter.next();
            jsonSpace.put(PropertyNames.ID, space.getId());
            jsonSpace.put(PropertyNames.NAME, space.getName());

            JSONObject jsonSpaceState = new JSONObject();                
            jsonSpaceState.put(PropertyNames.RUNNING, space.isRunning());
            jsonSpaceState.put(PropertyNames.TIME, space.getState().getRunningFrom());            
            jsonSpace.put(PropertyNames.STATE, jsonSpaceState);
            
            jsonSpaces.add(jsonSpace);
        }
        json.put(PropertyNames.SPACES, jsonSpaces);        

        return json;                
    }

    private JSONObject serializeAuthentication(Federation federation) {
        JSONObject json = new JSONObject();
        Authentication authentication = (federation.getData() != null)? federation.getData().getAuthentication() : null;        
        
        boolean userAuth = (authentication != null && authentication.getPassword())? true : false;
        boolean certificate = (authentication != null && authentication.getCertificate())? true : false;
        boolean openId = (authentication != null && authentication.getOpenId())? true : false;        
            
        json.put(PropertyNames.USER_AUTH, userAuth);
        json.put(PropertyNames.CERTIFICATE, certificate);
        json.put(PropertyNames.OPENID, openId);
        
        return json;
    }
    
    private JSONObject serializeConnection(Federation federation) {
        JSONObject json = new JSONObject();
        Connection connection = (federation.getData() != null)? federation.getData().getConnection() : null;        
        
        if (connection == null) return null;
                        
        if (connection instanceof DatabaseConnection) json = this.serializerDatabaseConnection(connection);        
        else if (connection instanceof LDAPConnection) json = this.serializerLDAPConnection(connection);
        else if (connection instanceof MockConnection) json = this.serializerMockConnection(connection);
                        
        return json;
    }

    private JSONObject serializerDatabaseConnection(Connection connection) {
        JSONObject json = new JSONObject();
                
        json.put(PropertyNames.TYPE, connection.getType());
        json.put(PropertyNames.URL, connection.getUrl());
        json.put(PropertyNames.USER, connection.getUser());
        json.put(PropertyNames.PASSWORD, connection.getPassword());
                        
        DatabaseConnectionConfiguration config = ((DatabaseConnection) connection).getConfiguration();
            
        if (config == null) return json;
        
        JSONObject jsonConfig = new JSONObject();
        
        jsonConfig.put(PropertyNames.DATABASE_TYPE, config.getDatabaseType());
        json.put(PropertyNames.CONFIG, jsonConfig);
                
        return json;        
    }
    
    private JSONObject serializerLDAPConnection(Connection connection) {
        JSONObject json = new JSONObject();
        
        json.put(PropertyNames.TYPE, connection.getType());
        json.put(PropertyNames.URL, connection.getUrl());
        json.put(PropertyNames.USER, connection.getUser());
        json.put(PropertyNames.PASSWORD, connection.getPassword());
        
        LDAPConnectionConfiguration config = ((LDAPConnection) connection).getConfiguration();
        
        if (config == null) return json;

        JSONObject jsonConfig = new JSONObject();
        
        jsonConfig.put(PropertyNames.SCHEMA, config.getSchema());
        
        if (config.getParameters().size() > 0) {
            JSONArray jsonParameters = new JSONArray();
            
            for (Parameter parameter : config.getParameters()) {
                JSONObject jsonParameter = new JSONObject();
                
                jsonParameter.put(PropertyNames.NAME, parameter.getName());
                jsonParameter.put(PropertyNames.ALIAS, parameter.getAs());                
                jsonParameters.add(jsonParameter);
            }
            jsonConfig.put(PropertyNames.PARAMETERS, jsonParameters);
        }
                        
        return json;                
    }

    private JSONObject serializerMockConnection(Connection connection) {
        JSONObject json = new JSONObject();
        
        json.put(PropertyNames.TYPE, connection.getType());
        json.put(PropertyNames.URL, connection.getUrl());
        json.put(PropertyNames.USER, connection.getUser());
        json.put(PropertyNames.PASSWORD, connection.getPassword());
        
        return json;
    }

    
    @Override
    public Federation unserialize(String text) {
        JSONObject json =  (JSONObject) net.sf.json.JSONSerializer.toJSON(text);
        
        String serverText = json.getString(PropertyNames.SERVER);
        JSONObject jsonServer = (JSONObject) net.sf.json.JSONSerializer.toJSON(serverText);                
                
        Server server = new Server(Long.valueOf(jsonServer.getString(PropertyNames.ID)), jsonServer.getString(PropertyNames.NAME), jsonServer.getString(PropertyNames.IP));
        Federation federation = new Federation(server);
        federation.setId(json.getLong(Params.ID));
        federation.setName(json.getString(PropertyNames.NAME));

        String stateText = json.getString(PropertyNames.STATE);        
        JSONObject jsonState = (JSONObject) net.sf.json.JSONSerializer.toJSON(stateText);        
        boolean running = jsonState.getBoolean(PropertyNames.RUNNING);
        
        State state = State.stopped();
        if (running) {
          state = State.running(jsonState.getLong(PropertyNames.TIME));    
        }        
        federation.setState(state);
        
        FederationData data = new FederationData();
        Authentication authentication = new Authentication();
        authentication.setPassword(json.getBoolean(PropertyNames.USER_AUTH));
        authentication.setCertificate(json.getBoolean(PropertyNames.CERTIFICATE));
        authentication.setOpenId(json.getBoolean(PropertyNames.OPENID));
        
        data.setAuthentication(authentication);
        
        Organization organization = new Organization();       
        organization.setName(json.getString(PropertyNames.NAME));
        organization.setLogo(new Logo(json.getString(PropertyNames.LOGO)));
        organization.setLabel(json.getString(PropertyNames.LABEL));        
        organization.setUrl(json.getString(PropertyNames.URL));
        data.setOrganization(organization);
        
        String connectionText = json.getString(PropertyNames.CONNECTION);                       
        Connection connection = this.unSerializeConnection(connectionText);
        data.setConnection(connection);        
        
        federation.setData(data);        
        return federation;
    }
    
    private Connection unSerializeConnection(String text) {        
        Connection connection = null;
        JSONObject json = (JSONObject) net.sf.json.JSONSerializer.toJSON(text);        
                        
        if (json.getString(PropertyNames.TYPE).equals(ConnectionTypes.DATABASE )) connection = this.unSerializeDatabaseConnection(json);
        else if (json.getString(PropertyNames.TYPE).equals(ConnectionTypes.LDAP)) connection = this.unSerializeLDAPConnection(json);
        else if (json.getString(PropertyNames.TYPE).equals(ConnectionTypes.MOCK)) connection = this.unSerializeMockConnection(json);
                               
        return connection;
    }
    
    private DatabaseConnection unSerializeDatabaseConnection(JSONObject json) {
        String url = json.getString(PropertyNames.URL);
        String user = json.getString(PropertyNames.USER);
        String password = json.getString(PropertyNames.PASSWORD);
        
        DatabaseConnection connection = new DatabaseConnection(url, user, password);
        
        JSONObject jsonConfig = (JSONObject) net.sf.json.JSONSerializer.toJSON(json.getString(PropertyNames.CONFIG));
        DatabaseConnectionConfiguration config = new DatabaseConnectionConfiguration(jsonConfig.getString(PropertyNames.DATABASE_TYPE));
        connection.setConfiguration(config);
        
        return connection;                
    }

    private LDAPConnection unSerializeLDAPConnection(JSONObject json) {             
        String url = json.getString(PropertyNames.URL);
        String user = json.getString(PropertyNames.USER);
        String password = json.getString(PropertyNames.PASSWORD);
        
        LDAPConnection connection = new LDAPConnection(url, user, password);  
        
        JSONObject jsonConfig = (JSONObject) net.sf.json.JSONSerializer.toJSON(json.getString(PropertyNames.CONFIG));
        LDAPConnectionConfiguration config = new LDAPConnectionConfiguration(jsonConfig.getString(PropertyNames.DATABASE_TYPE));
        connection.setConfiguration(config);
        
        return connection;
    }

    private MockConnection unSerializeMockConnection(JSONObject json) {              
        String url = json.getString(PropertyNames.URL);
        String user = json.getString(PropertyNames.USER);
        String password = json.getString(PropertyNames.PASSWORD);
        
        MockConnection connection = new MockConnection(url, user, password);  
        
        return connection;
    }
}

