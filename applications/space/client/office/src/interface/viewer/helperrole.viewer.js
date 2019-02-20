ViewerHelperRole = new Object;
ViewerHelperRole.extLayer = null;

ViewerHelperRole.init = function (extLayer) {
  ViewerHelperRole.extLayer = extLayer;
  ViewerHelperRole.extLayer.dom.innerHTML = translate(AppTemplate.ViewerHelperRole, Lang.ViewerHelperRole);

  var extExpires = ViewerHelperRole.extLayer.select(".field.expiredate .expires").first();
  extExpires.on("change", ViewerHelperRole.atExpiresChange.bind(this, extExpires.dom));

  this.initType();
  this.initDefinitionTypes();
  this.initUsers();
  this.initPartners();
  this.initDatePickers();
  this.initToolbar();
};

ViewerHelperRole.initType = function () {
  var extTypeList = ViewerHelperRole.extLayer.select(".field.type .component .type");
  extTypeList.each(function (extType) {
    extType.on("change", ViewerHelperRole.atTypeChange, ViewerHelperRole);
  }, this);
};

ViewerHelperRole.initDefinitionTypes = function (Target) {
  var extSelector = ViewerHelperRole.extLayer.select(".field.definitiontype .component").first();
  extSelector.on("change", ViewerHelperRole.atDefinitionTypeChange.bind(this));
  extSelector.on("focus", ViewerHelperRole.atDefinitionTypeFocus.bind(this));
};

ViewerHelperRole.initUsers = function () {

  var Options = new Object();
  Options.DataSource = { Remote: true };
  Options.itemsPerPage = 6;
  Options.selfScroller = true;
  Options.Templates = new Object();
  Options.Templates.Item = "&lt;div class='label'&gt;\#\{label\}&lt;/div&gt;&lt;div class='body email'&gt;\#\{email\}&lt;/div&gt;";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;" + Lang.ViewerHelperRole.NoUsers + "&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} " + Lang.ViewerHelperRole.Users;

  this.usersViewer = new CGListViewer(Options, Context.Config.Language, Context.Config.ImagesPath);
  this.usersViewer.setWizardLayer(null);
  this.usersViewer.layerId = Ext.id();
  this.usersViewer.setDataSourceUrls(Kernel.getLoadFederationUsersLink(), null);
  this.usersViewer.onBoundItem = ViewerHelperRole.atBoundUser.bind(this);
  this.usersViewer.onShowItem = ViewerHelperRole.atSelectUser.bind(this);

  ViewerHelperRole.extLayer.select(".dialog.users .usersbox").first().dom.id = this.usersViewer.layerId;
};

ViewerHelperRole.initPartners = function () {
  this.initServices();
  this.initFeeders();
};

ViewerHelperRole.initServices = function () {
  var Options = new Object();
  Options.DataSource = { Remote: false };
  Options.itemsPerPage = 6;
  Options.selfScroller = true;
  Options.Templates = new Object();
  Options.Templates.Item = "&lt;div class='label'&gt;\#\{label\}&lt;/div&gt;&lt;div class='body'&gt;&lt;/div&gt;";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;" + Lang.ViewerHelperRole.NoServices + "&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} " + Lang.ViewerHelperRole.Services;

  this.servicesViewer = new CGListViewer(Options, Context.Config.Language, Context.Config.ImagesPath);
  this.servicesViewer.setWizardLayer(null);
  this.servicesViewer.layerId = Ext.id();
  this.servicesViewer.Options.DataSource.Items = { rows: [], nrows: 0 };
  this.servicesViewer.Options.DataSource.Groups = [];
  this.servicesViewer.State.CurrentPage = 1;
  this.servicesViewer.onShowItem = ViewerHelperRole.atSelectPartnerService.bind(this);

  ViewerHelperRole.extLayer.select(".dialog.services .servicesbox").first().dom.id = this.servicesViewer.layerId;
};

