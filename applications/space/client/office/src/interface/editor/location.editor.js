CGEditorLocation = function () {
  this.base = CGEditor;
  this.base();
};

CGEditorLocation.prototype = new CGEditor;

//private
CGEditorLocation.prototype.addBehaviours = function () {
  this.aDialogs[LOCATION].onSelect = this.atSelect.bind(this);
};

//public
CGEditorLocation.prototype.init = function (DOMEditor) {
  this.extEditor = Ext.get(DOMEditor);

  var extDialogLocation = this.extEditor.select(CSS_EDITOR_DIALOG_LOCATION).first();

  this.aDialogs[LOCATION] = new CGEditorDialogLocation(extDialogLocation);
  this.setDialogMain(LOCATION);

  CGEditor.prototype.init.call(this, DOMEditor);

  this.addBehaviours();
};

CGEditorLocation.prototype.setConfiguration = function (Config) {
  this.Configuration = Config;
  if (Config.Field) {
    this.Configuration.Field = null;
  }
  for (var iPos in Config.Dialogs) {
    if (isFunction(Config.Dialogs[iPos])) continue;
    var CurrentConfig = Config.Dialogs[iPos];
    if (this.aDialogs[CurrentConfig.sName]) this.aDialogs[CurrentConfig.sName].setConfiguration(CurrentConfig);
  }
};