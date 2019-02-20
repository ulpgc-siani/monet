function Service(data) {   
  this.id = "";  
  this.code = "";
  this.name = "";
  this.type = "";
  this.enabled = false; 

  if (data != null) { 
	this.copyFrom(data); 
  }
}

//---------------------------------------------------------------------
Service.prototype.copyFrom = function(data) {    
  this.id = data.id;
  this.code = data.code;
  this.name = data.name;
  this.type = data.type;
  this.enabled = data.enabled;
};