ViewerHelperRole.initFeeders = function () {
  var Options = new Object();
  Options.DataSource = { Remote: false };
  Options.itemsPerPage = 6;
  Options.selfScroller = true;
  Options.Templates = new Object();
  Options.Templates.Item = "&lt;div class='label'&gt;\#\{label\}&lt;/div&gt;&lt;div class='body'&gt;&lt;/div&gt;";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;" + Lang.ViewerHelperRole.NoFeeders + "&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} " + Lang.ViewerHelperRole.Feeders;

  this.feedersViewer = new CGListViewer(Options, Context.Config.Language, Context.Config.ImagesPath);
  this.feedersViewer.setWizardLayer(null);
  this.feedersViewer.layerId = Ext.id();
  this.feedersViewer.Options.DataSource.Items = { rows: [], nrows: 0 };
  this.feedersViewer.Options.DataSource.Groups = [];
  this.feedersViewer.State.CurrentPage = 1;
  this.feedersViewer.onShowItem = ViewerHelperRole.atSelectPartnerFeeder.bind(this);

  ViewerHelperRole.extLayer.select(".dialog.feeders .feedersbox").first().dom.id = this.feedersViewer.layerId;
};

ViewerHelperRole.initDatePickers = function (Target) {

  this.beginPicker = new DatePicker();
  this.beginPicker.onChange = ViewerHelperRole.atBeginDateChange.bind(this);
  this.beginPicker.init(ViewerHelperRole.extLayer.select(".field.begindate .picker").first(), Context.Config.Language);
  this.beginPicker.setDate(new Date());
  this.beginPicker.setPrecision(DATE_PRECISION_SECONDS);
  this.beginPicker.refresh();
  this.beginPicker.hide();

  var extBeginComponent = ViewerHelperRole.extLayer.select(".field.begindate .component").first();
  extBeginComponent.on("click", ViewerHelperRole.atBeginDateClick.bind(this, extBeginComponent));
  extBeginComponent.on("blur", ViewerHelperRole.atBeginDateBlur.bind(this));

  this.expirePicker = new DatePicker();
  this.expirePicker.onChange = ViewerHelperRole.atExpireDateChange.bind(this);
  this.expirePicker.init(ViewerHelperRole.extLayer.select(".field.expiredate .picker").first(), Context.Config.Language);
  this.expirePicker.setDate(new Date());
  this.expirePicker.setPrecision(DATE_PRECISION_SECONDS);
  this.expirePicker.refresh();
  this.expirePicker.hide();

  var extExpiresComponent = ViewerHelperRole.extLayer.select(".field.expiredate .component").first();
  extExpiresComponent.on("click", ViewerHelperRole.atExpireDateClick.bind(this, extExpiresComponent));
  extExpiresComponent.on("blur", ViewerHelperRole.atExpireDateBlur.bind(this));
};

ViewerHelperRole.initToolbar = function (Target) {

  var extAddButton = ViewerHelperRole.extLayer.select(".toolbar .add");
  extAddButton.on("click", ViewerHelperRole.atAddButtonClick.bind(this));

  var extAddOtherButton = ViewerHelperRole.extLayer.select(".toolbar .addother");
  extAddOtherButton.on("click", ViewerHelperRole.atAddOtherButtonClick.bind(this));

  var extSaveButton = ViewerHelperRole.extLayer.select(".toolbar .save");
  extSaveButton.on("click", ViewerHelperRole.atSaveButtonClick.bind(this));
};

ViewerHelperRole.setTarget = function (Target) {
  this.Target = Target;
};

ViewerHelperRole.show = function () {
  ViewerHelperRole.extLayer.dom.style.display = "block";
};

ViewerHelperRole.hide = function () {
  ViewerHelperRole.extLayer.dom.style.display = "none";
};

