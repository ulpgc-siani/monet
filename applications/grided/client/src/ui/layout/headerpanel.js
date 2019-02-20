function HeaderPanel(element) {    
  this.extElement = Ext.get(element);
  this.toolbar = null;
  this.extNavigationBar = null;
  
  this._init();  
};

//-----------------------------------------------------------------------------
HeaderPanel.prototype._init = function() {	
  this.toolbar = new Toolbar(Ext.get(Ids.Elements.APP_TOOLBAR));
  this.toolbar.setPanel(this);
  this.service = GridedService;
  this._loadAccount();
	
  this._bind();
};

//-----------------------------------------------------------------------------
HeaderPanel.prototype._bind = function() {  
  this.extNavigationBar = Ext.get('navigation-bar'); 
  this.extNavigationBar.on('click', this._clickItemHandler, this);   
};

//-----------------------------------------------------------------------------
HeaderPanel.prototype._clickItemHandler = function(event, target, options) {
  event.stopPropagation();
  event.preventDefault();
	  
  var name = target.getAttribute('name');
	  
  switch (name) {
    case Lang.Desktop.home:
      var event = {name: Events.OPEN_HOME, token: new HomePlace().toString(), data:{} };
      EventBus.fire(event);	      
    break;
    case Lang.Desktop.servers:      	
      var event = {name: Events.OPEN_SERVERS, token: new ServersPlace().toString(), data:{} };      
      EventBus.fire(event);
	  break;
    case Lang.Desktop.models:   	     
   	  var event = {name: Events.OPEN_MODELS, token: new ModelsPlace().toString(), data:{} };
      EventBus.fire(event);      
	  break;
	case Lang.Desktop.deployment:
	  var event = {name: Events.OPEN_DEPLOYMENT, token: new DeploymentPlace().toString(), data:{} };	  
	  EventBus.fire(event);
	  break;	
  }
};

//-----------------------------------------------------------------------------
HeaderPanel.prototype.selectItem = function(name) {
  var currentItem = this.extNavigationBar.query('a[name="' + name + '"]').first();	
  var items       = this.extNavigationBar.query('a');
  
  items.each(function(item) {
	$(item).removeClassName('selected');
  });
      
  $(currentItem).addClassName('selected');
};

//-----------------------------------------------------------------------------
HeaderPanel.prototype._loadAccount = function() {	
  this.service.loadAccount({
	context : this,
	success: function(account) {
	  this.toolbar.setAccount(account);
	},
	failure: function(ex) {
	  throw ex;
	}
  });
};

//-----------------------------------------------------------------------------
HeaderPanel.prototype.onLogout = function() {
  this.service.logout({
    context: this,
    success: function() {console.log("logout");},
    failure: function() {console.log("imposible hacer el logout");}
  });	
};
