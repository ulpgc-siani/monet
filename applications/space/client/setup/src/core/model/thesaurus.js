function Thesaurus(data) {
  this.label = "";
  this.code = "";
  this.parent = "";
  this.url    = "";
  this.bExternal = false;
  this.enabled = true;
  this.values = [];
  providerName = "";
  
  if (data != null) {
	this.copyFrom(data);
  }
}

//---------------------------------------------------------------------
Thesaurus.prototype.isExternal = function() {
  return this.bExternal;  	
};

//---------------------------------------------------------------------
Thesaurus.prototype.copyFrom = function (data) {
  this.label = data.label;
  this.code = data.code;
  this.parent = data.parent;
  this.url    = data.url;
  this.bExternal = data.external;
  this.enabled = data.enabled;
  this.values = data.values;
  this.providerName = data.providerName;
};