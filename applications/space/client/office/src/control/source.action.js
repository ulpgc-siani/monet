//----------------------------------------------------------------------
// show source
//----------------------------------------------------------------------
function CGActionShowSource() {
  this.base = CGAction;
  this.base(2);
};

CGActionShowSource.prototype = new CGAction;
CGActionShowSource.constructor = CGActionShowSource;
CommandFactory.register(CGActionShowSource, { Id: 0 }, true);

CGActionShowSource.prototype.step_1 = function () {

  Desktop.hideBanner();
  State.isShowingPrototype = false;

  Kernel.loadSource(this, this.Id);
};

CGActionShowSource.prototype.step_2 = function () {
  ViewSource.setContent(this.data);
  ViewSource.refresh();
  ViewSource.show();
  Desktop.Main.Center.Body.activateSource();
};

//----------------------------------------------------------------------
// render source
//----------------------------------------------------------------------
function CGActionRenderSource() {
  this.base = CGAction;
  this.base(3);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionRenderSource.prototype = new CGAction;
CGActionRenderSource.constructor = CGActionRenderSource;
CommandFactory.register(CGActionRenderSource, { Id: 0, Label: 1, IdDOMViewLayer: 2, IdDOMViewLayerTermsOptions: 3 }, false);

CGActionRenderSource.prototype.atBeforePushView = function (Object) {
  if (State.SourceListViewController == null) return;

  var SourceViewer = State.SourceListViewController.getFirstView();
  var SourceItem = SourceViewer.getActiveItem();
  var Source = CGSource.createFromListItem(SourceItem);
  var Term = CGTerm.createFromListItem(Object);
  var helper = ViewerSidebar.getHelper(Helper.SOURCE);

  helper.setTarget({Source: Source, Term: Term});
  helper.refresh();

  return Term.Type != CGTerm.TERM;
};

CGActionRenderSource.prototype.atPushView = function (View) {
  if (State.SourceListViewController == null) return;

  var SourceViewer = State.SourceListViewController.getFirstView();
  var ParentViewer = State.SourceListViewController.getParentViewOfCurrentView();
  var SourceItem = SourceViewer.getActiveItem();
  var Source = CGSource.createFromListItem(SourceItem);
  var Term = null;
  var helper = ViewerSidebar.getHelper(Helper.SOURCE);

  if (ParentViewer != null) {
    var ParentItem = ParentViewer.getActiveItem();
    Term = CGTerm.createFromListItem(ParentItem);
  }

  helper.setTarget({Source: Source, Term: Term});
  helper.refresh();
};

CGActionRenderSource.prototype.atBoundItem = function (Sender, Item) {
  if (Item.type != CGTerm.TERM) Item.css = "nav";
  var tags = SerializerData.deserializeSet(Item.tags);
  Item.formattedTags = "";
  if (tags.length == 0) Item.formattedTags = "<span class='notags'>" + Lang.ViewSourceList.NoTags + "</span>";
  else {
    for (var i = 0; i < tags.length; i++)
      Item.formattedTags += "<span>" + tags[i] + "</span>";
  }
};

CGActionRenderSource.prototype.destroyViewController = function () {
  State.SourceListViewController = null;
  $(Literals.ListViewerWizard).innerHTML = "";
};

CGActionRenderSource.prototype.createViewController = function () {

  this.destroyViewController();

  var Options, TermsOptions;

  eval($(this.IdDOMViewLayerTermsOptions).innerHTML);
  TermsOptions = Options;

  var TermsListViewerFactory = new CGListViewerFactory(Kernel.getLoadSourceTermsLink(this.Id, "::code::", "tree", 1), TermsOptions, Context.Config.Language, Context.Config.ImagesPath);
  TermsListViewerFactory.onBoundItem = this.atBoundItem;

  var SourceListViewer = TermsListViewerFactory.get({code: ""});
  //SourceListViewer.setLabel(Lang.ViewerHelperSource.TermList);
  SourceListViewer.setName(this.Id);

  State.SourceListViewController = new CGNavigationViewController(this.IdDOMViewLayer, TermsListViewerFactory, Context.Config.ImagesPath);
  State.SourceListViewController.pushView(SourceListViewer);
  State.SourceListViewController.onPushView = this.atPushView;
  State.SourceListViewController.onBeforePushView = this.atBeforePushView;
};

CGActionRenderSource.prototype.step_1 = function () {

  if ((this.IdDOMViewLayer == null) || (this.IdDOMViewLayerTermsOptions == null)) {
    this.terminate();
    return;
  }

  var Process = new CGProcessLoadHelperListViewer();
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionRenderSource.prototype.step_2 = function () {
  var Process = new CGProcessLoadHelperSourceViewer();
  Process.IdSource = this.Id;
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionRenderSource.prototype.step_3 = function () {
  this.createViewController();
  this.terminate();
};

//----------------------------------------------------------------------
// show add source term
//----------------------------------------------------------------------
function CGActionShowAddSourceTerm() {
  this.base = CGAction;
  this.base(2);
};

CGActionShowAddSourceTerm.prototype = new CGAction;
CGActionShowAddSourceTerm.constructor = CGActionAddSourceTerm;
CommandFactory.register(CGActionShowAddSourceTerm, { Type: 0 }, false);

CGActionShowAddSourceTerm.prototype.step_1 = function () {
  var SourceViewer = State.SourceListViewController.getFirstView();
  var SourceItem = SourceViewer.getActiveItem();
  var Source = CGSource.createFromListItem(SourceItem);

  var Process = new CGProcessLoadHelperSourceViewer();
  Process.IdSource = Source.Id;
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionShowAddSourceTerm.prototype.step_2 = function () {
  var SourceViewer = State.SourceListViewController.getFirstView();
  var SourceItem = SourceViewer.getActiveItem();
  var Source = CGSource.createFromListItem(SourceItem);
  var Term = new CGTerm();
  var helper = ViewerSidebar.getHelper(Helper.SOURCE);

  helper.setTarget({Source: Source, Term: Term });
  helper.refresh();
};

//----------------------------------------------------------------------
// add source term
//----------------------------------------------------------------------
function CGActionAddSourceTerm() {
  this.base = CGAction;
  this.base(3);
};

CGActionAddSourceTerm.prototype = new CGAction;
CGActionAddSourceTerm.constructor = CGActionAddSourceTerm;
CommandFactory.register(CGActionAddSourceTerm, { IdSource: 0, Code: 1, Label: 2 }, false);

CGActionAddSourceTerm.prototype.step_1 = function () {
  var ParentViewer = State.SourceListViewController.getParentViewOfCurrentView();
  var IdSource, CodeParent;

  if (ParentViewer == null || ParentViewer.getName() == "sourceList") {
    IdSource = this.IdSource;
    CodeParent = null;
  }
  else {
    var ParentItem = ParentViewer.getActiveItem();
    var Parent = CGTerm.createFromListItem(ParentItem);
    IdSource = Parent.IdSource;
    CodeParent = Parent.Code;
  }

  Kernel.addSourceTerm(this, IdSource, CodeParent, this.Code, this.Label);
};

CGActionAddSourceTerm.prototype.step_2 = function () {
  var Process = new CGProcessRefreshSourceNewTerms();
  Process.IdSource = this.IdSource;
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionAddSourceTerm.prototype.step_3 = function () {
  var CurrentViewer = State.SourceListViewController.getCurrentView();
  CurrentViewer.refresh();
  this.terminate();
};

//----------------------------------------------------------------------
// save source term attribute
//----------------------------------------------------------------------
function CGActionSaveSourceTermAttribute() {
  this.base = CGAction;
  this.base(2);
};

CGActionSaveSourceTermAttribute.prototype = new CGAction;
CGActionSaveSourceTermAttribute.constructor = CGActionSaveSourceTermAttribute;
CommandFactory.register(CGActionSaveSourceTermAttribute, { IdSource: 0, Code: 1, Attribute: 2, Value: 3 }, false);

CGActionSaveSourceTermAttribute.prototype.step_1 = function () {
  Kernel.saveSourceTermAttribute(this, this.IdSource, this.Code, this.Attribute, this.Value);
};

CGActionSaveSourceTermAttribute.prototype.step_2 = function () {
  var CurrentViewer = State.SourceListViewController.getCurrentView();
  var Term = CGTerm.createFromListItem(CurrentViewer.getActiveItem());
  var Viewer;

  if (Term.Type == CGTerm.TERM) Viewer = CurrentViewer;
  else Viewer = State.SourceListViewController.getParentViewOfCurrentView();

  if (this.Attribute == "tags") {
    var ViewerActiveItem = Viewer.getActiveItem();
    ViewerActiveItem.tags = this.Value;
    Viewer.updateItem(ViewerActiveItem);
    this.terminate();
    return;
  }

  Viewer.refresh();

  this.terminate();
};

//----------------------------------------------------------------------
// save source term type
//----------------------------------------------------------------------
function CGActionSaveSourceTermType() {
  this.base = CGAction;
  this.base(2);
};

CGActionSaveSourceTermType.prototype = new CGAction;
CGActionSaveSourceTermType.constructor = CGActionSaveSourceTermType;
CommandFactory.register(CGActionSaveSourceTermType, { IdSource: 0, Code: 1, PreviousType: 2, Type: 3 }, false);

CGActionSaveSourceTermType.prototype.step_1 = function () {
  Kernel.saveSourceTermAttribute(this, this.IdSource, this.Code, "type", this.Type);
};

CGActionSaveSourceTermType.prototype.step_2 = function () {
  var Viewer;

  if (this.Type == CGTerm.TERM) {
    if (this.PreviousType == CGTerm.SUPER_TERM || this.PreviousType == CGTerm.CATEGORY) {
      Viewer = State.SourceListViewController.getCurrentView();
      State.SourceListViewController.popView(Viewer);
      Viewer = State.SourceListViewController.getCurrentView();
      Viewer.refresh();
    }
  }
  else {
    if (this.PreviousType == CGTerm.TERM) {
      Viewer = State.SourceListViewController.getCurrentView();
      Viewer.refresh();
    }
    else {
      Viewer = State.SourceListViewController.getParentViewOfCurrentView();
      Viewer.refresh();
    }
  }

  this.terminate();
};

//----------------------------------------------------------------------
// delete source term
//----------------------------------------------------------------------
function CGActionDeleteSourceTerm() {
  this.base = CGAction;
  this.base(3);
};

CGActionDeleteSourceTerm.prototype = new CGAction;
CGActionDeleteSourceTerm.constructor = CGActionDeleteSourceTerm;
CommandFactory.register(CGActionDeleteSourceTerm, { IdSource: 0, Code: 1 }, false);

CGActionDeleteSourceTerm.prototype.step_1 = function () {
  Kernel.deleteSourceTerm(this, this.IdSource, this.Code);
};

CGActionDeleteSourceTerm.prototype.step_2 = function () {
  var Process = new CGProcessRefreshSourceNewTerms();
  Process.IdSource = this.IdSource;
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionDeleteSourceTerm.prototype.step_3 = function () {
  var Viewer = State.SourceListViewController.getParentViewOfCurrentView();
  Viewer.refresh();
  this.terminate();
};

//----------------------------------------------------------------------
// check exists source term
//----------------------------------------------------------------------
function CGActionCheckExistsSourceTerm() {
  this.base = CGAction;
  this.base(2);
};

CGActionCheckExistsSourceTerm.prototype = new CGAction;
CGActionCheckExistsSourceTerm.constructor = CGActionCheckExistsSourceTerm;
CommandFactory.register(CGActionCheckExistsSourceTerm, { IdSource: 0, Code: 1 }, false);

CGActionCheckExistsSourceTerm.prototype.step_1 = function () {
  Kernel.existsSourceTerm(this, this.IdSource, this.Code);
};

CGActionCheckExistsSourceTerm.prototype.step_2 = function () {
  var exists = (this.data == "true");
  var helper = ViewerSidebar.getHelper(Helper.SOURCE);
  if (exists) helper.showTermExists();
  this.terminate();
};

//----------------------------------------------------------------------
// publish source terms 
//----------------------------------------------------------------------
function CGActionPublishSourceTerms() {
  this.base = CGAction;
  this.base(3);
};

CGActionPublishSourceTerms.prototype = new CGAction;
CGActionPublishSourceTerms.constructor = CGActionPublishSourceTerms;
CommandFactory.register(CGActionPublishSourceTerms, { IdSource: 0, Terms: 1 }, false);

CGActionPublishSourceTerms.prototype.step_1 = function () {
  Kernel.publishSourceTerms(this, this.IdSource, this.Terms);
};

CGActionPublishSourceTerms.prototype.step_2 = function () {
  var Process = new CGProcessRefreshSourceNewTerms();
  Process.IdSource = this.IdSource;
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionPublishSourceTerms.prototype.step_3 = function () {
  var Viewer = State.SourceListViewController.getFirstView();
  Viewer.refresh();
  this.terminate();
};