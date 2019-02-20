package org.monet.grided.core.serializers.json.impl;

import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.monet.grided.constants.PropertyNames;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.serializers.json.JSONSerializer;

import com.google.inject.Inject;

public class JSONServerSerializer implements JSONSerializer<JSONObject, Server> {

    private JSONServerStateSerializer serverStateSerializer;

    @Inject
    public JSONServerSerializer(JSONServerStateSerializer serverStateSerializer) {
        this.serverStateSerializer = serverStateSerializer;
    }
    
    @Override
    public JSONObject serialize(Server server) {
        JSONObject json = new JSONObject();
        json.put(PropertyNames.ID, server.getId());
        json.put(PropertyNames.NAME, server.getName());        
        json.put(PropertyNames.IP, server.getIp());                
        json.put(PropertyNames.ENABLED, server.isEnabled());                
        json.put(PropertyNames.STATE, server.getServerState());
        
        JSONArray jsonFederations = new JSONArray();
        Iterator<Federation> iter = server.iterator();
        
        while (iter.hasNext()) {
          JSONObject jsonFederation = new JSONObject();  
          Federation federation = iter.next();
          jsonFederation.put(PropertyNames.ID, federation.getId());
          jsonFederation.put(PropertyNames.NAME, federation.getName());
          jsonFederations.add(jsonFederation);
        }
        
        json.put(PropertyNames.FEDERATIONS, jsonFederations);
        
        json.put(PropertyNames.SERVER_STATE, this.serverStateSerializer.serialize(server.getServerState()));
        
        return json;
    }

    @Override
    public Server unserialize(String text) {
        JSONObject json =  (JSONObject) net.sf.json.JSONSerializer.toJSON( text);    
        Server server = new Server(json.getLong(PropertyNames.ID), json.getString(PropertyNames.NAME), json.getString(PropertyNames.IP));        
        server.setEnabled(json.getBoolean(PropertyNames.ENABLED));
        return server;
    }       
}
