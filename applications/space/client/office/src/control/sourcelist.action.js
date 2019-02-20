//----------------------------------------------------------------------
// show source list
//----------------------------------------------------------------------
function CGActionShowSourceList() {
  this.base = CGAction;
  this.base(2);
};

CGActionShowSourceList.prototype = new CGAction;
CGActionShowSourceList.constructor = CGActionShowSourceList;
CommandFactory.register(CGActionShowSourceList, null, true);

CGActionShowSourceList.prototype.step_1 = function () {

  Desktop.hideBanner();
  State.isShowingPrototype = false;

  Kernel.loadSystemTemplate(this, "sourcelist");
};

CGActionShowSourceList.prototype.step_2 = function () {
  ViewSourceList.setContent(this.data);
  ViewSourceList.refresh();
  ViewSourceList.show();
  Desktop.Main.Center.Body.activateSourceList();
};

//----------------------------------------------------------------------
// render source list
//----------------------------------------------------------------------
function CGActionRenderSourceList() {
  this.base = CGAction;
  this.base(3);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionRenderSourceList.prototype = new CGAction;
CGActionRenderSourceList.constructor = CGActionRenderSourceList;
CommandFactory.register(CGActionRenderSourceList, { IdDOMViewLayer: 0, IdDOMViewLayerOptions: 1, IdDOMViewLayerTermsOptions: 2 }, false);

CGActionRenderSourceList.prototype.atBeforePushView = function (Object) {
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

CGActionRenderSourceList.prototype.atPushView = function (View) {
  if (State.SourceListViewController == null) return;

  var SourceViewer = State.SourceListViewController.getFirstView();
  var ParentViewer = State.SourceListViewController.getParentViewOfCurrentView();
  var SourceItem = SourceViewer.getActiveItem();
  var Source = CGSource.createFromListItem(SourceItem);
  var Term = null;
  var helper = ViewerSidebar.getHelper(Helper.SOURCE);

  if (ParentViewer != null && ParentViewer != SourceViewer) {
    var ParentItem = ParentViewer.getActiveItem();
    Term = CGTerm.createFromListItem(ParentItem);
  }

  helper.setTarget({Source: Source, Term: Term});
  helper.refresh();
};

CGActionRenderSourceList.prototype.atBoundItem = function (Sender, Item) {
  if (Item.type != CGTerm.TERM) Item.css = "nav";
  var tags = SerializerData.deserializeSet(Item.tags);
  Item.formattedTags = "";
  if (tags.length == 0) Item.formattedTags = "<span class='notags'>" + Lang.ViewSourceList.NoTags + "</span>";
  else {
    for (var i = 0; i < tags.length; i++)
      Item.formattedTags += "<span>" + tags[i] + "</span>";
  }
};

CGActionRenderSourceList.prototype.destroyViewController = function () {
  State.SourceListViewController = null;
  $(Literals.ListViewerWizard).innerHTML = "";
};

CGActionRenderSourceList.prototype.createViewController = function () {

  this.destroyViewController();

  var Options, SourceOptions, TermsOptions;

  eval($(this.IdDOMViewLayerOptions).innerHTML);
  SourceOptions = Options;

  eval($(this.IdDOMViewLayerTermsOptions).innerHTML);
  TermsOptions = Options;

  var SourceListViewerFactory = new CGListViewerFactory(Kernel.getLoadSourceListLink(), SourceOptions, Context.Config.Language, Context.Config.ImagesPath);
  var TermsListViewerFactory = new CGListViewerFactory(Kernel.getLoadSourceTermsLink("::idSource::", "::code::", "tree", 1), TermsOptions, Context.Config.Language, Context.Config.ImagesPath);

  SourceListViewerFactory.onBoundItem = this.atBoundItem;
  TermsListViewerFactory.onBoundItem = this.atBoundItem;

  var SourceListViewer = SourceListViewerFactory.get(null);
  //SourceListViewer.setLabel(Lang.ViewerHelperSource.SourceList);
  SourceListViewer.setName("sourceList");

  State.SourceListViewController = new CGNavigationViewController(this.IdDOMViewLayer, TermsListViewerFactory, Context.Config.ImagesPath);
  State.SourceListViewController.pushView(SourceListViewer);
  State.SourceListViewController.onPushView = this.atPushView;
  State.SourceListViewController.onBeforePushView = this.atBeforePushView;
};

CGActionRenderSourceList.prototype.step_1 = function () {

  if ((this.IdDOMViewLayer == null) || (this.IdDOMViewLayerOptions == null) || (this.IdDOMViewLayerTermsOptions == null)) {
    this.terminate();
    return;
  }

  var Process = new CGProcessLoadHelperListViewer();
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionRenderSourceList.prototype.step_2 = function () {
  var Process = new CGProcessLoadHelperSourceViewer();
  Process.IdSource = null;
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionRenderSourceList.prototype.step_3 = function () {
  this.createViewController();
  this.terminate();
};