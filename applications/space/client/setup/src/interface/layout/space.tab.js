function SpaceTab(extTabs, layername) {
  this.extTabs = extTabs;
  this.layername = layername;
  this.spaceTab = this.extTabs.addTab(this.layername, Lang.BottomLayout.Space);  
  this.spaceTab.on('activate', this._activateHandler, this);
  this.initialized = false;
  
  this.handlers = {
    'change-tab' : {fn: null, scope: null}	  
  };		 
}

//------------------------------------------------------------------------------------
SpaceTab.prototype._activateHandler = function() {
};

//------------------------------------------------------------------------------------
SpaceTab.prototype._init = function() {
  if (this.initialized) return;
  
  var html = AppTemplate.SpaceTab;
  this.$layer = $(this.layername);
  this.$layer.innerHTML = html;
  this.initialized = true;
		  
  this.spaceView = this._getSpaceView();
  this.updateSpaceDialog = this._createUpdateSpaceDialog();
  this.updateModelDialog = this._createUpdateModelDialog();
  this.space = null;
    
  this._renderTab();
};

//------------------------------------------------------------------------------------
SpaceTab.prototype.setTarget = function(space) {
  this.space = space;
  this.spaceView.setTarget(space);
};

//------------------------------------------------------------------------------------
SpaceTab.prototype.activate = function() {
  this.extTabs.activate(this.spaceTab.id);
};

//------------------------------------------------------------------------------------
SpaceTab.prototype.enable = function() {
  if (! this.initialized) this._init();	
  this.extTabs.enableTab(this.spaceTab.id);
};

//------------------------------------------------------------------------------------
SpaceTab.prototype.disable = function() {
  this.extTabs.disableTab(this.spaceTab.id);
};

//------------------------------------------------------------------------------------
SpaceTab.prototype.on = function(eventName, handler, scope) {
  this.handlers[eventName] = {fn: handler, scope: scope};
};

//------------------------------------------------------------------------------------
SpaceTab.prototype.un = function(eventName) {
  this.handlers[eventName] = null;
};

//------------------------------------------------------------------------------------
SpaceTab.prototype.refresh = function() {
  this._renderTab();	
};

//------------------------------------------------------------------------------------
SpaceTab.prototype.changeApplicationState = function(state) {
  if (! this.spaceView) return;	
  if (this.spaceView) this.spaceView.changeApplicationState(state);  
};

//------------------------------------------------------------------------------------
SpaceTab.prototype._renderTab = function() {
  this.updateSpaceDialog.hide();	
  this.spaceView.show();
};

//------------------------------------------------------------------------------------
SpaceTab.prototype._getSpaceView = function() {
  var view = ViewBusinessSpace;	
  view.init(Literals.Views.BusinessSpace);
  view.on('update-space', this._updateSpaceHandler, this);
  view.on('update-model', this._updateModelHandler, this);  
  view.on('show-model', this._showModelHandler, this);
  return view;
};

//------------------------------------------------------------------------------------
SpaceTab.prototype._updateSpaceHandler = function() {
  this.spaceView.hide();  
  this.updateSpaceDialog.show();
};

//------------------------------------------------------------------------------------
SpaceTab.prototype._updateModelHandler = function() {
  this.spaceView.hide();
  this.updateModelDialog.show();
 };
 
//------------------------------------------------------------------------------------
 SpaceTab.prototype._showModelHandler = function() {
   var handler = this.handlers['change-tab'];
   if (!handler) return;
   handler.fn.apply(handler.scope, [Literals.Tabs.Model]);
 };

//------------------------------------------------------------------------------------
SpaceTab.prototype._createUpdateSpaceDialog = function() {
  var dialog = new UpdateBusinessSpaceDialog('dialog-update-space');
  dialog.on('back', this._backUpdateSpaceHandler, this);
  dialog.on('result', this._resultUpdateSpaceHandler, this);    
  dialog.hide();
  return dialog;
};

//------------------------------------------------------------------------------------
SpaceTab.prototype._backUpdateSpaceHandler = function() {
  this.updateSpaceDialog.hide();
  this.spaceView.show();
};

//------------------------------------------------------------------------------------
SpaceTab.prototype._resultUpdateSpaceHandler = function() { 	
  if (!this.updateSpaceDialog.check()) return;
	  
  var updateHandler = function(btn, text) {	  	
  confirmDialog.hide();
  switch (btn) {
    case 'yes':
      this.updateSpaceDialog.submit();
      this.updateSpaceDialog.hide();
      this.spaceView.show();
      break;
    }
  };
	  
  var confirmDialog = new DialogConfirm();       
  confirmDialog.setTitle(Lang.DialogUpdateBusinessSpace.ConfirmDialog.title);
  confirmDialog.setMessage(Lang.DialogUpdateBusinessSpace.ConfirmDialog.message); 
  confirmDialog.setResultHandler(updateHandler, this);      
  confirmDialog.show();         
};

//------------------------------------------------------------------------------------
SpaceTab.prototype._createUpdateModelDialog = function(mode) {
  var dialog = new UpdateBusinessModelDialog('dialog-update-model');
  dialog.on('back', this._backUpdateModelHandler, this);
  dialog.on('result', this._resultUpdateModelHandler, this);  
  dialog.hide();
  return dialog;
};

//------------------------------------------------------------------------------------
SpaceTab.prototype._backUpdateModelHandler = function() {
  this.updateModelDialog.hide();	
  this.spaceView.show();
};

//------------------------------------------------------------------------------------
SpaceTab.prototype._resultUpdateModelHandler = function() { 	
  if (!this.updateModelDialog.check()) return;
	  
  var updateHandler = function(btn, text) {	  	
  confirmDialog.hide();
  switch (btn) {
    case 'yes':
      this.updateModelDialog.submit();
      this.updateModelDialog.hide();
      this.spaceView.show();
      break;
    }
  };
	  
  var confirmDialog = new DialogConfirm();       
  confirmDialog.setTitle(Lang.DialogUpdateBusinessModel.ConfirmDialog.title);
  confirmDialog.setMessage(Lang.DialogUpdateBusinessModel.ConfirmDialog.message); 
  confirmDialog.setResultHandler(updateHandler, this);      
  confirmDialog.show();           
};


