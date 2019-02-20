var ModelsView = View.extend({	
  init : function(id) {
    this._super(id);
    this.html = translate(AppTemplate.ModelsView, Lang.Buttons);
    this.html = translate(this.html, Lang.ModelsView);
    this.models = [];
    this.presenter = null;
    
    this.initialized = false;
  }
});

//-----------------------------------------------------------------------------------------------------------
ModelsView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};

//-----------------------------------------------------------------------------------------------------------
ModelsView.prototype.setModels = function(models) {
  this.models = models;  
  if (!this.initialized) this._init();  
  this._refresh();
};

//-----------------------------------------------------------------------------------------------------------
ModelsView.prototype.show = function() {
  this.showAddModelDialog();
  this.$id.show();
};

//-----------------------------------------------------------------------------------------------------------
ModelsView.prototype.hide = function() {
  this.hideAddModelDialog();
  this.$id.hide();
};

//-----------------------------------------------------------------------------------------------------------
ModelsView.prototype.showAddModelDialog = function() {
  if (! this.addModelDialog) {
    this.addModelDialog = new AddModelDialog(this.extAddModelDialog.dom);
    
    this.addModelDialog.on(AddModelDialog.ADD_EVENT, {
    	notify : function(event) {
    	  var dialog = event.data;    	  
    	  var data = dialog.getData();
    	  this.presenter.addModel({form: data.form, name: data.name});    	  
    	}
    }, this);
  }
  this.addModelDialog.open();
};

//-----------------------------------------------------------------------------------------------------------
ModelsView.prototype.hideAddModelDialog = function() {
  if (! this.addModelDialog) return;  
  this.addModelDialog.close();
};

//-----------------------------------------------------------------------------------------------------------
ModelsView.prototype.clearAddModelDialog = function() {
  if (this.addModelDialog) this.addModelDialog.clear();
};

//-----------------------------------------------------------------------------------------------------------
ModelsView.prototype.focusAddModelDialog = function() {
  if (this.addModelDialog) this.addModelDialog.setFocus();
};

//-----------------------------------------------------------------------------------------------------------
ModelsView.prototype._refresh = function() {
  this.table.setData(this.models); 
  this._refreshToolbar();
};

//-----------------------------------------------------------------------------------------------------------
ModelsView.prototype._init = function(event) {
  this.extParent = Ext.get(this.id);    
  this.extParent.dom.innerHTML = this.html;
  
  this.extRemoveButton = Ext.get(this.extParent.query('input[name="remove_button"]').first());
  this.extAddModelDialog = Ext.get(Ids.Elements.ADD_MODEL_DIALOG);
	  
  var columns = [BusinessModel.NAME];  
  var element = this.extParent.query('.table').first();
	   
  this.table = new Table(element, columns, {checkbox: true, clickable: true, empty_message: Lang.ModelsView.no_models});  
  this.table.setData(this.models);
  
  $(this.extRemoveButton.dom).hide();
  
  this._bind();
  
  this.initialized = true;
};

//-----------------------------------------------------------------------------------------------------------
ModelsView.prototype._bind = function(extParent) {        
  this.extRemoveButton.on('click', this._removeHandler, this);
   
  this.table.on(Table.CLICK_ROW, {notify: this._openHandler}, this);
  this.table.on(Table.CHECK_ROW, {notify: this._checkHandler}, this);   
};

//-----------------------------------------------------------------------------------------------------------
ModelsView.prototype._removeHandler = function(event, target, options) {  
  var rows = this.table.getSelectedRows();
  var ids = [];
  $(rows).each(function(row) { ids.push(row.id); });
  this.presenter.removeModels(ids);
};

//-----------------------------------------------------------------------------------------------------------
ModelsView.prototype._openHandler = function(event) {  
  var row = event.data.row;		
  this.presenter.openModel(row.id);
};

//-----------------------------------------------------------------------------------------------------------
ModelsView.prototype._checkHandler = function(event) {
  this._refreshToolbar();
};

//-----------------------------------------------------------------------------------------------------------
ModelsView.prototype._refresh = function() {
  this.table.setData(this.models); 
  this._refreshToolbar();
};

//-----------------------------------------------------------------------------------------------------------
ModelsView.prototype._refreshToolbar = function(event) {
  var rows = this.table.getSelectedRows();
  if (rows.length > 0) 
    $(this.extRemoveButton.dom).show();
  else
    $(this.extRemoveButton.dom).hide();	
};