ViewerHelperRole.refreshHeader = function () {
  var extDialog = ViewerHelperRole.extLayer.select(".dialog").first();
  var extTitle = extDialog.select(".title").first();
  var extSubtitle = extDialog.select(".subtitle").first();
  var extStates = extDialog.select(".states").first();
  var extDates = extDialog.select(".dates").first();

  if (this.Target.View == "save") {
    var extSubtitleSpan = extSubtitle.select("span").first();
    extTitle.dom.innerHTML = Lang.ViewerHelperRole.Save;
    extSubtitleSpan.dom.innerHTML = this.Role.sLabel;
    extSubtitle.dom.style.display = "block";
    extStates.dom.style.display = "inline";
    extDates.dom.style.display = "block";

    extDialog.removeClass("expires_true");
    extDialog.removeClass("expires_false");
    extDialog.removeClass("expired_true");
    extDialog.removeClass("expired_false");
    extDialog.removeClass("began_true");
    extDialog.removeClass("began_false");
    extDialog.addClass(this.Role.expires ? "expires_true" : "expires_false");
    extDialog.addClass(this.Role.expired ? "expired_true" : "expired_false");
    extDialog.addClass(this.Role.began ? "began_true" : "began_false");

    var formattedBeginDate = getFormattedDateTime(this.Role.dtBeginDate, Context.Config.Language, false);
    var formattedExpireDate = getFormattedDateTime(this.Role.dtExpireDate, Context.Config.Language, false);
    extDialog.select(".dates .date.pending").first().dom.innerHTML = Lang.ViewerHelperRole.DatePending.replace("\#\{formattedBeginDate\}", formattedBeginDate);
    extDialog.select(".dates .date.active").first().dom.innerHTML = Lang.ViewerHelperRole.DateActive.replace("\#\{formattedBeginDate\}", formattedBeginDate);
    extDialog.select(".dates .date.expires").first().dom.innerHTML = Lang.ViewerHelperRole.DateExpires.replace("\#\{formattedExpireDate\}", formattedExpireDate);
    extDialog.select(".dates .date.expired.begin").first().dom.innerHTML = Lang.ViewerHelperRole.DateExpiredBegin.replace("\#\{formattedBeginDate\}", formattedBeginDate);
    extDialog.select(".dates .date.expired.end").first().dom.innerHTML = Lang.ViewerHelperRole.DateExpiredEnd.replace("\#\{formattedExpireDate\}", formattedExpireDate);
  }
  else {
    extStates.dom.style.display = "none";
    extDates.dom.style.display = "none";

    extTitle.dom.innerHTML = Lang.ViewerHelperRole.Add;
    extSubtitle.dom.style.display = "none";
  }
};

ViewerHelperRole.enableUser = function () {
  Ext.get("role_type_user").dom.checked = true;
  ViewerHelperRole.extLayer.select(".dialog.users").first().dom.style.display = "block";
};

ViewerHelperRole.enableService = function () {
  Ext.get("role_type_service").dom.checked = true;
  ViewerHelperRole.extLayer.select(".dialog.services").first().dom.style.display = "block";
};

ViewerHelperRole.enableFeeder = function () {
  Ext.get("role_type_feeder").dom.checked = true;
  ViewerHelperRole.extLayer.select(".dialog.feeders").first().dom.style.display = "block";
};

ViewerHelperRole.disableUser = function () {
  Ext.get("role_type_user").dom.checked = false;
  ViewerHelperRole.extLayer.select(".dialog.users").first().dom.style.display = "none";
};

ViewerHelperRole.disableService = function () {
  Ext.get("role_type_service").dom.checked = false;
  ViewerHelperRole.extLayer.select(".dialog.services").first().dom.style.display = "none";
};

ViewerHelperRole.disableFeeder = function () {
  Ext.get("role_type_feeder").dom.checked = false;
  ViewerHelperRole.extLayer.select(".dialog.feeders").first().dom.style.display = "none";
};

ViewerHelperRole.refreshType = function () {
  var Type = this.Role.Type;

  this.disableUser();
  this.disableService();
  this.disableFeeder();

  if (Type == "user") this.enableUser();
  else if (Type == "service") this.enableService();
  else if (Type == "feeder") this.enableFeeder();
};

ViewerHelperRole.refreshDefinitionTypes = function () {
  var extSelector = ViewerHelperRole.extLayer.select(".field.definitiontype .component").first();
  var extSubtitle = ViewerHelperRole.extLayer.select(".subtitle .definitiontype").first();
  var rows = State.RoleDefinitionList.rows;

  extSelector.dom.innerHTML = "";
  for (var i = 0; i < rows.length; i++) {
    var Role = rows[i];

    if (this.Role.Code == Role.code) extSubtitle.dom.innerHTML = Role.label;

    var DOMOption = document.createElement("option");
    DOMOption.value = Role.code;
    DOMOption.selected = (this.Role.Code == Role.code);
    DOMOption.text = Role.label;
    extSelector.dom.appendChild(DOMOption);
  }

  if (this.Target.View != "save") extSelector.removeClass("readonly");
  else extSelector.addClass("readonly");
};

ViewerHelperRole.refreshUsers = function () {
  var enableUsers = this.Target.Definition.enableUsers;

  Ext.get("role_type_user").dom.style.display = (enableUsers) ? "inline" : "none";
  Ext.get("role_type_user_label").dom.style.display = (enableUsers) ? "inline" : "none";

  if (!enableUsers)
    return;

  this.usersViewer.State.CurrentPage = 1;
  this.usersViewer.render(this.usersViewer.layerId);
};

