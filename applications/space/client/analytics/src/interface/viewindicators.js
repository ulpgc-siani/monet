ViewIndicators = function () {
  this.base = View;
  this.base();
  this.maxMetrics = 2;
  this.maxIndicators = 1;
};

ViewIndicators.prototype = new View;

ViewIndicators.prototype.init = function(DOMLayer) { 

  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = translate(AppTemplate.viewindicators, Lang.ViewIndicators);
  
  if (!this.target) return;

  this.initTitles();
  this.addBehaviours();
};

ViewIndicators.prototype.addBehaviours = function() {
  
  var jClearAll = $(this.DOMLayer).find(".clearall");
  jClearAll.click(ViewIndicators.prototype.atClearAllClick.bind(this));
  
  CommandListener.capture(this.DOMLayer);
};

ViewIndicators.prototype.toggleView = function() {
  var jLayer = $(this.DOMLayer);
  var jLabel = jLayer.find("label:first");
  var jFolders = jLayer.find(".folders");
  var value = jLabel.html();

  if (jFolders.is(":visible")) {
    jLabel.html(value.replace("-", "+"));
    jFolders.hide();
  }
  else {
    jLabel.html(value.replace("+", "-"));
    jFolders.show();
  }
};

ViewIndicators.prototype.isIndicatorSelected = function(id) {
  var jIndicator = $(this.DOMLayer).find(".indicator." + id);
  return jIndicator.find("input").get(0).checked;
};

ViewIndicators.prototype.unselectIndicator = function(id) {
  var jIndicator = $(this.DOMLayer).find(".indicator." + id);
  var jInput = jIndicator.find("input");
  var jColorBox = jIndicator.find(".colorbox");

  jInput.get(0).checked = false;
  jColorBox.hide();
  
  this.checkIndicators();
};

ViewIndicators.prototype.unselectIndicators = function() {
  var jIndicators = $(this.DOMLayer).find(".indicator");
  
  for (var i=0; i<jIndicators.length; i++) {
    var jIndicator = $(jIndicators[i]);
    var jInput = jIndicator.find("input");
    var jColorBox = jIndicator.find(".colorbox");
  
    jInput.get(0).checked = false;
    jColorBox.hide();
  }
  
  this.checkIndicators();
};

ViewIndicators.prototype.selectIndicator = function(id) {
  var jIndicator = $(this.DOMLayer).find(".indicator." + id);
  var jInput = jIndicator.find("input");
  var jColorBox = jIndicator.find(".colorbox");
  var color = View.getColor(View.ColorContext.INDICATORS, id);
  
  if (color == null)
    color = View.retainColor(View.ColorContext.INDICATORS, id);

  jInput.get(0).checked = true;
  jColorBox.show();
  jColorBox.css("background-color", color);
  
  this.checkIndicators();
};

ViewIndicators.prototype.disableIndicator = function(id) {
  var jIndicator = $(this.DOMLayer).find(".indicator." + id);
  var jInput = jIndicator.find("input");
  
  jIndicator.addClass("disabled");
  jInput.get(0).disabled = true;
};

ViewIndicators.prototype.enableIndicator = function(id) {
  var jIndicator = $(this.DOMLayer).find(".indicator." + id);
  var jInput = jIndicator.find("input");
  
  jIndicator.removeClass("disabled");
  jInput.get(0).disabled = false;
};

ViewIndicators.prototype.getSelectedIndicators = function(id) {
  var indicatorList = this.target.dashboard.model.indicatorList;
  var result = {};
  
  for (var i=0; i<indicatorList.length; i++) {
    var indicator = indicatorList[i];
    if (this.isIndicatorSelected(indicator.id))
      result[indicator.id] = indicator.id;
  };
  
  return result;
};

ViewIndicators.prototype.getSelectedMetrics = function(id) {
  var indicatorList = this.target.dashboard.model.indicatorList;
  var result = {};
  
  for (var i=0; i<indicatorList.length; i++) {
    var indicator = indicatorList[i];
    if (this.isIndicatorSelected(indicator.id))
      result[indicator.metric] = indicator.metric;
  };
  
  return result;
};

ViewIndicators.prototype.checkIndicators = function(id) {
  var metrics = this.getSelectedMetrics();
  var indicators = this.getSelectedIndicators();
  var indicatorList = this.target.dashboard.model.indicatorList;
  
  for (var i=0; i<indicatorList.length; i++) {
    var indicator = indicatorList[i];
    
    if (this.maxMetrics == -1 || size(metrics) < this.maxMetrics) {
      if (this.maxIndicators == -1 || size(indicators) < this.maxIndicators)
        this.enableIndicator(indicator.id);
      else {
        if (indicators[indicator.id] != null)
          this.enableIndicator(indicator.id);
        else
          this.disableIndicator(indicator.id);
      }
    }
    else {
      if (metrics[indicator.metric] != null) {
        if (this.maxIndicators == -1 || size(indicators) < this.maxIndicators)
          this.enableIndicator(indicator.id);
        else {
          if (indicators[indicator.id] != null)
            this.enableIndicator(indicator.id);
          else
            this.disableIndicator(indicator.id);
        }
      }
      else this.disableIndicator(indicator.id);
    }
  };
  
  this.refreshToolbar();
};

