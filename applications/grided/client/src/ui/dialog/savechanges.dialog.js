var SaveChangesDialog = Dialog.extend({
	
	init : function(element) {
	  this._super(element);
	  this.html = translate(AppTemplate.SaveChangesDialog, Lang.SaveChangesDialog);
	  
	  this._init();
	}
});

//--------------------------------------------------------------------------------
SaveChangesDialog.prototype.open = function() {
  this.extDialog.show();
  this.extDialog.focus();
};

//--------------------------------------------------------------------------------
SaveChangesDialog.prototype.close = function() {
  this.extDialog.hide();	
};

//--------------------------------------------------------------------------------
SaveChangesDialog.prototype.addButton = function(buttonName, handler, scope) {
  this.extDialog.addButton(buttonName, handler, scope);	
};
 
//--------------------------------------------------------------------------------
SaveChangesDialog.prototype._init = function() {
  this.extDialog = this._createDialog();
  this.extDialog.header.dom.innerHTML = Lang.SaveChangesDialog.title;
  this.extDialog.body.dom.innerHTML = this.html;
};

//--------------------------------------------------------------------------------
SaveChangesDialog.prototype._createDialog = function() {
  var dialog = new Ext.BasicDialog(this.$el, {
    modal: true,
    shadow: false,
    width: 300,
    height: 160,
    minWidth: 300,
    minHeight: 150,
    minimizable: false,
  });
  return dialog;
};
