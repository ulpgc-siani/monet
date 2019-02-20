CGEditorDate = function () {
  this.base = CGEditor;
  this.base();
};

CGEditorDate.prototype = new CGEditor;

//private
CGEditorDate.prototype.addBehaviours = function () {
  this.aDialogs[DATE].onSelect = this.atSelect.bind(this);
};

//public
CGEditorDate.prototype.init = function (DOMEditor) {
  this.extEditor = Ext.get(DOMEditor);

  var extDialogDate = this.extEditor.select(CSS_EDITOR_DIALOG_DATE).first();

  this.aDialogs[DATE] = new CGEditorDialogDate(extDialogDate);
  this.setDialogMain(DATE);

  CGEditor.prototype.init.call(this, DOMEditor);

  this.addBehaviours();
};

CGEditorDate.prototype.setConfiguration = function (Config) {
  this.Configuration = Config;
  if (Config.Field) {
    Config.Field.onKeyPress = CGEditorDate.prototype.atFieldKeyPress.bind(this);
    this.Configuration.Field = null;
  }
  for (var iPos in Config.Dialogs) {
    if (isFunction(Config.Dialogs[iPos])) continue;
    var CurrentConfig = Config.Dialogs[iPos];
    if (this.aDialogs[CurrentConfig.sName]) this.aDialogs[CurrentConfig.sName].setConfiguration(CurrentConfig);
  }
};

// #############################################################################################################
CGEditorDate.prototype.atFieldKeyPress = function (sInputValue) {
  this.aDialogs[DATE].setData({code: null, value: sInputValue});
  this.aDialogs[DATE].refresh();
};