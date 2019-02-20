function ProviderDefinition(data) {  
  this.code = '';
  this.label = '';
  this.service = '';
  this.type = '';
  this.isInstanced = false;
  
  if (data != null) this.copyFrom(data);
};

//---------------------------------------------------------------------
ProviderDefinition.prototype.copyFrom = function(data) {
  this.code = data.code;
  this.label = data.label;
  this.service = data.service;
  this.type = data.type;
};
