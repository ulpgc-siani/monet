//----------------------------------------------------------------------
// show role list
//----------------------------------------------------------------------
function CGActionShowRoleList() {
  this.base = CGAction;
  this.base(2);
};

CGActionShowRoleList.prototype = new CGAction;
CGActionShowRoleList.constructor = CGActionShowRoleList;
CommandFactory.register(CGActionShowRoleList, null, true);

CGActionShowRoleList.prototype.step_1 = function () {

  Desktop.hideBanner();
  State.isShowingPrototype = false;

  Kernel.loadSystemTemplate(this, "rolelist");
};

CGActionShowRoleList.prototype.step_2 = function () {
  ViewRoleList.setContent(this.data);
  ViewRoleList.refresh();
  ViewRoleList.show();
  this.setActiveFurniture(Furniture.ROLES);
  Desktop.Main.Center.Body.activateRoleList();
};

//----------------------------------------------------------------------
// Render Role List
//----------------------------------------------------------------------
function CGActionRenderRoleList() {
  this.base = CGAction;
  this.base(4);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionRenderRoleList.prototype = new CGAction;
CGActionRenderRoleList.constructor = CGActionRenderRoleList;
CommandFactory.register(CGActionRenderRoleList, { IdDOMViewLayer: 0, IdDOMViewDefinitionsLayerOptions: 1, IdDOMViewLayerOptions: 2 }, false);

CGActionRenderRoleList.prototype.getRolesOptions = function () {
  var Options = clone(this.RolesOptions);
  Options.Operations = new Array();
  Options.Templates.NoItems = Lang.ViewerHelperRole.NoHistory;
  return Options;
};

CGActionRenderRoleList.prototype.atBeforePushView = function (Object) {
  if (State.RoleListViewController == null) return;
  if (Object != null) View = Object.view;

  var DefinitionViewer = State.RoleListViewController.getFirstView();
  var DefinitionItem = DefinitionViewer.getActiveItem();
  var Definition = new CGRoleDefinition(DefinitionItem);
  var Role = new CGRole(Object);

  var helper = ViewerSidebar.getHelper(Helper.ROLE);
  if (Object.view == "grouped") {
    helper.setTarget({Definition: Definition, HistoryOptions: this.getRolesOptions(), PartnerList: State.PartnerList, Role: Role, View: "save"});
    helper.refresh();
  }
  else helper.hide();

  return Object.view != "grouped";
};

CGActionRenderRoleList.prototype.atBoundItem = function (Sender, Item) {
  if (Item.view == null || Item.view != "grouped") Item.css = "nav";
  if (Item.beginDate) Item.formattedBeginDate = getFormattedDateTime(parseServerDate(Item.beginDate), Context.Config.Language, false);
  if (Item.expireDate) Item.formattedExpireDate = getFormattedDateTime(parseServerDate(Item.expireDate), Context.Config.Language, false);
  else Item.formattedExpireDate = "";
};

CGActionRenderRoleList.prototype.atOperationClick = function (Sender, name) {
  var DefinitionViewer = State.RoleListViewController.getFirstView();
  var DefinitionItem = DefinitionViewer.getActiveItem();
  var helper = ViewerSidebar.getHelper(Helper.ROLE);
  var Definition = new CGRoleDefinition(DefinitionItem);
  var RoleType = null;

  if (name == "adduser") RoleType = "user";
  else if (name == "addservice") RoleType = "service";
  else if (name == "addfeeder") RoleType = "feeder";

  helper.setTarget({Definition: Definition, HistoryOptions: this.getRolesOptions(), PartnerList: State.PartnerList, Role: null, View: "add", RoleType: RoleType });
  helper.refresh();
};

CGActionRenderRoleList.prototype.atBoundOperation = function (Sender, Operation) {
  var DefinitionViewer = State.RoleListViewController.getFirstView();
  var DefinitionItem = DefinitionViewer.getActiveItem();

  if (Operation.name == "adduser" && !DefinitionItem.enableUsers) Operation.visible = false;
  if (Operation.name == "addservice" && DefinitionItem.PartnerServiceOntologies == null) Operation.visible = false;
  if (Operation.name == "addfeeder" && DefinitionItem.PartnerFeederOntologies == null) Operation.visible = false;
};

CGActionRenderRoleList.prototype.destroyViewController = function () {
  State.RoleListViewController = null;
  $(Literals.ListViewerWizard).innerHTML = "";
};

CGActionRenderRoleList.prototype.createViewController = function () {

  this.destroyViewController();

  var Options, DefinitionsOptions;

  eval($(this.IdDOMViewDefinitionsLayerOptions).innerHTML);
  DefinitionsOptions = Options;

  eval($(this.IdDOMViewLayerOptions).innerHTML);
  this.RolesOptions = Options;

  var RoleDefinitionListViewerFactory = new CGListViewerFactory(Kernel.getLoadRoleDefinitionListLink(), DefinitionsOptions, Context.Config.Language, Context.Config.ImagesPath);
  var RoleListViewerFactory = new CGListViewerFactory(Kernel.getLoadRoleListLink("::code::", "::view::"), this.RolesOptions, Context.Config.Language, Context.Config.ImagesPath);

  RoleDefinitionListViewerFactory.onBoundItem = this.atBoundItem.bind(this);
  RoleListViewerFactory.onBoundItem = this.atBoundItem.bind(this);
  RoleListViewerFactory.onOperationClick = this.atOperationClick.bind(this);
  RoleListViewerFactory.onBoundOperation = this.atBoundOperation.bind(this);

  var RoleDefinitionListViewer = RoleDefinitionListViewerFactory.get(null);
  //RoleDefinitionListViewer.setLabel(Lang.ViewerHelperRole.DefinitionList);
  RoleDefinitionListViewer.setName("roleDefinitionList");

  State.RoleListViewController = new CGNavigationViewController(this.IdDOMViewLayer, RoleListViewerFactory, Context.Config.ImagesPath);
  State.RoleListViewController.pushView(RoleDefinitionListViewer);
  State.RoleListViewController.onBeforePushView = this.atBeforePushView.bind(this);
};

CGActionRenderRoleList.prototype.step_1 = function () {

  if ((this.IdDOMViewLayer == null) || (this.IdDOMViewDefinitionsLayerOptions == null) || (this.IdDOMViewLayerOptions == null)) {
    this.terminate();
    return;
  }

  var Process = new CGProcessLoadHelperListViewer();
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionRenderRoleList.prototype.step_2 = function () {
  var Process = new CGProcessLoadHelperRoleViewer();
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionRenderRoleList.prototype.step_3 = function () {
  this.createViewController();
  this.terminate();
};