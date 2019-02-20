ViewerHelperEditors = new Object();

ViewerHelperEditors.extLayer = null;
ViewerHelperEditors.CurrentEditor = null;
ViewerHelperEditors.Target = null;

ViewerHelperEditors.init = function (extLayer) {
  ViewerHelperEditors.extLayer = extLayer;

  ViewerHelperEditors.extLayer.dom.innerHTML = translate(AppTemplate.ViewerHelperEditors, Lang.ViewerHelperEditors);
  ViewerHelperEditors.CurrentEditor = null;

  CommandListener.capture(ViewerHelperEditors.extLayer.dom);

  ViewerHelperEditors.initEditors();
  ViewerHelperEditors.hideAllEditors();
};

ViewerHelperEditors.registerEvents = function (Editor) {
  Editor.onShow = ViewerHelperEditors.atEditorShow.bind(ViewerHelperEditors);
  Editor.onHide = ViewerHelperEditors.atEditorHide.bind(ViewerHelperEditors);
  Editor.onLock = ViewerHelperEditors.atEditorLock.bind(ViewerHelperEditors);
  Editor.onUnLock = ViewerHelperEditors.atEditorUnLock.bind(ViewerHelperEditors);
};

ViewerHelperEditors.initEditors = function () {
  var sDisplay = this.extLayer.dom.style.display;
  var zIndex = this.extLayer.dom.style.zIndex;

  this.extLayer.dom.style.zIndex = -1000000;
  this.extLayer.dom.style.display = "block";

  EditorsFactory.init();

  var EditorBoolean = new CGEditorBoolean();
  ViewerHelperEditors.registerEvents(EditorBoolean);
  EditorBoolean.init(ViewerHelperEditors.getLayer(FIELD_TYPE_BOOLEAN));
  EditorsFactory.register(FIELD_TYPE_BOOLEAN, EditorBoolean);

  var EditorCheck = new CGEditorCheck();
  ViewerHelperEditors.registerEvents(EditorCheck);
  EditorCheck.init(ViewerHelperEditors.getLayer(FIELD_TYPE_CHECK));
  EditorsFactory.register(FIELD_TYPE_CHECK, EditorCheck);

  var EditorDate = new CGEditorDate();
  ViewerHelperEditors.registerEvents(EditorDate);
  EditorDate.init(ViewerHelperEditors.getLayer(FIELD_TYPE_DATE));
  EditorsFactory.register(FIELD_TYPE_DATE, EditorDate);

  var EditorFile = new CGEditorFile();
  ViewerHelperEditors.registerEvents(EditorFile);
  EditorFile.init(ViewerHelperEditors.getLayer(FIELD_TYPE_FILE));
  EditorsFactory.register(FIELD_TYPE_FILE, EditorFile);

  var EditorLink = new CGEditorLink();
  ViewerHelperEditors.registerEvents(EditorLink);
  EditorLink.init(ViewerHelperEditors.getLayer(FIELD_TYPE_LINK));
  EditorsFactory.register(FIELD_TYPE_LINK, EditorLink);

  var EditorList = new CGEditorList();
  ViewerHelperEditors.registerEvents(EditorList);
  EditorList.init(ViewerHelperEditors.getLayer(FIELD_TYPE_LIST));
  EditorsFactory.register(FIELD_TYPE_LIST, EditorList);

  var EditorNumber = new CGEditorNumber();
  ViewerHelperEditors.registerEvents(EditorNumber);
  EditorNumber.init(ViewerHelperEditors.getLayer(FIELD_TYPE_NUMBER));
  EditorsFactory.register(FIELD_TYPE_NUMBER, EditorNumber);

  var EditorPicture = new CGEditorPicture();
  ViewerHelperEditors.registerEvents(EditorPicture);
  EditorPicture.init(ViewerHelperEditors.getLayer(FIELD_TYPE_PICTURE));
  EditorsFactory.register(FIELD_TYPE_PICTURE, EditorPicture);

  var EditorComposite = new CGEditorComposite();
  ViewerHelperEditors.registerEvents(EditorComposite);
  EditorComposite.init(ViewerHelperEditors.getLayer(FIELD_TYPE_COMPOSITE));
  EditorsFactory.register(FIELD_TYPE_COMPOSITE, EditorComposite);

  var EditorSelect = new CGEditorSelect();
  ViewerHelperEditors.registerEvents(EditorSelect);
  EditorSelect.init(ViewerHelperEditors.getLayer(FIELD_TYPE_SELECT));
  EditorsFactory.register(FIELD_TYPE_SELECT, EditorSelect);

  var EditorText = new CGEditorText();
  ViewerHelperEditors.registerEvents(EditorText);
  EditorText.init(ViewerHelperEditors.getLayer(FIELD_TYPE_TEXT));
  EditorsFactory.register(FIELD_TYPE_TEXT, EditorText);

  var EditorNode = new CGEditorNode();
  ViewerHelperEditors.registerEvents(EditorNode);
  EditorNode.init(ViewerHelperEditors.getLayer(FIELD_TYPE_NODE));
  EditorsFactory.register(FIELD_TYPE_NODE, EditorNode);

  var EditorSerial = new CGEditorSerial();
  ViewerHelperEditors.registerEvents(EditorSerial);
  EditorSerial.init(ViewerHelperEditors.getLayer(FIELD_TYPE_SERIAL));
  EditorsFactory.register(FIELD_TYPE_SERIAL, EditorSerial);

  var EditorLocation = new CGEditorLocation();
  ViewerHelperEditors.registerEvents(EditorLocation);
  EditorLocation.init(ViewerHelperEditors.getLayer(FIELD_TYPE_LOCATION));
  EditorsFactory.register(FIELD_TYPE_LOCATION, EditorLocation);

  var EditorSummation = new CGEditorSummation();
  ViewerHelperEditors.registerEvents(EditorSummation);
  EditorSummation.init(ViewerHelperEditors.getLayer(FIELD_TYPE_SUMMATION));
  EditorsFactory.register(FIELD_TYPE_SUMMATION, EditorSummation);

  this.extLayer.dom.style.display = sDisplay;
  this.extLayer.dom.style.zIndex = zIndex;
};

