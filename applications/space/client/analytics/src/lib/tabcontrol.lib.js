TabControl = function(Options) {
  this.id = Options.id;
  this.tabsCount = 0;
  this.tabsIds = new Object();
  this.tabsIdxs = new Object();
  this.adding = false;
  this.deleting = false;
  this.templates = new Object();
  this.templates["es"] = new Object();
  this.templates["es"]["main"] = "<div id='tabs-${id}' class='tabs'><ul class='header'></ul><a class='behaviour deletetabs' title='cerrar pestañas'>cerrar pestañas</a></div>";
  this.templates["es"]["tab"] = "<li><a href='#{href}'>#{label}</a><span class='ui-icon ui-icon-close deletetab'>cerrar</span></li>";
  this.templates["es"]["tab_header"] = "<div class='title'>${title}</div>";
  this.templates["es"]["tab_body"] = "<div class='layer'></div>";
  this.templates["en"] = new Object();
  this.templates["en"]["main"] = "<div id='tabs-${id}' class='tabs'><ul class='header'></ul><a class='behaviour deletetabs' title='close tabs'>close tabs</a></div>";
  this.templates["en"]["tab"] = "<li><a href='#{href}'>#{label}</a><span class='ui-icon ui-icon-close deletetab'>close</span></li>";
  this.templates["en"]["tab_header"] = "<div class='title'>${title}</div>";
  this.templates["en"]["tab_body"] = "<div class='layer'></div>";
  this.language = Options.language;
  this.showHeader = Options.showHeader!=null?Options.showHeader:true;
  if (this.templates[this.language] == null) this.language = "en";
};

TabControl.prototype.render = function(DOMLayer) {
  
  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = replaceAll(this.templates[this.language]["main"], "${id}", this.id);;
  
  CommandListener.capture(this.DOMLayer);

  this.renderTabs();
  this.addBehaviours();
};

TabControl.prototype.renderTabs = function() {

  $("#tabs-" + this.id).tabs({
    tabTemplate : this.templates[this.language]["tab"],
    show: TabControl.prototype.atShowTab.bind(this)
  });
  
  this.activeTabIndex = 0;
};

TabControl.prototype.addBehaviours = function() {
  var jDeleteTabs = $(this.DOMLayer).find(".deletetabs");
  jDeleteTabs.click(TabControl.prototype.atDeleteTabs.bind(this));
};

TabControl.prototype.refresh = function(){
  $("#tabs-" + this.id).tabs('select', this.activeTabIndex);
  if (this.onShowTab) this.onShowTab(this, this.activeTabIndex);
};

TabControl.prototype.checkHeaderVisibility = function() {
  var tabPanelId = "#tabs-" + this.id;
  var jHeader = $(tabPanelId).find(".header");
  var jDeleteTabs = $(this.DOMLayer).find(".deletetabs");
  if (this.showHeader) {
    if (this.tabsCount <= 1) {
      jHeader.hide();
      jDeleteTabs.hide();
    }
    else {
      jHeader.show();
      jDeleteTabs.show();
    }
  }
  else {
    jHeader.hide();
    jDeleteTabs.hide();
  }
};

TabControl.prototype.relocateButtons = function() {
  var jDeleteTabs = $(this.DOMLayer).find(".deletetabs");
  var DOMDeleteTabs = jDeleteTabs.get(0);
  var tabPanelId = "#tabs-" + this.id;
  var lastHeaderItem = $(tabPanelId + " .header li:last");
  var left = lastHeaderItem.position().left+lastHeaderItem.width();
  
  if (this.tabsCount > 2) jDeleteTabs.show();
  else jDeleteTabs.hide();
  
  if (DOMDeleteTabs.initialLeft == null)
    DOMDeleteTabs.initialLeft = jDeleteTabs.position().left;
  
  if (left > 0)
    jDeleteTabs.css("left", left+5);
  else
    jDeleteTabs.css("left", DOMDeleteTabs.initialLeft);
  
  jDeleteTabs.css("top", lastHeaderItem.position().top+lastHeaderItem.height()-24);
};

