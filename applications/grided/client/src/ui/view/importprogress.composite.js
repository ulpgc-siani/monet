var ImportProgressComposite = EventsSource.extend({  
  init : function(element) {
    this._super();
    this.$element = $(element);
    
    this._init();
  }
});
ImportProgressComposite.CLICK_STOP = 'click_stop';

//---------------------------------------------------------------------------
ImportProgressComposite.prototype.show = function() {
  this.$element.show();
};

//---------------------------------------------------------------------------
ImportProgressComposite.prototype.hide = function() {
  this.$element.hide();
};

//---------------------------------------------------------------------------
ImportProgressComposite.prototype.setProgress = function(progress) {
  this.progress = progress;  
  this._refresh();
};

//---------------------------------------------------------------------------
ImportProgressComposite.prototype._init = function() {
  this.extParent = Ext.get(this.$element);
  var template = translate(AppTemplate.ImportProgressComposite, Lang.ImportProgressComposite);
  if (! this.progress) this.progress = {value : 0, estimatedTime : 0};
  var html = _.template(template, {progress : this.progress});
  
  this.extParent.dom.innerHTML = html;
  this.extProgress      = this.extParent.select('.progress').first();
  this.extEstimatedTime = this.extParent.select('.estimated_time').first();        
  this.extStopButton    = Ext.get(this.extParent.query('input[name="stop_button"]').first());
  
  this.extStopButton.on('click', this._stopButtonHandler, this);
};

//---------------------------------------------------------------------------
ImportProgressComposite.prototype._refresh = function() {
  if (! this.progress) return;
  this.extProgress.dom.innerHTML = this.progress.value;
  this.extEstimatedTime.dom.innerHTML = this.progress.estimatedTime;  
};

//---------------------------------------------------------------------------
ImportProgressComposite.prototype._stopButtonHandler = function(event, target, options) {
  ev = {name : ImportProgressComposite.CLICK_STOP , data : {progress : this.progress}};
  this.fire(ev);
};