var activateNotifications = true;
var CurrentDOMOptions = null;
var translations = new Object();
translations["es"] = { Add: "Añadir", Download: "Descargar", Tools: "Herramientas"}; 
translations["en"] = { Add: "Add", Download: "Download", Tools: "Tools"}; 

function enableNotifications() {
  activateNotifications = true;
}

function disableNotifications() {
  activateNotifications = false;
}

function getWindowSize() {
  var winW = 630, winH = 460;
  if (document.body && document.body.offsetWidth) {
   winW = document.body.offsetWidth;
   winH = document.body.offsetHeight;
  }
  if (document.compatMode=='CSS1Compat' &&
      document.documentElement &&
      document.documentElement.offsetWidth ) {
   winW = document.documentElement.offsetWidth;
   winH = document.documentElement.offsetHeight;
  }
  if (window.innerWidth && window.innerHeight) {
   winW = window.innerWidth;
   winH = window.innerHeight;
  }

  return {width: winW, height: winH};
}

function atTabActivated(extTabPanel, extTab) {
  var height = getWindowSize().height - extTabPanel.bodyEl.dom.offsetTop - 80;
  
  extTab.bodyEl.setHeight(height);
  extTab.bodyEl.dom.style.overflow = "auto";
  
  Ext.EventManager.onWindowResize(atWindowResize.createDelegate(null, [extTabPanel, extTab]));
  
  if (!activateNotifications) return;
  
  var extListViewerList = extTab.bodyEl.select(".listviewer");
  extListViewerList.each(function(extListViewer) {
    eval(extListViewer.dom.id + ".refresh();");
  });
  
  refreshToolbar(extTab.bodyEl.id);
}

function renderOperations(extToolbar, operations, label, collapsed, labelOnSingle) {
  var result = ""; 
  
  if (operations.length == 0)
    return;
  
  if (operations.length == 1) {
    result = "<li style='list-style-type:none;'>" + ((labelOnSingle)?"<a class='button behaviour' href='javascript:void(null)'>" + label + "</a>":operations[0]) + "</li>";
    className = "";
  }
  else {
    if (collapsed) {
      var operationsValue = "";
      for (var i=0; i<operations.length; i++)
        operationsValue += "<li class='option'>" + operations[i] + "</li>";
      result = "<a class='button behaviour' href='toggleselector()'>" + label + "</a><ul class='options'>" + operationsValue + "</ul>"; 
      className = "selector"; 
    }
    else {
      for (var i=0; i<operations.length; i++)
        result += "<li class='option'>" + operations[i] + "</li>";
      className = "";
    }
  }
  
  var extRow = extToolbar.select("table tr").first();
  var extPivotColumn = extToolbar.select("table tr td.pivot").first();
  var DOMColumn = document.createElement("td");
  DOMColumn.innerHTML = result;
  DOMColumn.className = className;
  extRow.dom.insertBefore(DOMColumn, extPivotColumn.dom);
}

function atBehaviourClick(DOMBehaviour, EventLaunched) {
  if ((DOMOptions = DOMBehaviour.next("ul.options")) != null)
    toggleSelectorOptions(DOMBehaviour, DOMOptions);
  Event.stop(EventLaunched);
  return false;
};

function showSelectorOptions(DOMSelector, DOMOptions) {
  DOMOptions.style.display = "block";
  
  Ext.get(document.body).on("click", atHideSelectorOptions, this);
  CurrentDOMOptions = DOMOptions;
  
  if (DOMOptions.hasClassName("top"))
    DOMOptions.style.marginTop = "-" + (DOMSelector.getHeight() + DOMOptions.getHeight() - 1) + "px";

  if (DOMSelector.getWidth() > DOMOptions.getWidth()) {
    var extOptions = Ext.get(DOMOptions);
    extOptions.setWidth(DOMSelector.getWidth());
  }
};

function hideSelectorOptions(DOMOptions) {
  if (!DOMOptions) DOMOptions = CurrentDOMOptions;
  DOMOptions.style.display = "none";
  CurrentDOMOptions = null;
  Ext.get(document.body).un("click", atHideSelectorOptions);
};

function toggleSelectorOptions(DOMSelector, DOMOptions) {
  var isVisible = DOMOptions.style.display == "block";
  if (isVisible) hideSelectorOptions(DOMOptions);
  else showSelectorOptions(DOMSelector, DOMOptions);
};

function atHideSelectorOptions() {
  hideSelectorOptions();
};

function atSelectorOptionClick(DOMSelectorOption, EventLaunched) {
  hideSelectorOptions();
};

