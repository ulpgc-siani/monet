var UploadDialog = Dialog.extend({
  init: function(element) {
	this._super();
    var html = translate(AppTemplate.UploadDialog, Lang.UploadDialog);
    this.element = element;
    this.$el = $(element);
    this.$el.innerHTML = html;
    this._bind();    	  
  }
});

UploadDialog.CLICK_SUBMIT = 'click-submit';
UploadDialog.CLICK_CLOSE = 'click-close';

//------------------------------------------------------------------------------------------
UploadDialog.prototype._bind = function() {
  this.extParent = Ext.get(this.element);	
  this.extForm = Ext.get(this.extParent.query('form').first());  
  this.extSource = Ext.get(this.extForm.query('input[type="file"]').first());
  this.extSubmitButton = this.extParent.select('.submit').first();
  this.extCloseButton  = this.extParent.select('.close').first();
  this.extFilename = Ext.get(this.extParent.query('div[name="filename"]').first());
 
  this.extSubmitButton.on('click', this._clickSubmitButtonHandler, this);
  this.extCloseButton.on('click', this._clickCloseButtonHandler, this);
  this.extSource.on('change', this._changeSourceHandler, this);
};

//------------------------------------------------------------------------------------------
UploadDialog.prototype._clickSubmitButtonHandler = function(event, target, options) {
  event.stopPropagation();
  event.preventDefault();
  
  if (this.extSource.dom.value == "") {alert(Lang.UploadDialog.no_filename); return; }
  this.extFilename.dom.innerHTML = this.extSource.dom.value;
  
  var event = {name: UploadDialog.CLICK_SUBMIT, data : {form: this.extForm.dom}};
  this.fire(event);
};

//------------------------------------------------------------------------------------------
UploadDialog.prototype._clickCloseButtonHandler = function(event, target, options) {
  event.stopPropagation();
  event.preventDefault();
  this.extFilename.dom.innerHTML = "";
  var event = {name: UploadDialog.CLICK_CLOSE, data : {form: this.extForm.dom}};
  this.fire(event);    
};

//------------------------------------------------------------------------------------------
UploadDialog.prototype._changeSourceHandler = function(event, target, options) {
  this.extFilename.dom.innerHTML = this.extSource.dom.value;  
};