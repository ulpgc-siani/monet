Kernel = new Object;

Kernel.init = function() {
  Kernel.mode = (Context.Config.EncriptData == "true")?true:false;
  Kernel.stub = new StubAjax(Kernel.mode);
};

Kernel.loadAccount = function(action) {
  Kernel.stub.request(action, "loadaccount", { language: Context.Config.Language });
};

Kernel.saveAccount = function(Action, account) {
  var data = Serializer.serializeAccount(account);
  Kernel.stub.request(Action, "saveaccount", {data: escape(utf8Encode(data))});
};

Kernel.logout = function(Action, instanceId) {
  Kernel.stub.request(Action, "logout", {i: instanceId});
};

Kernel.loadDashboardList = function(action) {
  Kernel.stub.request(action, "loaddashboardlist", { language: Context.Config.Language });
};

Kernel.loadDashboard = function(action, id, indicatorList, reportId) {
  var parameters = new Object();
  parameters.id = id;
  parameters.language = Context.Config.Language;
  parameters.report = reportId;
  
  if (indicatorList != null) 
    parameters.indicators = escape(utf8Encode(indicatorList.toJson()));
  
  Kernel.stub.request(action, "loaddashboard", parameters);
};

Kernel.getLoadCategoriesUrl = function(dashboardName, taxonomyId, parentCategoryId) {

  var parentParemeter = "";
  if (parentCategoryId != null)
    parentParemeter = "&category=" + escape(utf8Encode(parentCategoryId));

  var parameters = "op=loadcategories&id=" + taxonomyId + "&dashboard=" + dashboardName + parentParemeter + "&language=" + Context.Config.Language;
  return Context.Config.Url + writeServerRequest(Kernel.mode, parameters);
};

Kernel.loadChartApi = function(action, type) {
  Kernel.stub.request(action, "loadchartapi", { type : type, language : Context.Config.Language });
};

Kernel.getChartUrl = function(dashboard, type, indicatorList, compareList, filterList, rangeList, from, to, scale) {
  var parameters = "op=loadchart&dashboard=" + dashboard + "&type=" + type;
  parameters += "&indicators=" + escape(utf8Encode(indicatorList.toJson()));
  parameters += "&compare=" + escape(utf8Encode(compareList.toJson()));
  parameters += "&filters=" + escape(utf8Encode(filterList.toJson()));
  parameters += "&ranges=" + escape(utf8Encode(rangeList.toJson()));
  parameters += "&from=" + Date.timeToUTC(from);
  parameters += "&to=" + Date.timeToUTC(to);
  parameters += "&scale=" + scale;
  parameters += "&language=" + Context.Config.Language;
  return Context.Config.Url + writeServerRequest(Kernel.mode, parameters);
};

Kernel.getPrintDashboardUrl = function(dashboard, type, indicatorList, compareList, filterList, rangeList, from, to, scale, colors) {
  var parameters = "op=printdashboard&id=" + dashboard + "&type=" + type;
  parameters += "&indicators=" + escape(utf8Encode(indicatorList.toJson()));
  parameters += "&compare=" + escape(utf8Encode(compareList.toJson()));
  parameters += "&filters=" + escape(utf8Encode(filterList.toJson()));
  parameters += "&ranges=" + escape(utf8Encode(rangeList.toJson()));
  parameters += "&from=" + Date.timeToUTC(from);
  parameters += "&to=" + Date.timeToUTC(to);
  parameters += "&scale=" + scale;
  parameters += "&colors=" + escape(utf8Encode($.toJSON(colors)));
  parameters += "&language=" + Context.Config.Language;
  return Context.Config.Url + writeServerRequest(Kernel.mode, parameters);
};

Kernel.getDownloadDashboardUrl = function(dashboard, format, indicatorList, compareList, filterList, rangeList, from, to, scale) {
  var parameters = "op=downloaddashboard&id=" + dashboard + "&format=" + format;
  parameters += "&indicators=" + escape(utf8Encode(indicatorList.toJson()));
  parameters += "&compare=" + escape(utf8Encode(compareList.toJson()));
  parameters += "&filters=" + escape(utf8Encode(filterList.toJson()));
  parameters += "&ranges=" + escape(utf8Encode(rangeList.toJson()));
  parameters += "&from=" + Date.timeToUTC(from);
  parameters += "&to=" + Date.timeToUTC(to);
  parameters += "&scale=" + scale;
  parameters += "&language=" + Context.Config.Language;
  return Context.Config.Url + writeServerRequest(Kernel.mode, parameters);
};

Kernel.registerException = function(exception) {
  alert(exception.message);
};