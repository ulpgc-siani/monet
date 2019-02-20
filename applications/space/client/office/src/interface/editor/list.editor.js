CGEditorList = function () {
  this.base = CGEditor;
  this.base();
};

CGEditorList.prototype = new CGEditor;

//private
CGEditorList.prototype.addBehaviours = function () {
  this.aDialogs[LIST].onSelect = this.atSelect.bind(this);
};

//public
CGEditorList.prototype.init = function (DOMEditor) {
  this.extEditor = Ext.get(DOMEditor);

  var DOMGrid;
  var extDialogList = this.extEditor.select(CSS_EDITOR_DIALOG_LIST).first();

  this.aDialogs[LIST] = new CGEditorDialogList(extDialogList);
  this.setDialogMain(LIST);

  CGEditor.prototype.init.call(this, DOMEditor);

  this.addBehaviours();
};

// #############################################################################################################