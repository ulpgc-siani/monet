//----------------------------------------------------------------------
// show role
//----------------------------------------------------------------------
function CGActionShowRole() {
  this.base = CGAction;
  this.base(2);
};

CGActionShowRole.prototype = new CGAction;
CGActionShowRole.constructor = CGActionShowRole;
CommandFactory.register(CGActionShowRole, { Code: 0 }, true);

CGActionShowRole.prototype.step_1 = function () {

  Desktop.hideBanner();
  State.isShowingPrototype = false;

  Kernel.loadRole(this, this.Code);
};

CGActionShowRole.prototype.step_2 = function () {
  ViewRole.setContent(this.data);
  ViewRole.refresh();
  ViewRole.show();
  this.setActiveFurniture(Furniture.ROLES);
  Desktop.Main.Center.Body.activateRole();
};

//----------------------------------------------------------------------
// add user role
//----------------------------------------------------------------------
function CGActionAddUserRole() {
  this.base = CGAction;
  this.base(4);
};

CGActionAddUserRole.prototype = new CGAction;
CGActionAddUserRole.constructor = CGActionAddUserRole;
CommandFactory.register(CGActionAddUserRole, { Code: 0, UserId: 1, Username: 2, BeginDate: 3, ExpireDate: 4 }, false);

CGActionAddUserRole.prototype.onFailure = function (response) {
  response = response.substring(response.indexOf(":") + 1);
  var message = response;

  if (response.indexOf("ERR_ROLE_ACTIVE") != -1)
    message = Lang.ViewerHelperRole.UserRoleExists;

  Desktop.reportError(message);

  this.terminate();
};

CGActionAddUserRole.prototype.step_1 = function () {
  Kernel.addUserRole(this, this.Code, this.UserId, this.Username, this.BeginDate, this.ExpireDate);
};

CGActionAddUserRole.prototype.step_2 = function () {
  var jsonData = Ext.util.JSON.decode(this.data);
  var Role = new CGRole(jsonData);

  var Process = new CGProcessAddRole();
  Process.InstanceId = Role.User.getId();
  Process.Code = this.Code;
  Process.Role = Role;
  Process.execute();

  this.terminate();
};

//----------------------------------------------------------------------
// add service role
//----------------------------------------------------------------------
function CGActionAddServiceRole() {
  this.base = CGAction;
  this.base(2);
};

CGActionAddServiceRole.prototype = new CGAction;
CGActionAddServiceRole.constructor = CGActionAddServiceRole;
CommandFactory.register(CGActionAddServiceRole, { Code: 0, PartnerId: 1, PartnerServiceName: 2, BeginDate: 3, ExpireDate: 4 }, false);

CGActionAddServiceRole.prototype.onFailure = function (response) {
  response = response.substring(response.indexOf(":") + 1);
  var message = response;

  if (response.indexOf("ERR_ROLE_ACTIVE") != -1)
    message = Lang.ViewerHelperRole.ServiceRoleExists;

  Desktop.reportError(message);

  this.terminate();
};

CGActionAddServiceRole.prototype.step_1 = function () {
  Kernel.addServiceRole(this, this.Code, this.PartnerId, this.PartnerServiceName, this.BeginDate, this.ExpireDate);
};

CGActionAddServiceRole.prototype.step_2 = function () {
  var jsonData = Ext.util.JSON.decode(this.data);
  var Role = new CGRole(jsonData);

  var Process = new CGProcessAddRole();
  Process.InstanceId = generateIdForKey(Role.PartnerId + Role.PartnerServiceName);
  Process.Code = this.Code;
  Process.Role = Role;
  Process.execute();

  this.terminate();
};

//----------------------------------------------------------------------
// add feeder role
//----------------------------------------------------------------------
function CGActionAddFeederRole() {
  this.base = CGAction;
  this.base(2);
};