ViewerHelperRole.refreshPartners = function () {
  var enableServices = this.Target.Definition.PartnerServiceOntologies != null;
  var enableFeeders = this.Target.Definition.PartnerFeederOntologies != null;

  Ext.get("role_type_service").dom.style.display = (enableServices) ? "inline" : "none";
  Ext.get("role_type_service_label").dom.style.display = (enableServices) ? "inline" : "none";

  Ext.get("role_type_feeder").dom.style.display = (enableFeeders) ? "inline" : "none";
  Ext.get("role_type_feeder_label").dom.style.display = (enableFeeders) ? "inline" : "none";

  if (!enableServices && !enableFeeders)
    return;

  var serviceGroups = feederGroups = new Object();
  var serviceItems = feederItems = new Array();
  serviceItems.rows = feederItems.rows = new Array();
  serviceItems.nrows = feederItems.nrows = 0;

  for (var i = 0; i < this.Target.PartnerList.items.length; i++) {
    var partner = this.Target.PartnerList.items[i];
    ViewerHelperRole.refreshServices(partner, arrayToMap(this.Target.Definition.PartnerServiceOntologies), serviceItems, serviceGroups);
    ViewerHelperRole.refreshFeeders(partner, arrayToMap(this.Target.Definition.PartnerFeederOntologies), feederItems, feederGroups);
  }

  this.servicesViewer.Options.DataSource.Items = serviceItems;
  this.servicesViewer.Options.DataSource.Groups = serviceGroups;
  this.servicesViewer.State.CurrentPage = 1;
  this.servicesViewer.render(this.servicesViewer.layerId);

  this.feedersViewer.Options.DataSource.Items = feederItems;
  this.feedersViewer.Options.DataSource.Groups = feederGroups;
  this.feedersViewer.State.CurrentPage = 1;
  this.feedersViewer.render(this.feedersViewer.layerId);
};

ViewerHelperRole.refreshServices = function (partner, ontologies, items, groups) {
  if (ontologies == null) return;

  for (var j = 0; j < partner.services.items.length; j++) {
    var service = partner.services.items[j];
    if (ontologies[service.ontology] == null) continue;
    items.rows.push(service);
    items.nrows++;
    service.group = partner.id;
    if (groups[partner.id] == null)
      groups[partner.id] = partner;
  }
};

ViewerHelperRole.refreshFeeders = function (partner, ontologies, items, groups) {
  if (ontologies == null) return;

  for (var j = 0; j < partner.feeders.items.length; j++) {
    var feeder = partner.feeders.items[j];
    if (ontologies[feeder.ontology] == null) continue;
    items.rows.push(feeder);
    items.nrows++;
    feeder.group = partner.id;
    if (groups[partner.id] == null)
      Groups[partner.id] = partner;
  }
};

ViewerHelperRole.refreshExpireDateStatus = function (expires) {
  var extExpireDate = ViewerHelperRole.extLayer.select(".field.expiredate .component").first();

  if (expires) extExpireDate.dom.style.display = "block";
  else extExpireDate.dom.style.display = "none";
};

ViewerHelperRole.refreshDatePickers = function () {
  var extBeginDate = ViewerHelperRole.extLayer.select(".field.begindate .component").first();
  var extExpires = ViewerHelperRole.extLayer.select(".field.expiredate .expires").first();
  var extExpireDate = ViewerHelperRole.extLayer.select(".field.expiredate .component").first();
  var currentTime = (new Date()).getTime();

  if (this.Role.dtBeginDate.getTime() <= currentTime) extBeginDate.addClass("readonly");
  else extBeginDate.removeClass("readonly");

  if (this.Role.dtExpireDate != null && this.Role.dtExpireDate.getTime() <= currentTime) {
    extExpires.dom.disabled = true;
    extExpireDate.addClass("readonly");
  }
  else {
    extExpires.dom.disabled = false;
    extExpireDate.removeClass("readonly");
  }

  this.beginPicker.setDate(this.Role.dtBeginDate);
  extBeginDate.dom.value = getFormattedDateTime(this.Role.dtBeginDate, Context.Config.Language, true, true, true);

  this.expirePicker.setRange(this.Role.dtBeginDate, null);
  extExpires.dom.checked = this.Role.dtExpireDate != null;
  if (this.Role.dtExpireDate != null) {
    extExpireDate.dom.value = this.Role.dtExpireDate != null ? getFormattedDateTime(this.Role.dtExpireDate, Context.Config.Language, true, true, true) : "";
    this.expirePicker.setDate(this.Role.dtExpireDate);
  }
  else extExpireDate.dom.value = "";

  this.refreshExpireDateStatus(extExpires.dom.checked);
};

