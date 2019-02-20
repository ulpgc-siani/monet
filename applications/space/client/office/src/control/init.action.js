//----------------------------------------------------------------------
// Action init
//----------------------------------------------------------------------
function CGActionInit() {
  this.base = CGAction;
  this.base(4);
};

CGActionInit.prototype = new CGAction;
CGActionInit.constructor = CGActionInit;
CommandFactory.register(CGActionInit, null, false);

//PUBLIC
CGActionInit.prototype.onFailure = function (sResponse) {
  var Action = new CGActionLogout();
  Action.execute();

  Ext.MessageBox.hide();
  Desktop.hideLoading();
  alert(Lang.Exceptions.InitApplication);

  this.terminate();
};

CGActionInit.prototype.atBeforeUnload = function () {
  if ((State.LastView == null) || (!Desktop.Main.Center.Body.isContainerView(State.LastView))) {
    return "";
  }

  var DOMNode = State.LastView.getDOM();
  if ((DOMNode.isDirty) && (DOMNode.isDirty())) {
    return Lang.ViewNode.DialogUnload.Description;
  }
};

CGActionInit.prototype.atUnload = function () {
  Kernel.exiting(Account.getInstanceId());
};

CGActionInit.prototype.step_1 = function () {
  var Process = new CGProcessLoadAccount();
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionInit.prototype.step_2 = function () {
  Kernel.loadPartners(this);
};

CGActionInit.prototype.step_3 = function () {
  var DefinitionView;

  Desktop.hideLoading();
  Desktop.Main.Right.loadStateFromCookies();
  Desktop.refresh();

  State.PartnerList = Ext.util.JSON.decode(this.data);

  CommandListener.onBeforeUnload = CGActionInit.prototype.atBeforeUnload.bind(this);
  CommandListener.onUnload = CGActionInit.prototype.atUnload.bind(this);

  var initializerTask = Account.getUser().getInitializerTask();
  if (initializerTask != null) {
    CommandDispatcher.dispatch("showtask(" + initializerTask.id + ")");
    this.setActiveFurniture(Furniture.TASKBOARD);
  }
  else {
    if (Account.Links.length == 0) return;

    var link = Account.Links[0];
    var user = Account.getUser();
    var rootNode = user.getRootNode();

    if (link.type == Furniture.DESKTOP) {
      DefinitionView = Extension.getDefinitionDefaultView(rootNode.code, BUSINESS_MODEL_BROWSE);
      State.View = DefinitionView.Name;
      this.setActiveFurniture(Furniture.DESKTOP, rootNode.id);
      CommandDispatcher.dispatch("shownode(" + rootNode.id + "," + State.View + ")");
    }
    else {
      this.setActiveFurniture(link.type, link.id);
      CommandDispatcher.dispatch(ViewFurnitureSet.getCommand(link));
    }
  }

  this.execute();
};

CGActionInit.prototype.step_4 = function () {
  PushClient.init(Account.getInstanceId());
  this.terminate();
};