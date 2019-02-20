function Provider(data) {
  this.id = '';
  this.code = '';
  this.label = '';
  this.service = '';
  this.url = '';
  this.type = '';
  
  if (data != null) this.copyFrom(data);
};

//---------------------------------------------------------------------
Provider.prototype.copyFrom = function(data) {
  this.id = data.id;	
  this.code = data.code;
  this.label = data.label;
  this.name = data.name;
  this.service = data.service;
  this.url = data.url;
  this.type = data.type;
};