TabControl.prototype.getTab = function(index) {
  var tabPanelId = "#tabs-" + this.id;
  var tabId = tabPanelId + "-" + index;
  
  var tab = new Object();
  tab.DOMTab = $(tabId).get(0);
  
  if (tab.DOMTab == null) return null;
  
  tab.getLayer = function() {
    var jView = $(this.DOMTab).find(".layer");
    return jView.get(0);
  };
  
  return tab;
};

TabControl.prototype.activateTab = function(id) {
  var tabPanelId = "#tabs-" + this.id;
  var index = this.tabsIds[id];
  var tabId = tabPanelId + "-" + index;
  $(tabPanelId).tabs('select', tabId);
};

TabControl.prototype.getActiveTab = function() {
  return this.getTab(this.activeTabIndex);
};

TabControl.prototype.existsTab = function(id) {
  return this.getTab(this.tabsIds[id]) != null;
};

TabControl.prototype.addTab = function(id, title, className, closeable) {
  var tabPanelId = "#tabs-" + this.id;
  var index = this.tabsCount;
  var newTabId = tabPanelId + "-" + index;
  var labelTemplate = $.template(null, "<div>" + this.templates[this.language]["tab_header"] + "</div>");
  var label = $.tmpl(labelTemplate, { title: title }).get(0).innerHTML;
  
  if ($(newTabId).length > 0) return;
  
  this.adding = true;
  
  $(tabPanelId).tabs("add", newTabId, label);
  var jItem = $(tabPanelId).find(".header li:last");
  if (!closeable) jItem.addClass("uncloseable");
  jItem.find(".deletetab").click(TabControl.prototype.atDeleteTab.bind(this, jItem.get(0)));
  jItem.addClass("index_" + length);
  if (className != null) jItem.addClass(className);
  
  var jTab = $(newTabId);
  jTab.html(this.templates[this.language]["tab_body"]);
  
  this.tabsCount++;
  this.tabsIds[id] = index;
  this.tabsIdxs[index] = id;
  
  this.relocateButtons();
  this.checkHeaderVisibility();
  
  this.adding = false;
  
  $(tabPanelId).tabs('select', newTabId);
  
  return this.getTab(index);
};

TabControl.prototype.updateTabLabel = function(index, data) {
  var tabPanelId = "#tabs-" + this.id;
  var jItem = $(tabPanelId).find(".header li.index_" + index);
  jItem.find(".title").text(data.title);
  jItem.find(".subtitle").text(data.subtitle);
  this.relocateButtons();
  this.checkHeaderVisibility();
};

TabControl.prototype.deleteTab = function(index) {
  var tabPanelId = "#tabs-" + this.id;
  $(tabPanelId).tabs("remove", index);
  
  this.tabsCount--;
  var id = this.tabsIdxs[index];
  delete this.tabsIdxs[index];
  delete this.tabsIds[id];
  
  this.relocateButtons();
  this.checkHeaderVisibility();
  if (this.onDeleteTab) this.onDeleteTab(this, index);
};

TabControl.prototype.deleteTabs = function(index) {
  var tabsSize = this.tabsCount;
  
  for (var i=tabsSize-1; i>=1; i--)
    this.deleteTab(i);
};

//************************************************************************************************************

TabControl.prototype.atShowTab = function(event, ui) {
  this.activeTabIndex = ui.index;
  
  if (this.adding || this.deleting)
    return;
  
  this.refresh();
};

TabControl.prototype.atDeleteTab = function(DOMItem, event) {
  this.deleting = true;
  this.deleteTab($(DOMItem).index());
  this.deleting = false;
};

TabControl.prototype.atDeleteTabs = function() {
  this.deleting = true;
  this.deleteTabs();
  this.deleting = false;
};