
function FederationView(layername) {
  var html = AppTemplate.ViewFederation;  
  this.layername = layername;
  this.$layer = $(layername);	
  this.$layer.innerHTML = translate(html, Lang.FederationView);       

  this.federation = null;
}

//-------------------------------------------------------------------------------------------------------
FederationView.prototype.setTarget = function(model) {
  this.federation = model;	
  this.bindData(model);
};

//-------------------------------------------------------------------------------------------------------
FederationView.prototype.show = function() {
  this.$layer.show();
};

//-------------------------------------------------------------------------------------------------------
FederationView.prototype.hide = function() {    
  this.$layer.hide();  
};

//-------------------------------------------------------------------------------------------------------
FederationView.prototype.refresh = function() {
  console.log("SE LLAMA AL REFRESH DEL FEDERATION VIEW");
//  if (this.federation != null) {
//	this.bindData(this.federation);	
//  }
};

//-------------------------------------------------------------------------------------------------------
FederationView.prototype.bindData = function(model) {
  var extServer = Ext.get(this.layername).select('server').first();    
  var extKey = Ext.get(this.layername).select('.key').first();
  var extSecret = Ext.get(this.layername).select('.secret').first();

  if (extServer) extServer.dom.innerHTML = model.server;
  if (extKey) extKey.dom.innerHTML = model.key;
  if (extSecret) extSecret.dom.innerHTML = model.secret;     	
};
