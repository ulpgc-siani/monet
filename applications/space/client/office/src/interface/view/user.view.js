ViewUser = new Object;

ViewUser.sLayerName = null;
ViewUser.User = null;
ViewUser.Units = null;

ViewUser.init = function (sLayerName) {
  ViewUser.sLayerName = sLayerName;

  var html = AppTemplate.ViewUser;
  html = translate(html, Lang.ViewUser);

  this.unitItemTemplate = new Ext.Template(AppTemplate.ViewUserUnitItem);
  this.unitItemTemplate.compile();

  $(ViewUser.sLayerName).innerHTML = html;

  this.extUsername = Ext.get("Username");
  this.extUsernamePanel = Ext.get("UsernamePanel");
  this.extUnitList = this.extUsernamePanel.select("ul.units").first();
  Event.observe(this.extUsername.dom, "click", ViewUser.atUsernameClick.bind(this));
};

ViewUser.addUnits = function (Units) {
  this.Units = Units;
  this.extUnitList.dom.innerHTML = "";
  for (var i = 0; i < this.Units.length; i++) {
    var unit = this.Units[i];
    this.addUnit(unit);
  }
};

ViewUser.addUnit = function (unit) {
  unit.fullLabel = unit.label + (unit.active ? " " + Lang.ViewUser.Current : "");
  unit.anchorTitle = unit.active ? unit.label : Lang.ViewUser.GotoUnit + " " + unit.label;
  unit.disabledLabel = unit.disabled ? "disabled" : "";
  unit.command = unit.disabled ? "javascript:void(null)" : "showunit(" + unit.id + "," + unit.url + ")";
  element = this.unitItemTemplate.append(this.extUnitList, unit, true);
  CommandListener.capture(element);
};

ViewUser.setTarget = function (account) {
  this.User = account.getUser();
  this.Units = account.getUnits();
  CommandListener.capture($(ViewUser.sLayerName));
};

ViewUser.show = function () {
  if (!$(ViewUser.sLayerName)) return;
  $(ViewUser.sLayerName).show();
};

ViewUser.hide = function () {
  if (!$(ViewUser.sLayerName)) return;
  $(ViewUser.sLayerName).hide();
};

ViewUser.refresh = function () {
  if (!ViewUser.User) return;

  $("UsernameLabel").innerHTML = ViewUser.User.getInfo().getFullname();
};

ViewUser.atUsernameClick = function (EventLaunched) {
  if ($(this.extUsernamePanel.dom).hasClassName(CLASS_ACTIVE))
    ViewUser.hideUsernamePanel();
  else
    ViewUser.showUsernamePanel();

  if (this.Units == null)
    CommandListener.throwCommand("loadunits()");

  Event.stop(EventLaunched);
};

ViewUser.showUsernamePanel = function () {
  $(this.extUsernamePanel.dom).addClassName(CLASS_ACTIVE);
  Ext.get(document.body).on("click", ViewUser.hideUsernamePanel, ViewUser);
};

ViewUser.hideUsernamePanel = function () {
  $(this.extUsernamePanel.dom).removeClassName(CLASS_ACTIVE);
  Ext.get(document.body).un("click", ViewUser.hideUsernamePanel, ViewUser);
};
