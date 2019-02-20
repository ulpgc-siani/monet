function BusinessModelSerializer() {
}

BusinessModelSerializer.prototype.unserialize = function(businessModel, serialized) {
	try {
	  var jsonData = Ext.util.JSON.decode(serialized);

	  businessModel.code  = jsonData.code;
	  businessModel.label = jsonData.label;
	  businessModel.version = jsonData.version;
	  businessModel.updateDate = jsonData.update_date;
	  businessModel.images = jsonData.images;
	  	  	  	  
	} catch (e) {
		console.log("Error unserialize businessmodel: " + e.message);
	}		
  return businessModel;  
};
