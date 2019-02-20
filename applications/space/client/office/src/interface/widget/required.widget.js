CGWidgetRequired = function (extWidgetParent) {
  this.base = CGWidget;
  this.extWidgetParent = extWidgetParent;
  this.extWidget = null;
};

CGWidgetRequired.prototype = new CGWidget;

CGWidgetRequired.prototype.init = function () {
  var extLabel, extParent, extElement;
  var TemplateRequired = new Template(Lang.Widget.Templates.Required);

  extElement = this.extWidgetParent.up(CSS_FIELD);
  if (extElement == null) extElement = this.extWidgetParent.up(CSS_FIELD_COMPOSITE);

  extLabel = extElement.down(HTML_LABEL);

  new Insertion.Bottom(extLabel.dom, TemplateRequired.evaluate({'ImagesPath': Context.Config.ImagesPath, 'messageWhenRequired': this.Target.getMessageWhenRequired()}));
  this.extImg = extLabel.select("img").first();
  this.extSpan = extLabel.select("span").first();

  this.bIsReady = true;
};

CGWidgetRequired.prototype.setTarget = function (Target) {
  this.Target = Target;
  this.setMessageWhenEmpty(this.Target.getMessageWhenEmpty());
  this.validate();
  //this.updateData();
};

CGWidgetRequired.prototype.focus = function () {
  this.extWidgetParent.focus();
};

CGWidgetRequired.prototype.blur = function () {
  this.extWidgetParent.blur();
};

CGWidgetRequired.prototype.show = function () {
  this.extImg.show();
};

CGWidgetRequired.prototype.hide = function () {
  this.extImg.hide();
};