View = function() {
  this.DOMLayer = null;
  this.state = null;
};

View.colors = new Object();
View.accountView = null;
View.colorsIndex = -1;

View.ACCOUNT = "::id::_account";
View.DASHBOARDS = "::id::_dashboardlist";
View.DASHBOARD = "::id::_dashboard";
View.DASHBOARD_INFO = "::id::_dashboardinfo";
View.INDICATORS = "::id::_indicators";
View.MEASURE_UNITS = "::id::_measureunits";
View.COMPARE = "::id::_compare";
View.INDICATOR = "::id::_indicator";
View.TAXONOMIES = "::id::_taxonomies";
View.CHART = "::id::_chart_::type::";

View.ColorContext = new Object();
View.ColorContext.INDICATORS = "indicators";
View.ColorContext.COMPARE = "compare";

View.prototype.initTitles = function(DOMLayer) {
  var jLayer = $(this.DOMLayer);
  var jBlocks = jLayer.find(".block");
  var hasBlocks = jBlocks.length > 0;
  
  if (!hasBlocks) {
    jLayer.find("label:first").click(View.prototype.atTitleClick.bind(this));
    return;
  }
  
  for (var i=0; i<jBlocks.length; i++) {
    var jBlock = $(jBlocks[i]);
    jBlock.find("label:first").click(View.prototype.atTitleClick.bind(this, jBlock));
  }
};

View.prototype.init = function(DOMLayer) {
  this.DOMLayer = DOMLayer;
  this.initTitle();
};

View.prototype.initHeader = function() {
  var jHeader = $(this.DOMLayer).find(".header");
  
  var DOMFederationImage = jHeader.find(".federation.image").get(0);
  DOMFederationImage.src = Context.Config.FederationLogoUrl;
  DOMFederationImage.title = Context.Config.FederationLabel;
  DOMFederationImage.alt = Context.Config.FederationLabel;
  
  var DOMSpaceLabel = jHeader.find(".subtitle").get(0);
  DOMSpaceLabel.innerHTML = Context.Config.SpaceLabel;
  
  var DOMModelLabel = jHeader.find(".title").get(0);
  DOMModelLabel.innerHTML = Context.Config.ModelLabel;
  
  CommandListener.capture(jHeader.get(0));
  
  this.initAccountView();
};

View.prototype.initAccountView = function() {
  var jHeader = $(this.DOMLayer).find(".header");
  var jUserView = jHeader.find(".accountview");
  
  if (jUserView == null)
    return;
  
  if (this.accountView == null) {
    this.accountView = new ViewAccount();
    this.accountView.id = View.ACCOUNT.replace("::id::", State.account.id);
    this.accountView.target = State.account;
    this.accountView.init(jUserView.get(0));
  }
};

View.prototype.enableColors = function() {
  $(this.DOMLayer).removeClass("colorsoff");
};

View.prototype.disableColors = function() {
  $(this.DOMLayer).addClass("colorsoff");
};

View.getColor = function(context, id) {
  if (!View.colors[context]) return null;
  return View.colors[context][id];
};

View.freeColors = function(context) {
  View.colors[context] = new Object();
};

View.freeColor = function(context, id) {
  if (!View.colors[context]) return;
  delete View.colors[context][id];
};

View.getColorsIndex = function() {
  View.colorsIndex++;
  return View.colorsIndex;
};

View.retainColor = function(context, id) {
  if (!View.colors[context]) View.colors[context] = new Object();
  View.colors[context][id] = GraphUtil.getColor(View.getColorsIndex());
  return View.colors[context][id];
};

View.setColor = function(context, id, color) {
  if (!View.colors[context]) View.colors[context] = new Object();
  View.colors[context][id] = color;
};

View.setColors = function(colors) {
  View.colors = colors;
};

View.prototype.show = function() {
  if (!this.DOMLayer) return;
  $(this.DOMLayer).show();
};

View.prototype.hide = function() {
  if (!this.DOMLayer) return;
  $(this.DOMLayer).hide();
};

View.prototype.setState = function(state) {
  this.state = state;
};

View.prototype.refresh = function() {
  this.refreshHeader();
};

View.prototype.refreshHeader = function() {
  if (this.accountView != null)
    this.accountView.refresh();
};

View.prototype.destroy = function() {
  $(this.DOMLayer).remove();
};

View.prototype.getRange = function(timeLapse, freeTimeLapseWindowSize) {
  if (timeLapse == null) timeLapse = this.state.timeLapse;
  var bounds = TimeLapse.getBounds(timeLapse, this.target.dashboard.model.range);
  var windowSize = TimeLapse.getWindowSize(timeLapse, bounds, freeTimeLapseWindowSize);
  return TimeLapse.getValues(timeLapse, windowSize, bounds);
};

View.prototype.toggleColorsDialog = function(context, id, DOMLocation) {
  if (this.colourPicker == null || !this.colourPicker.isVisible())
    this.showColorsDialog(context, id, DOMLocation);
  else
    this.hideColorsDialog();
};

View.prototype.showColorsDialog = function(context, id, DOMLocation) {
  
  if (this.jColorPicker == null) {
    var jLayer = $('#jquery-colour-picker-example select');
    this.colourPicker = jLayer.colourPicker({});
    this.colourPicker.onSelect = View.prototype.atChangeColor.bind(this, context, id);
  }
  
  var jLocation = $(DOMLocation);
  var pos = jLocation.offset();
  this.colourPicker.render(pos.left-jLocation.width(), pos.top);
};

View.prototype.hideColorsDialog = function() {
  this.colourPicker.close();
};

View.prototype.toggleView = function(jBlock) {
};

View.prototype.onResize = function() {
};

View.prototype.atChangeColor = function(context, id, color) {
  this.updateColor(context, id, color);
};

View.prototype.atTitleClick = function(jBlock) {
  this.toggleView(jBlock);
};