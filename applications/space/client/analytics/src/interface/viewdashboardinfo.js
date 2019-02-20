ViewDashboardInfo = function () {
  this.base = View;
  this.base();
};

ViewDashboardInfo.prototype = new View;

ViewDashboardInfo.prototype.init = function(DOMLayer) {
  var jLayer = $(DOMLayer);

  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = translate(AppTemplate.viewdashboardinfo, Lang.ViewDashboardInfo);
  jLayer.addClass(CLASS_VIEW_DASHBOARD);
  
  if (!this.target) return;
  
  if (this.state.mode != null)
    jLayer.find(".view.dashboardinfo").addClass(this.state.mode);
  
  CommandListener.capture(this.DOMLayer);
};

ViewDashboardInfo.prototype.clearView = function() {
  var jLayer = $(this.DOMLayer); 
  jLayer.find(".title .timelapse").html("");
  jLayer.find(".title .indicator").html("");
  jLayer.find(".filters").html("");
};

ViewDashboardInfo.prototype.refreshTimeLapse = function() {
  var jLayer = $(this.DOMLayer);
  var free = this.target.freeTimeLapseWindowSize?this.target.freeTimeLapseWindowSize:false;
  var range = this.getRange(null, free);
  var scale = this.state.timeLapse.scale;
  var minDate = new Date(range.min).format(Lang.DateFormats[scale], Context.Config.Language);
  var maxDate = new Date(range.max).format(Lang.DateFormats[scale], Context.Config.Language);

  jLayer.find(".title .timelapse").html(minDate + "&nbsp;-&nbsp;" + maxDate);
};

ViewDashboardInfo.prototype.refreshIndicator = function() {
  var indicatorList = this.state.indicatorList;

  if (indicatorList.size() != 1) 
    return;
  
  var jLayer = $(this.DOMLayer);
  var keys = indicatorList.getKeys();
  var indicator = this.target.dashboard.model.indicatorMap[keys[0]];
  jLayer.find(".title .indicator").html("&nbsp;:&nbsp;" + indicator.label);
};

ViewDashboardInfo.prototype.refreshFilters = function() {
  var filterList = this.state.filterList;

  if (filterList.size() <= 0)
    return;
  
  var jLayer = $(this.DOMLayer);
  var filters = filterList.getAll();
  var filterTemplate = translate(AppTemplate.viewdashboardinfofilter, Lang.ViewDashboardInfo);
  var jFilters = jLayer.find(".filters");
  
  for (var i=0; i<filters.length; i++) {
    var jFilter = $.tmpl(filterTemplate, { label: filters[i].label, comma: (i!=filters.length-1?",&nbsp;":"")});
    var jLink = jFilter.find("a");

    jLink.click(ViewDashboardInfo.prototype.atDeleteFilterClick.bind(this, filters[i].id));
    jFilters.append(jFilter);
  }
};

ViewDashboardInfo.prototype.refresh = function() {
  
  this.clearView();
  
  var indicatorList = this.state.indicatorList;
  if (indicatorList.size() <= 0)
    return;
  
  this.refreshTimeLapse();
  this.refreshIndicator();
  this.refreshFilters();  
};

//************************************************************************************************************
ViewDashboardInfo.prototype.atDeleteFilterClick = function(id) {
  if (this.state.mode != State.Mode.PRINT)
    CommandListener.throwCommand("deletefilter(" + this.target.dashboard.code + "," + id + ")");
};