ViewerHelperEditors.getLayer = function (Type) {
  var extEditorLayer = ViewerHelperEditors.extLayer.select(CSS_EDITOR + DOT + "e" + Type.toLowerCase()).first();
  if (extEditorLayer == null) return null;
  return extEditorLayer.dom;
};

ViewerHelperEditors.show = function () {
  ViewerHelperEditors.extLayer.dom.style.display = "block";
};

ViewerHelperEditors.hide = function () {
  ViewerHelperEditors.extLayer.dom.style.display = "none";
};

ViewerHelperEditors.getCurrentEditor = function () {
  if (ViewerHelperEditors.CurrentEditor == null) return;
  return ViewerHelperEditors.CurrentEditor;
};

ViewerHelperEditors.showEditor = function (Type) {
  var Editor = EditorsFactory.get(Type);
  if (!Editor) return;

  var CurrentEditor = ViewerHelperEditors.getCurrentEditor();
  if (CurrentEditor != null) CurrentEditor.hide();

  ViewerHelperEditors.CurrentEditor = Editor;
  ViewerHelperEditors.CurrentEditor.show();
};

ViewerHelperEditors.hideEditor = function (Type) {
  var Editor = EditorsFactory.get(Type);
  if (!Editor) return;
  Editor.hide();
};

ViewerHelperEditors.hideCurrentEditor = function () {
  if (ViewerHelperEditors.CurrentEditor == null) return;
  ViewerHelperEditors.CurrentEditor.hide();
  ViewerHelperEditors.CurrentEditor = null;
};

ViewerHelperEditors.hideAllEditors = function () {
  var EditorList = ViewerHelperEditors.extLayer.select(CSS_EDITOR);
  EditorList.each(function (Editor) {
    Editor.dom.style.display = "none";
  }, this);
};

ViewerHelperEditors.refresh = function () {
  var CurrentEditor = ViewerHelperEditors.getCurrentEditor();
  if (CurrentEditor == null) return;
  CurrentEditor.refresh();
};

// #############################################################################################################

ViewerHelperEditors.atEditorShow = function (Editor) {
  if ((ViewerHelperEditors.CurrentEditor != null) && (ViewerHelperEditors.CurrentEditor != Editor)) ViewerHelperEditors.CurrentEditor.hide();
  ViewerHelperEditors.CurrentEditor = Editor;
};

ViewerHelperEditors.atEditorHide = function (Editor) {
  ViewerHelperEditors.CurrentEditor = null;
};

ViewerHelperEditors.atEditorLock = function (Editor) {
  if (this.onLock) this.onLock();
  ViewerHelperEditors.extLayer.dom.style.overflow = "hidden";
};

ViewerHelperEditors.atEditorUnLock = function (Editor) {
  if (this.onUnLock) this.onUnLock();
  ViewerHelperEditors.extLayer.dom.style.overflow = "hidden";
};