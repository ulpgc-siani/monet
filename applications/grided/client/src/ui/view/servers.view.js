
var ServersView = View.extend({
  init : function(id) {
    this._super(id);
    this.html = translate(AppTemplate.ServersView, Lang.Buttons);
    this.html = translate(this.html, Lang.ServersView);
    this.servers = [];
    this.presenter = null;
    
    this.initialized = false;
  }
});

//-----------------------------------------------------------------------------------------------------------
ServersView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};

//-----------------------------------------------------------------------------------------------------------
ServersView.prototype.setServers = function(servers) {
  this.servers = servers;  
  if (!this.initialized) this._init();
  
  this._refresh();
};

//-----------------------------------------------------------------------------------------------------------
ServersView.prototype.showAddServerDialog = function() {
  if (! this.addServerDialog) {
    this.addServerDialog = new AddServerDialog(this.extAddServerDialog.dom);
    this.addServerDialog.on(AddServerDialog.CLICK_ADD_EVENT, {
    	notify : function(event)  {    	  	
    	  var dialog = event.data;
    	  if (! dialog.check()) { dialog.showError(); return; }
    	  var data = dialog.getData();
    	  this.presenter.addServer({name: data.name, ip: data.ip});
    	}
    }, this);
  }
  this.addServerDialog.open();  
};

//-----------------------------------------------------------------------------------------------------------
ServersView.prototype.show = function() {
  this.showAddServerDialog();
  this.$id.show();
};

//-----------------------------------------------------------------------------------------------------------
ServersView.prototype.hide = function() {
  this.hideAddServerDialog();
  this.$id.hide();
};

//-----------------------------------------------------------------------------------------------------------
ServersView.prototype.hideAddServerDialog = function() {
  if (! this.addServerDialog) return;  
  this.addServerDialog.close();
};

//-----------------------------------------------------------------------------------------------------------
ServersView.prototype.clearAddServerDialog = function() {
  if (this.addServerDialog) this.addServerDialog.clear();	
};

//-----------------------------------------------------------------------------------------------------------
ServersView.prototype._refresh = function() {
  this.table.setData(this.servers); 
  this._refreshToolbar();
};

//-----------------------------------------------------------------------------------------------------------
ServersView.prototype._init = function(event) {
  this.extParent = Ext.get(this.id);    
  this.extParent.dom.innerHTML = this.html;
  
  this.extRemoveButton = Ext.get(this.extParent.query('input[name="remove_button"]').first());
  this.extAddServerDialog = Ext.get(Ids.Elements.ADD_SERVER_DIALOG);
	  
  var columns = [Server.NAME];  
  var element = this.extParent.query('.table').first();
	   
  this.table = new Table(element, columns, {checkbox: true, clickable: true, empty_message: Lang.ServersView.no_servers});  
  this.table.setData(this.servers);
  
  $(this.extRemoveButton.dom).hide();
  
  this._bind();
  
  this.initialized = true;
};

//-----------------------------------------------------------------------------------------------------------
ServersView.prototype._bind = function(extParent) {        
   this.extRemoveButton.on('click', this._removeHandler, this);
   
   this.table.on(Table.CLICK_ROW, {notify: this._openHandler}, this);
   this.table.on(Table.CHECK_ROW, {notify: this._checkHandler}, this);   
};

//-----------------------------------------------------------------------------------------------------------
ServersView.prototype._removeHandler = function(event, target, options) {  
  var rows = this.table.getSelectedRows();
  var ids = [];
  $(rows).each(function(row) { ids.push(row.id); });
  this.presenter.removeServers(ids);
};

//-----------------------------------------------------------------------------------------------------------
ServersView.prototype._openHandler = function(event) {  
  var row = event.data.row;		
  this.presenter.openServer(row.id);
};

//-----------------------------------------------------------------------------------------------------------
ServersView.prototype._checkHandler = function(event) {
  this._refreshToolbar();
};

//-----------------------------------------------------------------------------------------------------------
ServersView.prototype._refreshToolbar = function(event) {
  var rows = this.table.getSelectedRows();
  if (rows.length > 0) 
    $(this.extRemoveButton.dom).show();
  else
    $(this.extRemoveButton.dom).hide();	
};
