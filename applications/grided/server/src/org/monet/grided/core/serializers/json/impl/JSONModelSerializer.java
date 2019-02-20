package org.monet.grided.core.serializers.json.impl;

import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.monet.grided.constants.PropertyNames;
import org.monet.grided.core.model.MetaModelVersion;
import org.monet.grided.core.model.Model;
import org.monet.grided.core.model.ModelVersion;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.serializers.json.JSONSerializer;

import com.google.inject.Inject;

public class JSONModelSerializer implements JSONSerializer<JSONObject, Model> {

    private JSONModelVersionSerializer modelVersionSerializer;
    private JSONModelVersionsSerializer modelVersionsSerializer;

    @Inject
    public JSONModelSerializer(JSONModelVersionSerializer modelVersionSerializer, JSONModelVersionsSerializer modelVersionsSerializer) {
        this.modelVersionSerializer = modelVersionSerializer;
        this.modelVersionsSerializer = modelVersionsSerializer;
    }
    
    @Override
    public JSONObject serialize(Model model) {
        JSONObject json = new JSONObject();
        
        json.put(PropertyNames.ID, model.getId());
        json.put(PropertyNames.NAME, model.getName());               
        json.put(PropertyNames.LABEL, model.getData().getLabel());
        json.put(PropertyNames.LOGO, "");        
        
        if (model.getLatestVersion() != null) {
            json.put(PropertyNames.LATEST_VERSION, this.modelVersionSerializer.serialize(model.getLatestVersion()));            
        }

        json.put(PropertyNames.VERSIONS, this.modelVersionsSerializer.serialize(model.getVersions()));
                
        return json;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Model unserialize(String text) {
        JSONObject json = (JSONObject) net.sf.json.JSONSerializer.toJSON(text);
        
        Model model = new Model(json.getLong(PropertyNames.ID), json.getString(PropertyNames.LABEL));
                
        String versionsText = json.getString(PropertyNames.VERSIONS);
        JSONArray jsonVersions = (JSONArray) net.sf.json.JSONSerializer.toJSON(versionsText);        
        Iterator versionsIterator = jsonVersions.iterator();
                
        while (versionsIterator.hasNext()) {
            JSONObject jsonVersion = (JSONObject) versionsIterator.next();
            long   versionId    = jsonVersion.getLong(PropertyNames.ID);
            String versionLabel = jsonVersion.getString(PropertyNames.LABEL);
            long   date  = jsonVersion.getLong(PropertyNames.DATE);
            String metamodel = jsonVersion.getString(PropertyNames.METAMODEL);
                        
            ModelVersion version = new ModelVersion(versionId, model, versionLabel, date, new MetaModelVersion(metamodel));
            
            String spacesText = jsonVersion.getString(PropertyNames.SPACES);
            JSONArray jsonSpaces = (JSONArray) net.sf.json.JSONSerializer.toJSON(spacesText);
            Iterator spacesIterator = jsonSpaces.iterator();
                        
            while (spacesIterator.hasNext()) {
                JSONObject jsonSpace = (JSONObject) spacesIterator.next();
                long   spaceId    = jsonSpace.getLong(PropertyNames.ID);
                String spaceName  = jsonSpace.getString(PropertyNames.NAME);
                Space space = new Space(spaceId);
                space.setName(spaceName);
                
                version.addSpace(space);
            }
            
            model.addVersion(version);            
        }
                
        return model;        
    }    
}