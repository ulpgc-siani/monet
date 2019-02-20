function ThesaurusListSerializer() {
	
}

ThesaurusListSerializer.prototype.unserialize = function(serialized) {
  var thesaurusList = new Array();
  try {
    var jsonData = Ext.util.JSON.decode(serialized);	
	this.unserializeItem(jsonData, function(item) {thesaurusList.push(item);});
  } catch (e) {
	console.log("Error unserialize thesaurus: " + e.message);
  }
  return thesaurusList;  
};

ThesaurusListSerializer.prototype.unserializeItem = function(jsonData, handler) {
  for (var i=0; i < jsonData.length;i++) {
    var item = jsonData[i];
	var thesaurus = new Thesaurus(item);
	handler(thesaurus);
  }
};