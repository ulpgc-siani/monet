package org.monet.grided.core.serializers.json.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.monet.grided.core.serializers.json.JSONResponse;

public class JSONErrorResponse implements JSONResponse {

    private static final JSONObject jsonError; 
    static {
        jsonError = new JSONObject();
        jsonError.put("type", "error");
        jsonError.put("data", "");         
    }
    
    public static final String ERROR = jsonError.toString();

    private JSONObject json;

    public JSONErrorResponse(JSONObject data) {        
        this.json = new JSONObject();
        this.json.put("type", "error");
        this.json.put("data", data);                
    }
    
    public JSONErrorResponse(String errorCode) {        
        this.json = new JSONObject();
        this.json.put("type", "error");
        this.json.put("code", errorCode);
        this.json.put("data", "");                
    }
    
    public JSONErrorResponse(JSONArray data) {        
        this.json = new JSONObject();
        this.json.put("type", "error");
        this.json.put("data", data);                
    }
    
    @Override
    public String toString() {
        return this.json.toString();        
    }
}

