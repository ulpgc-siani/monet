var UploadModelVersionDialog = Dialog.extend({
  init : function(element) {
    this._super(element);
    this.id = this.$el.getAttribute('id');
    
    this._init();
  }    
});

//-----------------------------------------------------
UploadModelVersionDialog.prototype.getData = function() {
   return {
     form: this.extForm.dom,
   };
};

//-----------------------------------------------------
UploadModelVersionDialog.prototype.open = function() {
  this.extDialog.show();  
};

//-----------------------------------------------------
UploadModelVersionDialog.prototype.close = function() {
  this.extDialog.hide();  
};

//----------------------------------------------------
UploadModelVersionDialog.prototype.addButton = function(buttonName, handler, scope) {
  this.extDialog.addButton(buttonName, handler, scope);     
};

//-----------------------------------------------------
UploadModelVersionDialog.prototype._init = function() {
  this.extDialog = this._createDialog();
  this.extDialog.setTitle(Lang.UploadModelVersionDialog.title);
  
  var html  = translate(AppTemplate.UploadModelVersionDialog, Lang.Buttons);
  this.extDialog.body.dom.innerHTML  = translate(html, Lang.UploadModelVersionDialog);
  
  this.extParent     = Ext.get(this.id);
  this.extMessage    = Ext.get(this.extParent.select(".message").first());
  this.extForm       = Ext.get(this.extParent.query("form").first());
  this.extFile       = Ext.get(this.extParent.query("input[type='file']").first());
  this.extFilename   = Ext.get(this.extParent.query('div[name="filename"]').first());
    
  this.extFile.on('change', this._changeFileHandler, this);
};

//-----------------------------------------------------
UploadModelVersionDialog.prototype._changeFileHandler = function(event, target, options) {
  this.extFilename.dom.innerHTML = this.extFile.dom.value;
};

//-----------------------------------------------------
UploadModelVersionDialog.prototype._createDialog = function(event, target, options) {  
  var dialog = new Ext.BasicDialog(this.id, {
    modal: true,
    shadow: false,
    width: 435,
    height: 236,
    minWidth: 430,
    minHeight: 230,
    minimizable: false
  });

  return dialog;  
};