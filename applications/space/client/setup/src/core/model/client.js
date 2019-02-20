function Client(data) {
  this.id = ""	
  this.name = "";
  
  if (data != null) {
	this.copyFrom(data);
  }
}

//---------------------------------------------------------------------
Client.prototype.copyFrom = function (data) {
  this.id = data.id;
  this.name = data.name;  
};