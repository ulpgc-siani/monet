ModelSerializer = {

  serialize : function(model) {
	  var json = {};
	  json.id = model.id;
	  json.label = model.get(BusinessModel.LABEL);
	  json.logo  = model.get(BusinessModel.LOGO);
	  
	  var jsonVersions = [];
	  var versions = model.get(BusinessModel.VERSIONS);
	  
	  versions.each(function(index, version) {
	    var jsonVersion = ModelVersionSerializer.serialize(version);
	    jsonVersions.push(jsonVersion);	    
	  });
	  
	  json.versions = jsonVersions;
	  var text = Ext.util.JSON.encode(json);
	  return text;
  },
  
  unserialize : function(jsonModel) {
    var model = new BusinessModel();
    model.id = jsonModel.id;
    model.set(BusinessModel.NAME, jsonModel.name, {silent: true});
    model.set(BusinessModel.LABEL, jsonModel.label, {silent: true});
    model.set(BusinessModel.LOGO, jsonModel.logo, {silent: true});
    model.set(BusinessModel.LATEST_VERSION, jsonModel.latest_version, {silent: true});
    
    var versions = new Collection();
    
    for (var i=0, l = jsonModel.versions.length; i < l; i++) {      
      var jsonVersion = jsonModel.versions[i];
      var version = ModelVersionSerializer.unserialize(jsonVersion);
      versions.add(version, {silent: true});           
    }
    
    model.set(BusinessModel.VERSIONS, versions, {silent: true});    
    return model;
  }
};