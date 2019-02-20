function Toolbar(id) {    
  this.$id = $(id);
  this.menuElement = Ext.get(id).query('.account-menu');
}

//------------------------------------------------------------------------------------
Toolbar.prototype.setPanel = function(panel) {
  this.panel = panel;	
};

//------------------------------------------------------------------------------------
Toolbar.prototype.setAccount = function(account) {
  this.account = account;
  this._createMenu();
};

//------------------------------------------------------------------------------------
Toolbar.prototype._createMenu = function(account) {
  var accountMenu = new Menu(this.menuElement);
  accountMenu.setTitle(this.account.owner_name);
  
  accountMenu.addItem('logout', Lang.LOGOUT, this._logoutHandler, this);  
};

//------------------------------------------------------------------------------------
Toolbar.prototype._logoutHandler = function() {
  if (this.panel) this.panel.onLogout();
};

