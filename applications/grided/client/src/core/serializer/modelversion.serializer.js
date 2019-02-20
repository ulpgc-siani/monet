ModelVersionSerializer = {
    
  serialize : function(version) {
    var json = {};
    json.id = version.id;
    json.label = version.get(ModelVersion.LABEL);
    json.date  = version.get(ModelVersion.DATE);
    json.metamodel = version.get(ModelVersion.METAMODEL);
        
    var text = Ext.util.JSON.encode(json);
    return text;
  },
    
  unserialize : function(json) {
    var modelVersion = new ModelVersion();
    modelVersion.id = json.id;
    modelVersion.set(ModelVersion.LABEL, json.label, {silent: true});
    modelVersion.set(ModelVersion.DATE, json.date, {silent: true});
    modelVersion.set(ModelVersion.METAMODEL, json.metamodel, {silent: true});
        
    return modelVersion;
  }
};