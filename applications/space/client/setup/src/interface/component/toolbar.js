function Toolbar(layername) {
  this.html = AppTemplate.Toolbar;
  this.layername = layername;
  this.$layer = $(layername);
  this.$layer.innerHTML = this.html;
     
  this._init();
}

//------------------------------------------------------------------------------------
Toolbar.prototype._init = function() {  
  var successHandler = function(response, request) {
    if (response.responseText.indexOf("ERR_") != -1) {failureHandler.apply(this, [response, request]); return; }
    var account = Ext.util.JSON.decode(response.responseText);    
    this._createMenu('account-menu', account);                
  };
									 
  var failureHandler = function(response, request) {    
    //Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.Action.DeleteProvider.Failure);
	console.log('error cargando la cuenta');
  };  
		  
  Ext.Ajax.request({
	url: Context.Config.Api,
	method: 'post',
    params : {op: 'loadaccount'},
	success : successHandler.bind(this),
	failure : failureHandler.bind(this),
  });			 	     	  	
};

//------------------------------------------------------------------------------------
Toolbar.prototype._createMenu = function(id, account) {
  var accountMenu = new Menu(id);
  accountMenu.setTitle(account.owner_name);
  //accountMenu.addItem('email', account.owner_email);
  accountMenu.addItem('logout', Lang.Toolbar.Account.logout, this._logoutHandler, this);
};

//------------------------------------------------------------------------------------
Toolbar.prototype._logoutHandler = function() {
  var action = new CGActionLogout;
  action.execute();
};