function refreshToolbar(id) {
  var extToolbar = Ext.get(document.body).select(".page .node .toolbar").first();
  if (extToolbar == null) return;
  
  extToolbar.dom.innerHTML = "<table><tr><td class='pivot'></td></tr></table>";
  
  var buttons = new Array();
  buttons["NAVIGATION"] = viewToolbarButtonsList[id]!=null&&viewToolbarButtonsList[id]["NAVIGATION"]!=null?toolbarButtonsList["NAVIGATION"].concat(viewToolbarButtonsList[id]["NAVIGATION"]):toolbarButtonsList["NAVIGATION"];
  buttons["ADD"] = viewToolbarButtonsList[id]!=null&&viewToolbarButtonsList[id]["ADD"]!=null?toolbarButtonsList["ADD"].concat(viewToolbarButtonsList[id]["ADD"]):toolbarButtonsList["ADD"];
  buttons["DOWNLOAD"] = viewToolbarButtonsList[id]!=null&&viewToolbarButtonsList[id]["DOWNLOAD"]!=null?toolbarButtonsList["DOWNLOAD"].concat(viewToolbarButtonsList[id]["DOWNLOAD"]):toolbarButtonsList["DOWNLOAD"];
  buttons["TOOL"] = viewToolbarButtonsList[id]!=null&&viewToolbarButtonsList[id]["TOOL"]!=null?toolbarButtonsList["TOOL"].concat(viewToolbarButtonsList[id]["TOOL"]):toolbarButtonsList["TOOL"];
  buttons["EDIT"] = viewToolbarButtonsList[id]!=null&&viewToolbarButtonsList[id]["EDIT"]!=null?toolbarButtonsList["EDIT"].concat(viewToolbarButtonsList[id]["EDIT"]):toolbarButtonsList["EDIT"];
  buttons["CONTEXT"] = viewToolbarButtonsList[id]!=null&&viewToolbarButtonsList[id]["CONTEXT"]!=null?toolbarButtonsList["CONTEXT"].concat(viewToolbarButtonsList[id]["CONTEXT"]):toolbarButtonsList["CONTEXT"];
  
  buttons["DOWNLOAD"] = viewToolbarButtonsList[id]!=null&&viewToolbarButtonsList[id]["PRINT"]!=null?buttons["DOWNLOAD"].concat(viewToolbarButtonsList[id]["PRINT"]):buttons["DOWNLOAD"];

  renderOperations(extToolbar, buttons["NAVIGATION"], "", false, false);
  renderOperations(extToolbar, buttons["ADD"], translations[language].Add, true, true);
  renderOperations(extToolbar, buttons["DOWNLOAD"], translations[language].Download, true, false);
  renderOperations(extToolbar, buttons["TOOL"], translations[language].Tools, true, false);
  renderOperations(extToolbar, buttons["EDIT"], "", false, false);
  renderOperations(extToolbar, buttons["CONTEXT"], "", false, false);
  
  var aBehaviours = extToolbar.select(".behaviour");
  aBehaviours.each(function(extBehaviour) { 
    DOMBehaviour = extBehaviour.dom;
    Event.observe(DOMBehaviour, 'click', atBehaviourClick.bind(this, DOMBehaviour));
  }, this);
}

function atWindowResize(extTabPanel, extTab, extTab2) {
  this.rescale();
}

function atUsernameClick(EventLaunched) {
  var extUsernamePanel = Ext.get("UsernamePanel");
  if (extUsernamePanel.dom.style.display == "block")
    hideUsernamePanel();
  else
    showUsernamePanel();
 
  Event.stop(EventLaunched);
};

function showUsernamePanel() {
  Ext.get("UsernamePanel").dom.style.display = "block";
  Ext.get(document.body).on("click", hideUsernamePanel, null);
  return false;
};

function hideUsernamePanel() {
  Ext.get("UsernamePanel").dom.style.display = "none";
  Ext.get(document.body).un("click", hideUsernamePanel, null);
  return false;
};

function rescale() {
  var windowSize = getWindowSize();
  var extHeader = Ext.get("header");
  var extCenter = Ext.get("center");
  var extCenterContent = Ext.get("center").select(".layout.main.x-layout-container.x-layout-nested-layout.x-layout-active-content").first();
  var extFooter = Ext.get("footer");
  
  extHeader.setWidth(windowSize.width-1);
  extCenter.setWidth(windowSize.width-1);
  extCenterContent.setWidth(windowSize.width-1);
  extFooter.setWidth(windowSize.width-1);
  
  extCenter.setHeight(windowSize.height-extHeader.getHeight()-extFooter.getHeight()-1);
  extFooter.setTop(windowSize.height-extFooter.getHeight()-1);
}

function getRequestParameter(name) {
  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
  var regexS = "[\\?&]"+name+"=([^&#]*)";
  var regex = new RegExp( regexS );
  var results = regex.exec( window.location.href );
  if( results == null )
    return "";
  else
    return results[1];
}

function showHome() {
  var homePage = this.getRequestParameter("home");
  if (homePage == "") return;
  window.document.location.href = path + "pages/" + homePage + ".html?home=" + homePage;
}

function showPage(name) {
  var homePage = this.getRequestParameter("home");
  if (homePage == "") homePage = home;
  window.document.location.href = path + "pages/" + name + ".html?home=" + homePage;
}

function showEnvironment(name) {
  window.document.location.href = path + "pages/" + name + ".html?home=" + name;
}

function downloadFile(name) {
  window.document.location.href = path + "/templates/" + name;
}

function showLoading() {
  var DOMLoading = document.getElementById("loading");
  DOMLoading.style.display = "block";
}

function hideLoading() {
  var DOMLoading = document.getElementById("loading");
  DOMLoading.style.display = "none";
}

Ext.onReady(function() {
  var extUsername = Ext.get("Username");
  Event.observe(extUsername.dom, "click", atUsernameClick);
  rescale();
  
  var extEnvironments = Ext.get("Environments").select("li");
  extEnvironments.each(function(extEnvironment) {
    if (window.document.location.href.indexOf(extEnvironment.dom.id) != -1) {
      extEnvironment.addClass("active");
      extEnvironment.select(".current").first().dom.style.display = "inline";
    }
    else
      extEnvironment.removeClass("active");
  }, this);
  
  if (window.document.location.href.indexOf("index.html") != -1) {
    showLoading();
    window.setTimeout(hideLoading, 800);
  }
  else hideLoading();
  
  var extTabList = Ext.get(document.body).select(".tabs .tab");
  var extActive = null;
  extTabList.each(function(extTab) {
    if (extTab.isVisible()) extActive = extTab;
  }, this);
  
  if (extActive) refreshToolbar(extActive.dom.id);
});