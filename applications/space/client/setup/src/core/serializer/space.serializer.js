function BusinessSpaceSerializer() {
}

BusinessSpaceSerializer.prototype.unserialize = function(businessSpace, serialized) {
	try {
	  var jsonData = Ext.util.JSON.decode(serialized);
			  
	  businessSpace.businessUnit = jsonData.businessunit;	  
	  businessSpace.organization = jsonData.organization;
	  businessSpace.modelName = jsonData.modelname;
	  businessSpace.updateDate = jsonData.update_date;
	  businessSpace.images = jsonData.images;	  
	} catch (e) {
		console.log("Error unserialize businessspace: " + e.message);
	}		
  return businessSpace;  
};
