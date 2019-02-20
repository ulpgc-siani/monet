function ServiceListSerializer() {
	
}

ServiceListSerializer.prototype.unserialize = function(serialized) {
  var services = new Array();
  try {
    var jsonData = Ext.util.JSON.decode(serialized);	
	this.unserializeItem(jsonData, function(item) {services.push(item);});
  } catch (e) {
	console.log("Error unserialize services: " + e.message);
  }
  return services;  
};

ServiceListSerializer.prototype.unserializeItem = function(jsonData, handler) {
  for (var i=0; i < jsonData.length;i++) {
	var item = jsonData[i];
	var service = new Service(item);
	handler(service);		
  }
};