CGActionAddFeederRole.prototype = new CGAction;
CGActionAddFeederRole.constructor = CGActionAddFeederRole;
CommandFactory.register(CGActionAddFeederRole, { Code: 0, PartnerId: 1, PartnerServiceName: 2, BeginDate: 3, ExpireDate: 4 }, false);

CGActionAddFeederRole.prototype.onFailure = function (response) {
  response = response.substring(response.indexOf(":") + 1);
  var message = response;

  if (response.indexOf("ERR_ROLE_ACTIVE") != -1)
    message = Lang.ViewerHelperRole.FeederRoleExists;

  Desktop.reportError(message);

  this.terminate();
};

CGActionAddFeederRole.prototype.step_1 = function () {
  Kernel.addFeederRole(this, this.Code, this.PartnerId, this.PartnerServiceName, this.BeginDate, this.ExpireDate);
};

CGActionAddFeederRole.prototype.step_2 = function () {
  var jsonData = Ext.util.JSON.decode(this.data);
  var Role = new CGRole(jsonData);

  var Process = new CGProcessAddRole();
  Process.InstanceId = generateIdForKey(Role.PartnerId + Role.PartnerServiceName);
  Process.Code = this.Code;
  Process.Role = Role;
  Process.execute();

  this.terminate();
};

//----------------------------------------------------------------------
// save user role
//----------------------------------------------------------------------
function CGActionSaveUserRole() {
  this.base = CGAction;
  this.base(2);
};

CGActionSaveUserRole.prototype = new CGAction;
CGActionSaveUserRole.constructor = CGActionSaveUserRole;
CommandFactory.register(CGActionSaveUserRole, { Id: 0, UserId: 1, BeginDate: 2, ExpireDate: 3 }, false);

CGActionSaveUserRole.prototype.step_1 = function () {
  Kernel.saveUserRole(this, this.Id, this.UserId, this.BeginDate, this.ExpireDate);
};

CGActionSaveUserRole.prototype.step_2 = function () {
  var CurrentViewer = State.RoleListViewController.getCurrentView();
  CurrentViewer.refresh();
  this.terminate();
};

//----------------------------------------------------------------------
// save service role
//----------------------------------------------------------------------
function CGActionSaveServiceRole() {
  this.base = CGAction;
  this.base(2);
};

CGActionSaveServiceRole.prototype = new CGAction;
CGActionSaveServiceRole.constructor = CGActionSaveServiceRole;
CommandFactory.register(CGActionSaveServiceRole, { Id: 0, PartnerId: 1, PartnerServiceName: 2, BeginDate: 3, ExpireDate: 4 }, false);

CGActionSaveServiceRole.prototype.step_1 = function () {
  Kernel.saveServiceRole(this, this.Id, this.PartnerId, this.PartnerServiceName, this.BeginDate, this.ExpireDate);
};

CGActionSaveServiceRole.prototype.step_2 = function () {
  var CurrentViewer = State.RoleListViewController.getCurrentView();
  CurrentViewer.refresh();
  this.terminate();
};

//----------------------------------------------------------------------
// save feeder role
//----------------------------------------------------------------------
function CGActionSaveFeederRole() {
  this.base = CGAction;
  this.base(2);
};

CGActionSaveFeederRole.prototype = new CGAction;
CGActionSaveFeederRole.constructor = CGActionSaveFeederRole;
CommandFactory.register(CGActionSaveFeederRole, { Id: 0, PartnerId: 1, PartnerServiceName: 2, BeginDate: 3, ExpireDate: 4 }, false);

CGActionSaveFeederRole.prototype.step_1 = function () {
  Kernel.saveFeederRole(this, this.Id, this.PartnerId, this.PartnerServiceName, this.BeginDate, this.ExpireDate);
};

CGActionSaveFeederRole.prototype.step_2 = function () {
  var CurrentViewer = State.RoleListViewController.getCurrentView();
  CurrentViewer.refresh();
  this.terminate();
};