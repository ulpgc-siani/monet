var EditorsFactory = new Object();

EditorsFactory.init = function () {
  EditorsFactory.aEditors = new Array();
  EditorDialogListItemTemplate = new Template(Lang.Editor.Templates.DialogListItem);
};

EditorsFactory.register = function (sName, Editor) {
  EditorsFactory.aEditors[sName] = Editor;
};

EditorsFactory.get = function (sName) {
  return EditorsFactory.aEditors[sName];
};