ModelsSerializer = {
	serialize : function(models) {
		
	},
	
  unserialize : function(jsonModels) {
    var models = new Collection();

    for (var i=0; i < jsonModels.length; i++)  {
      if (_.isFunction(jsonModels[i])) continue;

      var jsonModel = jsonModels[i];
      var model = new BusinessModel();
      model.id = jsonModel.id;
      model.set(BusinessModel.NAME , jsonModel.name, {silent: true});		  
      models.add(model, {silent: true});
    }  
    return models;
  }
};