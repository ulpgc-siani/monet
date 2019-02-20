
function Map(data) {   
  this.name = "";  
  this.reference = "";
  this.key = "";
  this.clients = [];

  if (data != null) { 
	this.copyFrom(data); 
  }
}

//---------------------------------------------------------------------
Map.prototype.copyFrom = function(data) {    
  this.name = data.name;
  this.reference = data.reference;
  this.key = data.key;
  this.clients = data.clients;  
};

