package org.monet.grided.core.serializers.json.impl;

import net.sf.json.JSONObject;

import org.monet.grided.constants.PropertyNames;
import org.monet.grided.core.model.FederationAccount;
import org.monet.grided.core.serializers.json.JSONSerializer;

public class JSONAccountSerializer implements JSONSerializer<JSONObject, FederationAccount>{

    @Override
    public JSONObject serialize(FederationAccount account) {
        JSONObject json = new JSONObject();
        json.put(PropertyNames.USERNAME, account.getUserName());
        json.put(PropertyNames.EMAIL, account.getEmail());        
        return json;
    }

    @Override
    public FederationAccount unserialize(String json) {
        // TODO Auto-generated method stub
        return null;
    }
}

