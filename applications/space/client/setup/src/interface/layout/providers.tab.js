function ProvidersTab(extTabs, layername) {
  this.extTabs = extTabs;
  this.layername = layername;
  this.providersTab = this.extTabs.addTab(layername, Lang.BottomLayout.Providers);
  this.providersTab.on('activate', this._activateHandler, this);
  this.initialized = false;
}

//------------------------------------------------------------------------------------
ProvidersTab.prototype._activateHandler = function() {
  this.providersView.refresh();
};

//------------------------------------------------------------------------------------
ProvidersTab.prototype._init = function() {
  if (this.initialized) return;
  var html = AppTemplate.ProvidersTab;
  this.$layer = $(this.layername);
  this.$layer.innerHTML = html;
  this.initialized = true;
  
  this.providersView = new ProvidersView('providers-view');
  
  this._renderTab();
};

//------------------------------------------------------------------------------------
ProvidersTab.prototype.activate = function() {
  this.extTabs.activate(this.providersTab.id);	
};

//------------------------------------------------------------------------------------
ProvidersTab.prototype.enable = function() {
  if (! this.initialized) this._init();	
  this.extTabs.enableTab(this.providersTab.id);	
};

//------------------------------------------------------------------------------------
ProvidersTab.prototype.disable = function() {
  this.extTabs.disableTab(this.providersTab.id);
};

//------------------------------------------------------------------------------------
ProvidersTab.prototype.refresh = function() {
//  if (! this.initialized) this._init();    
//  this._renderTab();
  console.log("borrar caller: " + this.caller);	
};

//------------------------------------------------------------------------------------
ProvidersTab.prototype._renderTab = function() {
  this.providersView.show();
};



