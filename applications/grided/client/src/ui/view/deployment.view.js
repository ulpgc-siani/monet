var DeploymentView = View.extend({
  init : function(id) {
    this._super(id);	  
    var template = translate(AppTemplate.DeploymentView, Lang.DeploymentView); 
    this.html = translate(template, Lang.Buttons);

    this.servers = null;
    this.federations = null;
    this.spaces = null;
    this.addFederationDialog = null;
    this.addSpaceDialog = null;
    
    this.initialized = false;       
    this._init();
  }
});

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.setServers = function(servers) {
  this.servers = servers;  
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.setModels = function(models) {
  this.models = models;
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.setFederations = function(federations) {
  this.federations = federations;      
  this._refreshFederationsViewer();  
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.setSpaces = function(spaces) {
  this.spaces = spaces;  
  this._refreshSpacesViewer();  
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.refreshFederation = function(federation) {
	this._refreshFederationsViewer();  
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.refreshFederationSpaces = function() {
  this._refreshSpacesViewer();
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.selectFederation = function(federationId) { 
  this.federationsTable.openRow(federationId);
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.showLoadingSpaces = function() {      	
  var extDocument = Ext.get(document);
  //this.loadingSpacesFlag = true;
  //if (this.loadingSpacesFlag) $(this.extLoadingSpaces.dom).show();
  $(this.extLoadingSpaces.dom).show();
  
  extDocument.on('mousemove', function(mouseEvent) {    
    this.extLoadingSpaces.dom.style.left = mouseEvent.getPageX() + 15 + "px";
    this.extLoadingSpaces.dom.style.top = mouseEvent.getPageY() + "px";
  }, this);
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.hideLoadingSpaces = function() {
  var scope = this;	  
  //this.loadingSpacesFlag = false;
  setTimeout(function(scope) {$(scope.extLoadingSpaces.dom).hide();}, 300, scope);	  
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.showSpacesViewer = function() {
  this.extSpacesViewer.show();    
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.hideSpacesViewer = function() {
  this.extSpacesViewer.hide();  
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.show = function() {
  this.$id.show();
  this.extSpacesViewer.hide();
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.hide = function() {
  this.$id.hide();	
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.showSelectServerDialog = function() {
  if (! this.selectServerDialog) {
    this.selectServerDialog = new SelectServerDialog(this.extSelectServerDialog.dom, this.servers);
    this.selectServerDialog.on(SelectServerDialog.CHANGE_SELECTION_EVENT, {
  	  notify: function(event) {
  	    var dialog = event.data;
  	    var data = dialog.getData(); 
        this.presenter.selectServer(data);    
      }
  	},this);	  
  }
  this.selectServerDialog.open();       
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.showAddFederationDialog = function() {
  if (! this.addFederationDialog) { 
    this.addFederationDialog = new AddFederationDialog(this.extFederationDialog.dom);
        
    this.addFederationDialog.on(AddFederationDialog.ADD_EVENT, {
  	  notify: function(event) {
  	    var dialog = event.data;
  	    var name = dialog.getAddFormData().name; 
  	    var url  = dialog.getAddFormData().url;
        this.presenter.addFederation(name, url);    
      }
  	},this);
  }
  this.addFederationDialog.open(); 
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.hideAddFederationDialog = function() {
  if (! this.addFederationDialog) return;
  this.addFederationDialog.close();
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.clearAddFederationDialog = function() {
  if (! this.addFederationDialog) return;
  this.addFederationDialog.clear();
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.focusAddFederationDialog = function() {
  if (! this.addFederationDialog) return;
  this.addFederationDialog.setFocus();
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.showAddSpaceDialog = function() {
  if (! this.addSpaceDialog) {	  
    this.addSpaceDialog = new AddSpaceDialog(this.extSpaceDialog.dom, this.models);
	
	  this.addSpaceDialog.on(AddSpaceDialog.ADD_EVENT, {
	    notify: function(event) {
	      var dialog = event.data;
	  	  var name    = dialog.getAddFormData().name;	  	
	  	  var url     = dialog.getAddFormData().url;
	  	  var modelId = dialog.getAddFormData().modelId;
	      this.presenter.addSpace(name, url, modelId);    
	    }
	  },this);
  }
  this.addSpaceDialog.open();
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.clearAddSpaceDialog = function() {
  if (! this.addSpaceDialog) return;
  this.addSpaceDialog.clear();
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.focusAddSpaceDialog = function() {
  if (! this.addSpaceDialog) return;
  this.addSpaceDialog.setFocus();
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype.hideAddSpaceDialog = function() {
  if (! this.addSpaceDialog) return;
  this.addSpaceDialog.close();
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._init = function() {
  this.extParent = Ext.get(this.id);  
  this.extParent.dom.innerHTML = this.html;
  
  this.extSelectServerDialog = this.extParent.select('.select-server-dialog').first();
  this.extFederationDialog   = this.extParent.select('.add-federation-dialog').first();
  this.extSpaceDialog        = this.extParent.select('.new-space-dialog').first();
  
  this.extLoadingSpaces = this.extParent.select('.loading-spaces').first();
  
  this._createFederationsViewer();
  this._createSpacesViewer();  
  
  this.hideLoadingSpaces();
  $(this.extLoadingSpaces.dom).hide();
  
  this.hideAddFederationDialog();
  this.hideAddSpaceDialog();
  
  this.initialized = true;
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._createFederationsViewer = function() {
  this.extFederationsViewer = Ext.get('federations-viewer');
   
  var extRemoveButton = Ext.get(this.extFederationsViewer.query('input[name="remove_button"]').first());
   		
  var tableElement = this.extFederationsViewer.select('.table').first();	
  var columns = [Federation.NAME];    
 
  var startAction = {name: Lang.Buttons.start};
  var stopAction  = {name: Lang.Buttons.stop};
  var conditional = {actions:[startAction, stopAction], getIndex: function(row) {
    return (row.state.running)? 1 : 0; 
  }}; 
  
  this.federationsTable = new Table(tableElement.dom, columns, {checkbox: true, clickable: true, conditional : conditional, empty_message: Lang.DeploymentView.no_federations});
    
  extRemoveButton.on('click', this._removeFederationHandler, this);
  
  this.federationsTable.on(Table.CLICK_ROW, {notify: this._openFederationHandler}, this);  
  this.federationsTable.on(Table.CHECK_ROW, {notify : this._refreshFederationsViewerToolbar}, this);
  this.federationsTable.on(Table.CLICK_ROW_ACTION, {notify: this._federationsActionHandler}, this);
  this.federationsTable.on(Table.ROW_MOUSE_OVER, {notify: this._selectFederationHandler}, this);
  //this.federationsTable.on(Table.ROW_MOUSE_OUT, {notify: this._unSelectFederationHandler}, this);
		  		    
  $(this.extFederationsViewer.dom).hide();
  
  this.federationsToolbar = {
    removeButton : extRemoveButton.dom
  };
  
  this._refreshFederationsViewerToolbar();
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._refreshFederationsViewer = function() {
  this.federationsTable.setData(this.federations);  
  $(this.extFederationsViewer.dom).show();
  this._refreshFederationsViewerToolbar();
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._refreshFederationsViewerToolbar = function() {  	
  var button = this.federationsToolbar.removeButton;
  var rows = this.federationsTable.getSelectedRows();
  if (rows.length > 0) $(button).show(); else $(button).hide();    
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._addFederationHandler = function(event, target, options) {
  this.presenter.addFederation();
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._removeFederationHandler = function(event, target, options) {
  var rows = this.federationsTable.getSelectedRows();
  var ids = [];
  $(rows).each(function(row) { ids.push(row.id); });
  this.presenter.removeFederations(ids);
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._selectFederationHandler = function(event) {  
  var federationId = event.data.row.id;	
  this.presenter.selectFederation(federationId);
  this.federationsTable.openRow(federationId);
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._unSelectFederationHandler = function(event) { 
  this.presenter.unSelectFederation();	
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._openFederationHandler = function(event) {
  var federationId = event.data.row.id;
  this.presenter.openFederation(federationId);	
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._federationsActionHandler = function(event) {  	
  switch (event.data.action) {
    case Lang.Buttons.start: this.presenter.startFederation(event.data.row.id); break;
    case Lang.Buttons.stop: this.presenter.stopFederation(event.data.row.id); break;
  }
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._spacesActionHandler = function(event) {    
  switch (event.data.action) {
    case Lang.Buttons.start: this.presenter.startSpace(event.data.row.id); break;
    case Lang.Buttons.stop: this.presenter.stopSpace(event.data.row.id); break;
  }
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._createSpacesViewer = function() {	
  this.extSpacesViewer = Ext.get('spaces-viewer');
    
  var extRemoveButton = Ext.get(this.extSpacesViewer.query('input[name="remove_button"]').first());		     	  

  var tableElement = this.extSpacesViewer.select('.table').first();	  
  var columns = [Space.NAME];  
  
  var startAction = {name : Lang.Buttons.start};
  var stopAction  = {name: Lang.Buttons.stop};
  var conditional = {actions:[startAction, stopAction], getIndex: function(row) {
    return (row.state.running)? 1 : 0; 
  }}; 
  
  this.spacesTable = new Table(tableElement.dom, columns, {checkbox: true, clickable: true, conditional : conditional, empty_message: Lang.DeploymentView.no_spaces});  
  
  extRemoveButton.on('click', this._removeSpaceHandler, this);
  
  this.spacesTable.on(Table.CLICK_ROW, {notify: this._openSpaceHandler}, this);    
  this.spacesTable.on(Table.CHECK_ROW, {notify: this._refreshSpacesViewerToolbar}, this);
  this.spacesTable.on(Table.MOUSE_ENTER, {notify: this._enterSpacesHandler}, this);
  this.spacesTable.on(Table.CLICK_ROW_ACTION, {notify: this._spacesActionHandler}, this);
 // this.spacesTable.on(Table.MOUSE_LEAVE, {notify: this._leaveSpacesHandler}, this);

  this.spacesToolbar = {
    removeButton : extRemoveButton.dom
  };
  
  this.extSpacesViewer.hide();
  this._refreshSpacesViewerToolbar();
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._enterSpacesHandler = function(event, target, options) {
  this.presenter.onOverSpacesViewer();	  
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._leaveSpacesHandler = function(event, target, options) {
  this.presenter.onOutSpacesViewer();	   
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._refreshSpacesViewer = function() {	
  this.spacesTable.setData(this.spaces);
  this.extSpacesViewer.show();
  this._refreshSpacesViewerToolbar();
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._refreshSpacesViewerToolbar = function() {  	
  var button = this.spacesToolbar.removeButton;
  var rows = this.spacesTable.getSelectedRows();
  if (rows.length > 0) $(button).show(); else $(button).hide();    
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._removeSpaceHandler = function(event, target, options) {
  var rows = this.spacesTable.getSelectedRows();
  var ids = [];
  $(rows).each(function(row) { ids.push(row.id); });
  this.presenter.removeSpaces(ids);
};

//-----------------------------------------------------------------------------------------------------------
DeploymentView.prototype._openSpaceHandler = function(event) {
  var spaceId = event.data.row.id;
  this.presenter.openSpace(spaceId);	
};