ViewerHelperRole.refreshHistoryRoles = function () {
  var extHistoryRoles = ViewerHelperRole.extLayer.select(".historyroles").first();
  var extHistoryRolesItems = ViewerHelperRole.extLayer.select(".historyroles .historyitems").first();

  extHistoryRoles.dom.style.display = (this.Target.View == "add") ? "none" : "";
  if (this.Target.View == "add") return;

  if (this.historyViewer == null) {
    this.historyViewer = new CGListViewer(this.Target.HistoryOptions, Context.Config.Language, Context.Config.ImagesPath);
    this.historyViewer.setWizardLayer(null);
    this.historyViewer.onBoundItem = ViewerHelperRole.atBoundHistoryRole;
  }

  if (extHistoryRolesItems.dom.id == null || extHistoryRolesItems.dom.id == "") extHistoryRolesItems.dom.id = Ext.id();

  this.historyViewer.setDataSourceUrls(Kernel.getLoadGroupedRoleListLink(this.Target.Definition.Code, this.Role.GroupedId), null);
  this.historyViewer.render(extHistoryRolesItems.dom.id);
};

ViewerHelperRole.refreshToolbar = function () {
  var extAddButton = ViewerHelperRole.extLayer.select(".toolbar .button.add").first();
  var extAddOtherButton = ViewerHelperRole.extLayer.select(".toolbar .button.addother").first();
  var extSaveButton = ViewerHelperRole.extLayer.select(".toolbar .button.save").first();

  extAddButton.dom.style.display = (this.Target.View != "save" && !this.Role.expired) ? "block" : "none";
  extAddOtherButton.dom.style.display = (this.Role.expired) ? "block" : "none";
  extSaveButton.dom.style.display = (this.Target.View == "save" && !this.Role.expired) ? "block" : "none";
};

ViewerHelperRole.refresh = function () {
  var extMessageList = ViewerHelperRole.extLayer.select(".message");

  if (this.Target == null) {
    ViewerHelperRole.extLayer.dom.style.display = "none";
    return;
  }

  if (this.Target.Role != null) this.Role = this.Target.Role;
  else {
    var Definition = this.Target.Definition;
    this.Role = new CGRole();
    this.Role.Code = this.Target.Definition.Code;
    if (this.Target.RoleType != null) this.Role.Type = this.Target.RoleType;
    else this.Role.Type = Definition.getType();
  }

  var dtBeginDate = this.Role.dtBeginDate;
  var dtExpireDate = (this.Target.View == "save") ? this.Role.dtExpireDate : null;
  this.Role.dtBeginDate = dtBeginDate;
  this.Role.dtExpireDate = dtExpireDate;

  this.beginPicker.hide();
  this.expirePicker.hide();

  ViewerHelperRole.extLayer.dom.style.display = "block";

  var extDialog = ViewerHelperRole.extLayer.select(".dialog").first();
  extDialog.removeClass("add");
  extDialog.removeClass("save");
  extDialog.addClass(this.Target.View);

  this.refreshHeader();
  this.refreshDefinitionTypes();
  this.refreshType();
  this.refreshUsers();
  this.refreshPartners();
  this.refreshDatePickers();
  this.refreshHistoryRoles();
  this.refreshToolbar();

  extMessageList.each(function (extMessage) {
    extMessage.dom.style.display = "none";
  }, this);
};

ViewerHelperRole.getType = function () {
  var extTypeList = ViewerHelperRole.extLayer.select(".field.type .component .type");
  var Type = "";

  extTypeList.each(function (extType) {
    if (extType.dom.checked) Type = extType.dom.value;
  }, this);

  return Type;
};

ViewerHelperRole.calculateExpireDate = function () {
  var dtBeginDate = this.Role.dtBeginDate;
  var extSelectExpireDate = ViewerHelperRole.extLayer.select(".message.expire").first();

  if (dtBeginDate == null)
    return;

  extSelectExpireDate.dom.style.display = (this.Role.dtExpireDate != null && this.Role.dtExpireDate.getTime() < dtBeginDate.getTime()) ? "block" : "none";
};

ViewerHelperRole.hideBeginPicker = function () {
  this.beginPicker.hide();
};

