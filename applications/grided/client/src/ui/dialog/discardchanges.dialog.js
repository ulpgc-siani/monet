var DiscardChangesDialog = Dialog.extend({
  init : function(element) {
    this._super(element);
    this.html = AppTemplate.DiscardChangesDialog;
    
    this._init();
  }	
});

//--------------------------------------------------------------------------------
DiscardChangesDialog.prototype.open = function() {
  this.extDialog.show();
  this.extDialog.focus();
};

//--------------------------------------------------------------------------------
DiscardChangesDialog.prototype.close = function() {
  this.extDialog.hide();	
};

//--------------------------------------------------------------------------------
DiscardChangesDialog.prototype.addButton = function(buttonName, handler, scope) {
  this.extDialog.addButton(buttonName, handler, scope);	
};

//--------------------------------------------------------------------------------
DiscardChangesDialog.prototype._init = function() {
  this.extDialog = this._createDialog();
  this.extDialog.header.dom.innerHTML = "titulo";
  this.extDialog.body.dom.innerHTML = this.html;
};

//--------------------------------------------------------------------------------
DiscardChangesDialog.prototype._createDialog = function() {
  var dialog = new Ext.BasicDialog(this.$el, {
    modal: true,
    shadow: false,
    width: 400,
    height: 190,
    minWidth: 400,
    minHeight: 180,
    minimizable: false,
  });
    
  return dialog;
};