ViewIndicators.prototype.setMaxMetrics = function(maxMetrics) {
  this.maxMetrics = maxMetrics;
};

ViewIndicators.prototype.setMaxIndicators = function(maxIndicators) {
  this.maxIndicators = maxIndicators;
};

ViewIndicators.prototype.refreshIndicators = function(indicators, jIndicators) {
  var indicatorTemplate = translate(AppTemplate.viewindicatorsitem, Lang.ViewIndicators);
  
  for (var i=0; i<indicators.length; i++) {
    var indicatorKey = indicators[i];
    var indicator = this.target.dashboard.model.indicatorMap[indicatorKey];
    var jIndicator = $.tmpl(indicatorTemplate, { id: indicator.id, label: indicator.label, description: indicator.description });
    var DOMIndicator = jIndicator.get(0);
    var jInput = jIndicator.find("input");
    var jColorBox = jIndicator.find(".colorbox");
    
    DOMIndicator.id = indicator.id;
    
    jColorBox.click(ViewIndicators.prototype.toggleColorsDialog.bind(this, View.ColorContext.INDICATORS, indicator.id, jColorBox.get(0)));
    jInput.change(ViewIndicators.prototype.atIndicatorClick.bind(this, DOMIndicator));
    jIndicators.append(jIndicator);
  }
};

ViewIndicators.prototype.refreshFolder = function(folder, jFoldersContainer) {
  var folderTemplate = translate(AppTemplate.viewindicatorsfolder, Lang.ViewIndicators);
  var jFolder = $.tmpl(folderTemplate, { label: folder.label });
  var jTitle = jFolder.find(".title");
  var jFolders = jFolder.find(".folders");
  var jIndicators = jFolder.find(".indicators");
  
  jFoldersContainer.append(jFolder);

  jTitle.click(ViewIndicators.prototype.atFolderClick.bind(this, jFolder.get(0)));
  this.refreshFolders(folder.folders, jFolder.find(".folders"));
  this.refreshIndicators(folder.indicators, jIndicators);
};

ViewIndicators.prototype.refreshFolders = function(folders, jFoldersContainer) {
  for (var i=0; i<folders.length; i++)
    this.refreshFolder(folders[i], jFoldersContainer);
};

ViewIndicators.prototype.refresh = function() {
  var folderList = this.target.dashboard.model.folderList;
  var jFolders = $(this.DOMLayer).find(".folders");

  //View.freeColors(View.ColorContext.INDICATORS);
  this.refreshToolbar();
  this.refreshFolders(folderList, jFolders);
};

ViewIndicators.prototype.refreshToolbar = function(context, id, color) {
  var jLayer = $(this.DOMLayer);
  var jClearAll = jLayer.find(".clearall");
  var countSelectedIndicators = jLayer.find(".indicator input:checked").length;
  
  if (countSelectedIndicators > 0)
    jClearAll.show();
  else
    jClearAll.hide();
};

ViewIndicators.prototype.updateColor = function(context, id, color) {
  var jIndicator = $(this.DOMLayer).find(".indicator." + id);
  var jColorBox = jIndicator.find(".colorbox");
  
  jColorBox.css("background-color", color);
  
  CommandListener.throwCommand("setcolor(" + this.target.id + "," + context + "," + id + "," + (color.indexOf("#")==-1?"#":"") + color + ")");
};

ViewIndicators.prototype.atFolderClick = function(DOMFolder) {
  Desktop.expandWestLayer($(this.DOMLayer).find("._expansible").get(0));
  var jFolder = $(DOMFolder);
  jFolder.removeClass("closed");
  if (jFolder.hasClass("opened")) {
    jFolder.removeClass("opened");
    jFolder.addClass("closed");
  }
  else {
    jFolder.addClass("opened");
    jFolder.removeClass("closed");
  }
};

ViewIndicators.prototype.atIndicatorClick = function(DOMIndicator) {
  Desktop.expandWestLayer($(this.DOMLayer).find("._expansible").get(0));
  CommandListener.throwCommand("toggleindicator(" + this.target.id + "," + DOMIndicator.id + "," + this.target.dashboard.model.report + ")");
  return false;
};

ViewIndicators.prototype.atClearAllClick = function(DOMIndicator) {
  var countSelectedIndicators = $(this.DOMLayer).find(".indicator input:checked").length;
  
  if (countSelectedIndicators <= 0)
    return false;
  
  Desktop.expandWestLayer($(this.DOMLayer).find("._expansible").get(0));
  CommandListener.throwCommand("unselectindicators(" + this.target.id + "," + this.target.dashboard.model.report + ")");
  return false;
};