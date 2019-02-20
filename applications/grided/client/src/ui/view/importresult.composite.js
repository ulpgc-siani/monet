var ImportResultComposite = EventsSource.extend({
  init : function(element) {
    this._super();
    this.$element = $(element);
    
    this._init();
  }
});
ImportResultComposite.CLICK_CLOSE = 'click_close';

//---------------------------------------------------------------------------
ImportResultComposite.prototype.setResult = function(result) {
  this.result = result;
  if (! this._initialized) this._init();  
};

//---------------------------------------------------------------------------
ImportResultComposite.prototype.show = function() {
  this.$element.show();
};

//---------------------------------------------------------------------------
ImportResultComposite.prototype.hide = function() {
  this.$element.hide();
};

//---------------------------------------------------------------------------
ImportResultComposite.prototype._init = function() {
  this.extParent = Ext.get(this.$element);
  var html = translate(AppTemplate.ImportResultComposite, Lang.ImportResultComposite);
  this.extParent.dom.innerHTML = html;
  
  this.extMessage  = this.extParent.select('.message').first();
  this.extDuration = this.extParent.select('.duration').first();
  this.extCloseButton = Ext.get(this.extParent.query('input[name="close_button"]').first());

  if (! this.result) this.result = {message: '', duration: 0};
  
  this.extMessage.dom.innerHTML = this.result.message;
  this.extDuration.dom.innerHTML = this.result.duration;
      
  this.extCloseButton.on('click', this._closeButtonHandler, this);
};

//---------------------------------------------------------------------------
ImportResultComposite.prototype._closeButtonHandler = function(event, target, options) {
  ev = {name : ImportResultComposite.CLICK_CLOSE , data :{}};  
  this.fire(ev);
};