ViewerHelperRole.hideExpirePicker = function () {
  this.expirePicker.hide();
};

ViewerHelperRole.add = function () {
  var hasErrors = false;
  var command = "";
  var beginDate = toServerDate(this.Role.dtBeginDate);
  var expireDate = toServerDate(this.Role.dtExpireDate);

  var Type = this.getType();
  if (Type == "user") {
    command = "adduserrole(" + this.Role.Code + "," + this.Role.User.getId() + "," + this.Role.User.getName() + "," + beginDate + "," + expireDate + ")";

    var extSelectUser = ViewerHelperRole.extLayer.select(".message.user").first();
    extSelectUser.dom.style.display = "none";
    if (this.Role.User.getId() == null) {
      extSelectUser.dom.style.display = "block";
      hasErrors = true;
    }
  }
  else if (Type == "service") {
    command = "addservicerole(" + this.Role.Code + "," + this.Role.PartnerId + "," + this.Role.PartnerServiceName + "," + beginDate + "," + expireDate + ")";

    var extSelectService = ViewerHelperRole.extLayer.select(".message.service").first();
    extSelectService.dom.style.display = "none";
    if (this.Role.PartnerId == null || this.Role.PartnerServiceName == null) {
      extSelectService.dom.style.display = "block";
      hasErrors = true;
    }
  }
  else {
    command = "addfeederrole(" + this.Role.Code + "," + this.Role.PartnerId + "," + this.Role.PartnerServiceName + "," + beginDate + "," + expireDate + ")";

    var extSelectFeeder = ViewerHelperRole.extLayer.select(".message.feeder").first();
    extSelectFeeder.dom.style.display = "none";
    if (this.Role.PartnerId == null || this.Role.PartnerServiceName == null) {
      extSelectFeeder.dom.style.display = "block";
      hasErrors = true;
    }
  }

  var extSelectBeginDate = ViewerHelperRole.extLayer.select(".message.begin").first();
  if (extSelectBeginDate.dom.style.display != "none") hasErrors = true;

  var extSelectExpireDate = ViewerHelperRole.extLayer.select(".message.expire").first();
  if (extSelectExpireDate.dom.style.display != "none") hasErrors = true;

  if (hasErrors) return;

  this.Role.dtBeginDate.setSeconds(-1);
  this.Role.dtBeginDate.setMinutes(-1);
  this.Role.dtBeginDate.setHours(-1);
  this.Role.dtBeginDate.setDate(this.Role.dtBeginDate.getDate() + 1);

  CommandListener.throwCommand(command);
};

ViewerHelperRole.save = function () {
  if (this.Role.User.getId() == "undefined") return;

  var command = "";
  var Type = this.Role.Type;
  var beginDate = toServerDate(this.Role.dtBeginDate);
  var expireDate = toServerDate(this.Role.dtExpireDate);

  if (Type == "user") {
    command = "saveuserrole";
    CommandListener.throwCommand(command + "(" + this.Role.Id + "," + this.Role.User.getId() + "," + beginDate + "," + expireDate + ")");
  }
  else {
    if (Type == "service") command = "saveservicerole";
    else command = "savefeederrole";
    CommandListener.throwCommand(command + "(" + this.Role.Id + "," + this.Role.PartnerId + "," + this.Role.PartnerServiceName + "," + beginDate + "," + expireDate + ")");
  }
};

ViewerHelperRole.atBoundHistoryRole = function (Sender, Item) {
  if (Item.beginDate) Item.formattedBeginDate = getFormattedDateTime(parseServerDate(Item.beginDate), Context.Config.Language, false);
  if (Item.expireDate) Item.formattedExpireDate = getFormattedDateTime(parseServerDate(Item.expireDate), Context.Config.Language, false);
  else Item.formattedExpireDate = "";
};

ViewerHelperRole.atSearchResultChange = function (record) {
  var extSelectUser = ViewerHelperRole.extLayer.select(".message.user").first();

  extSelectUser.dom.style.display = "none";
  this.Role.User.setId(record.data.id);

  return true;
};

