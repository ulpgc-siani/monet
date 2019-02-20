CGEditorBoolean = function () {
  this.base = CGEditor;
  this.base();
};

CGEditorBoolean.prototype = new CGEditor;

//private
CGEditorBoolean.prototype.addBehaviours = function () {
  this.aDialogs[SOURCE_STORE].onSelect = this.atSelect.bind(this);
};

//public
CGEditorBoolean.prototype.init = function (DOMEditor) {
  this.extEditor = Ext.get(DOMEditor);

  var extDialogSourceStore = this.extEditor.select(CSS_EDITOR_DIALOG_SOURCE_STORE).first();

  this.aDialogs[SOURCE_STORE] = new CGEditorDialogGrid(extDialogSourceStore);
  this.aDialogs[SOURCE_STORE].setRenderFooter(false);
  this.setDialogMain(SOURCE_STORE);

  CGEditor.prototype.init.call(this, DOMEditor);

  this.addBehaviours();
};

CGEditorBoolean.prototype.refresh = function () {
  this.aDialogs[SOURCE_STORE].refresh();
};

// #############################################################################################################