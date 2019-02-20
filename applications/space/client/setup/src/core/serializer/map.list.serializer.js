function MapListSerializer() {
	
}

MapListSerializer.prototype.unserialize = function(serialized) {
  var maps = new Array();
  try {
    var jsonData = Ext.util.JSON.decode(serialized);	
	this.unserializeItem(jsonData, function(item) {services.push(item);});
  } catch (e) {
	console.log("Error unserialize maps: " + e.message);
  }
  return maps;  
};

MapListSerializer.prototype.unserializeItem = function(jsonData, handler) {
  for (var i=0; i < jsonData.length;i++) {
	var item = jsonData[i];
	var map = new Map(item);
	handler(map);		
  }
};