ViewerHelperRole.atBeginDateChange = function (record) {
  var extBeginDate = ViewerHelperRole.extLayer.select(".dialog .field.begindate .component").first();
  var extSelectBeginDate = ViewerHelperRole.extLayer.select(".message.begin").first();

  if (this.beginPickerTimeout) window.clearTimeout(this.beginPickerTimeout);
  extBeginDate.dom.focus();

  extSelectBeginDate.dom.style.display = "none";

  this.Role.dtBeginDate = this.beginPicker.getDate();
  extBeginDate.dom.value = getFormattedDateTime(this.Role.dtBeginDate, Context.Config.Language, true, true, true);

  this.calculateExpireDate();

  this.expirePicker.setRange(this.Role.dtBeginDate, null);
  this.expirePicker.refresh();
};

ViewerHelperRole.atExpireDateChange = function (record) {
  var extExpireDate = ViewerHelperRole.extLayer.select(".field.expiredate .component").first();
  var extSelectExpireDate = ViewerHelperRole.extLayer.select(".message.expire").first();

  if (this.expirePickerTimeout) window.clearTimeout(this.expirePickerTimeout);
  extExpireDate.dom.focus();

  this.Role.dtExpireDate = this.expirePicker.getDate();
  extSelectExpireDate.dom.style.display = (this.Role.dtExpireDate.getTime() < this.Role.dtBeginDate) ? "block" : "none";
  extExpireDate.dom.value = getFormattedDateTime(this.Role.dtExpireDate, Context.Config.Language, true, true, true);
};

ViewerHelperRole.atBeginDateClick = function (extComponent) {
  if (extComponent.hasClass("readonly")) return;
  if (this.beginPickerTimeout) window.clearTimeout(this.beginPickerTimeout);
  if (this.beginPicker.isVisible()) window.setTimeout(ViewerHelperRole.hideBeginPicker.bind(this), 150);
  else this.beginPicker.show();
};

ViewerHelperRole.atBeginDateBlur = function () {
  this.beginPickerTimeout = window.setTimeout(ViewerHelperRole.hideBeginPicker.bind(this), 150);
};

ViewerHelperRole.atExpireDateClick = function (extComponent) {
  if (extComponent.hasClass("readonly")) return;
  if (this.expirePickerTimeout) window.clearTimeout(this.expirePickerTimeout);
  this.expirePicker.setDate(this.Role.dtExpireDate);
  this.expirePicker.refresh();
  if (this.expirePicker.isVisible()) window.setTimeout(ViewerHelperRole.hideExpirePicker.bind(this), 150);
  else this.expirePicker.show();
};

ViewerHelperRole.atExpireDateBlur = function () {
  this.expirePickerTimeout = window.setTimeout(ViewerHelperRole.hideExpirePicker.bind(this), 150);
};

ViewerHelperRole.atExpiresChange = function (DOMExpires) {

  if (DOMExpires.checked)
    this.calculateExpireDate();
  else
    this.Role.dtExpireDate = null;

  this.refreshExpireDateStatus(DOMExpires.checked);
};

ViewerHelperRole.atAddOtherButtonClick = function () {
  var Role = new CGRole();
  Role.clone(this.Role);
  this.Role = Role;
  this.add();
};

ViewerHelperRole.atAddButtonClick = function () {
  this.add();
};

ViewerHelperRole.atSaveButtonClick = function () {
  this.save();
};

ViewerHelperRole.atDefinitionTypeChange = function () {
  var DOMDefinitionType = ViewerHelperRole.extLayer.select(".field.definitiontype .component").first().dom;
  this.Role.Code = DOMDefinitionType.options[DOMDefinitionType.selectedIndex].value;
};

ViewerHelperRole.atDefinitionTypeFocus = function () {
  var DOMDefinitionType = ViewerHelperRole.extLayer.select(".field.definitiontype .component").first().dom;

  if (this.Target.View == "save")
    DOMDefinitionType.blur();
};

ViewerHelperRole.atTypeChange = function () {
  this.Role.Type = this.getType();
  this.refreshType();
};

ViewerHelperRole.atBoundUser = function (listViewer, item) {
  if (item.email == "") item.email = Lang.ViewerHelperRole.EmailNotDefined;
};

ViewerHelperRole.atSelectUser = function (listViewer, item) {
  this.Role.User.setId(item.id);
  this.Role.User.setName(item.name);
};

ViewerHelperRole.atSelectPartnerService = function (listViewer, item) {
  this.Role.PartnerId = item.group;
  this.Role.PartnerServiceName = item.name;
};

ViewerHelperRole.atSelectPartnerFeeder = function (listViewer, item) {
  this.Role.PartnerId = item.group;
  this.Role.PartnerServiceName = item.name;
};