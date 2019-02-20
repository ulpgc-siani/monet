package org.monet.grided.core.serializers.json.impl;

import net.sf.json.JSONObject;

import org.monet.grided.constants.PropertyNames;
import org.monet.grided.core.model.JSImage;
import org.monet.grided.core.serializers.json.JSONSerializer;

public class JSONImageSerializer implements JSONSerializer<JSONObject, JSImage> {

    @Override
    public JSONObject serialize(JSImage image) {
        JSONObject json = new JSONObject();
        json.put(PropertyNames.SOURCE, image.getSource());
        json.put(PropertyNames.NAME, image.getName());        
        return json;
    }

    @Override
    public JSImage unserialize(String json) {
        // TODO Auto-generated method stub
        return null;
    }
}

