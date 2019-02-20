package org.monet.grided.core.serializers.json.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.monet.grided.constants.PropertyNames;
import org.monet.grided.core.serializers.json.JSONResponse;

public class JSONSuccessResponse implements JSONResponse {

    private static final JSONObject jsonOK; 
    static {
        jsonOK = new JSONObject();
        jsonOK.put("type", "success");
        jsonOK.put("data", "");         
    }
    
    public static final String OK = jsonOK.toString();            
    private JSONObject json;
    
    
    public JSONSuccessResponse(JSONObject data) {        
        this.json = new JSONObject();
        this.json.put(PropertyNames.TYPE, "success");
        this.json.put(PropertyNames.DATA, data);                
    }
    
    public JSONSuccessResponse(JSONArray data) {        
        this.json = new JSONObject();
        this.json.put(PropertyNames.TYPE, "success");
        this.json.put(PropertyNames.DATA, data);                
    }          

    @Override
    public String toString() {
        return this.json.toString();        
    }
}

