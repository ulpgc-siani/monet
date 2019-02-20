function ProviderSerializer() {
	
}

ProviderSerializer.prototype.unserialize = function(serialized) {
  var provider = new Provider();	
  try {
    var jsonData = Ext.util.JSON.decode(serialized);
    provider = new Provider(jsonData);        
  } catch (e) {
  	console.log("Error unserialize provider: " + e.message);
  }
  return provider;  
};