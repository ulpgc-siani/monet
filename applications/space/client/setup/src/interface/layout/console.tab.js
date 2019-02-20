function ConsoleTab(extTabs, layername) {
  this.extTabs = extTabs;
  this.layername = layername;
  this.consoleTab = this.extTabs.addTab(layername, Lang.BottomLayout.Console);
  this.consoleTab.on('activate', this._activateHandler, this);
  this.initialized = false;
}

//------------------------------------------------------------------------------------
ConsoleTab.prototype._activateHandler = function() {
};

//------------------------------------------------------------------------------------
ConsoleTab.prototype._init = function() {
  if (this.initialized) return;
  
  var html = AppTemplate.ConsoleTab;
  this.$layer = $(this.layername);
  this.$layer.innerHTML = html;
  this.initialized = true;
  
  this.consoleView = this._getConsoleView('console-view');    
  this._renderTab();
};

//------------------------------------------------------------------------------------
ConsoleTab.prototype.setTarget = function(space) {  
  this.consoleView.setTarget(space);
};

//------------------------------------------------------------------------------------
ConsoleTab.prototype.activate = function() {
  this.extTabs.activate(this.consoleTab.id);	
};

//------------------------------------------------------------------------------------
ConsoleTab.prototype.enable = function() {
  if (! this.initialized) this._init();	
  this.extTabs.enableTab(this.consoleTab.id);	
};

//------------------------------------------------------------------------------------
ConsoleTab.prototype.disable = function() {
  this.extTabs.disableTab(this.consoleTab.id);	
};

//------------------------------------------------------------------------------------
ConsoleTab.prototype.refresh = function() {    
  this._renderTab();	
};

//------------------------------------------------------------------------------------
ConsoleTab.prototype.changeApplicationState = function(state) {
  if (! this.consoleView) return;	
  this.consoleView.changeApplicationState(state);
};

//------------------------------------------------------------------------------------
ConsoleTab.prototype._renderTab = function() {
  this.consoleView.show();  
};

//------------------------------------------------------------------------------------
ConsoleTab.prototype._getConsoleView = function(id) {
  var consoleView = new ConsoleView(id);
  return consoleView;
};



