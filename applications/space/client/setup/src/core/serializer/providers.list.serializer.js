function ProvidersListSerializer() {
	
}

ProvidersListSerializer.prototype.unserialize = function(serialized) {
  var providersList = new Array();
  try {
    var jsonData = Ext.util.JSON.decode(serialized);	
    this.unserializeItem(jsonData, function(item) {providersList.push(item);});
  } catch (e) {
  	console.log("Error unserialize providers: " + e.message);
  }
  return providersList;  
};

ProvidersListSerializer.prototype.unserializeItem = function(jsonData, handler) {
  for (var i=0; i < jsonData.length;i++) {
    var item = jsonData[i];
 	var provider = new Provider(item);
	handler(provider);
  }
};