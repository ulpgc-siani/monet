// IMPORTANT! THIS LIST VIEWER IS DIFFERENT FROM OFFICE LIST VIEWER.

var aTemplates = new Array();
aTemplates["es"] = new Array();
aTemplates["es"]["LIST_VIEWER_TEMPLATE"] = "<div class='label'></div><div class='summary'><ul class='filters'></ul><div class='count'></div></div><table class='toolbar' width='100%'><tr><td class='edition' width='18px'><input type='checkbox' class='selectall'/></td><td width='100%'><div class='add list'><div class='edition selector'>Añadir&nbsp;<select style='width:200px;margin-top:-2px;font-size:14px;'></select><a href='#'></a></div><a class='edition delete' style='display:none;'>Eliminar elementos seleccionados</a></td></tr></table><div class='layer'><ul class='items'></ul><div class='lwloading'><p>Cargando...</p></div></div><div class='paging'><a class='first'>primero</a><a class='previous'>anterior</a><a class='next'>siguiente</a><a class='last'>último</a></div>";
aTemplates["es"]["LIST_VIEWER_ITEM_TEMPLATE"] = "<li class='item'><table width='100%'><tr><td width='18px' class='edition'><input type='checkbox' class='selector'/></td><td width='100%'><a class='content' href='#'>#{content}</a></td><td><a class='delete edition' href='#'></a></td></tr></table></li>";
aTemplates["es"]["LIST_VIEWER_DEFAULT_ITEM_CONTENT_TEMPLATE"] = "<div class='item' style='display:none;'><div class='label'>#{label}</div><div class='description'>#{description}</div></div></div>";
aTemplates["es"]["LIST_VIEWER_DEFAULT_NO_ITEMS_TEMPLATE"] = "<div style='margin: 10px 0px;'>No existen elementos</div>";
aTemplates["es"]["LIST_VIEWER_DEFAULT_COUNT_ITEMS_TEMPLATE"] = "#{count} elementos";
aTemplates["es"]["LIST_VIEWER_FILTERS_MESSAGE_TEMPLATE"] = "<li style='margin-right:5px;'>Filtrando:&nbsp;</li>";
aTemplates["es"]["LIST_VIEWER_FILTER_TEMPLATE"] = "<li class='filter #{code}'><span>#{label}</span>&nbsp;<a class='unselect' href='#' alt='quitar'></a></li>";
aTemplates["es"]["LIST_VIEWER_WIZARD_TEMPLATE"] = "<div class='separator'><div class='group groupby'><label>Mostrar</label><table class='filter'><tr><td class='content'><input type='text' class='value'/></td></tr></table><ul class='groupby list'></ul></div><div class='group sortby'><label>Ordenar por</label><ul class='sortby list'></ul><ul class='toolbar'><li><a class='more' href='#'>más</a></li></ul></div><div class='group add edition'><label>Añadir</label><ul class='add list'></ul></div></div>";
aTemplates["es"]["LIST_VIEWER_WIZARD_FILTER_EMPTY_TEMPLATE"] = "<span class='empty'>Introduzca el texto que desee encontrar</span>";
aTemplates["es"]["LIST_VIEWER_WIZARD_SORTBYLIST_SECTION_TEMPLATE"] = "<ul class='section'></ul>";
aTemplates["es"]["LIST_VIEWER_WIZARD_SORTBYLIST_SECTION_OPTIONS_TEMPLATE"] = "<div class='sortby options'>&nbsp;<a class='unselect' href='#' alt='quitar'></a></div>";
aTemplates["es"]["LIST_VIEWER_WIZARD_GROUPBYLIST_ITEM_TEMPLATE"] = "<li class='#{code}'><div class='label'>#{label}</div><div class='content'><select class='selector'><option value='all'>Todos</option></select></div></li>";
aTemplates["es"]["LIST_VIEWER_WIZARD_SORTBYLIST_ITEM_TEMPLATE"] = "<li class='#{code}'><a class='label' href='#'>#{label}</a></li>";
aTemplates["es"]["LIST_VIEWER_WIZARD_ADDLIST_ITEM_TEMPLATE"] = "<li><a class='#{code}' href='#{command}'>#{label}</a><div class='description'>#{description}</div></li>";
aTemplates["en"] = new Array();
aTemplates["en"]["LIST_VIEWER_TEMPLATE"] = "<div class='label'></div><div class='summary'><ul class='filters'></ul><div class='count'></div></div><table class='toolbar' width='100%'><tr><td class='edition' width='18px'><input type='checkbox' class='selectall'/></td><td width='100%'><div class='add list'><div class='edition selector'>Add&nbsp;<select style='width:200px;margin-top:-2px;font-size:14px;'></select><a href='#'></a></div><a class='edition delete' style='display:none;'>Delete selected elements</a></td></tr></table><div class='layer'><ul class='items'></ul><div class='lwloading'><p>Loading…</p></div></div><div class='paging'><a class='first'>first</a><a class='previous'>previous</a><a class='next'>next</a><a class='last'>last</a></div>";
aTemplates["en"]["LIST_VIEWER_ITEM_TEMPLATE"] = "<li class='item'><table width='100%'><tr><td width='18px' class='edition'><input type='checkbox' class='selector'/></td><td width='100%'><a class='content' href='#'>#{content}</a></td><td><a class='delete edition' href='#'></a></td></tr></table></li>";
aTemplates["en"]["LIST_VIEWER_DEFAULT_ITEM_CONTENT_TEMPLATE"] = "<div class='item' style='display:none;'><div class='label'>#{label}</div><div class='description'>#{description}</div></div></div>";
aTemplates["en"]["LIST_VIEWER_DEFAULT_NO_ITEMS_TEMPLATE"] = "<div style='margin: 10px 0px;'>No elements</div>";
aTemplates["en"]["LIST_VIEWER_DEFAULT_COUNT_ITEMS_TEMPLATE"] = "#{count} elements";
aTemplates["en"]["LIST_VIEWER_FILTERS_MESSAGE_TEMPLATE"] = "<li style='margin-right:5px;'>Filtering:&nbsp;</li>";
aTemplates["en"]["LIST_VIEWER_FILTER_TEMPLATE"] = "<li class='filter #{code}'><span>#{label}</span>&nbsp;<a class='unselect' href='#' alt='delete'></a></li>";
aTemplates["en"]["LIST_VIEWER_WIZARD_TEMPLATE"] = "<div class='separator'><div class='group groupby'><label>Show</label><table class='filter'><tr><td class='content'><input type='text' class='value'/></td></tr></table><ul class='groupby list'></ul></div><div class='group sortby'><label>Sort by</label><ul class='sortby list'></ul><ul class='toolbar'><li><a class='more' href='#'>more</a></li></ul></div><div class='group add edition'><label>Add</label><ul class='add list'></ul></div></div>";
aTemplates["en"]["LIST_VIEWER_WIZARD_FILTER_EMPTY_TEMPLATE"] = "<span class='empty'>Enter the text you are looking for</span>";
aTemplates["en"]["LIST_VIEWER_WIZARD_SORTBYLIST_SECTION_TEMPLATE"] = "<ul class='section'></ul>";
aTemplates["en"]["LIST_VIEWER_WIZARD_SORTBYLIST_SECTION_OPTIONS_TEMPLATE"] = "<div class='sortby options'>&nbsp;<a class='unselect' href='#' alt='delete'></a></div>";
aTemplates["en"]["LIST_VIEWER_WIZARD_GROUPBYLIST_ITEM_TEMPLATE"] = "<li class='#{code}'><div class='label'>#{label}</div><div class='content'><select class='selector'><option value='all'>All</option></select></div></li>";
aTemplates["en"]["LIST_VIEWER_WIZARD_SORTBYLIST_ITEM_TEMPLATE"] = "<li class='#{code}'><a class='label' href='#'>#{label}</a></li>";
aTemplates["en"]["LIST_VIEWER_WIZARD_ADDLIST_ITEM_TEMPLATE"] = "<li><a class='#{code}' href='#{command}'>#{label}</a><div class='description'>#{description}</div></li>";

// IMPORTANT: escape and utf8Encode functions are needed by this library

var LIST_VIEWER_QUERY_SEPARATOR = ":";
var LIST_VIEWER_QUERIES_SEPARATOR = "_f_";
var MODE_ASCENDANT = "asc";
var MODE_DESCENDANT = "desc";
var CLASS_ASCENDANT = "ascendant";
var CLASS_DESCENDANT = "descendant";
var CLASS_SELECTED = "selected";
var CLASS_LISTVIEWER = "listviewer";
var CLASS_LISTVIEWER_WIZARD = "listviewer wizard";
var CLASS_DISABLED = "disabled";
var CLASS_VISIBLE = "visible";
var CLASS_EDITABLE = 'editable';
var CLASS_READONLY = "readonly";
var CLASS_ACTIVE = "active"; 
var LIST_ADD = "add";
var LIST_SORTBY = "sortby";
var LIST_GROUPBY = "groupby";
var CSS_SELECTED = ".selected";
var CSS_SELECT_ALL = ".selectall";
var CSS_DELETE = ".delete";
var CSS_MORE = ".more";
var CSS_FILTER = ".filter input.value";
var CSS_FILTER_EMPTY = ".filter .empty";
var CSS_ADDLIST = ".add.list";
var CSS_SORTBYLIST = ".sortby.list";
var CSS_SORTBYOPTIONS = ".sortby.options";
var CSS_GROUPBYLIST = ".groupby.list";
var CSS_SELECTOR = ".selector";
var CSS_UNSELECT = ".unselect";
var CSS_LIST_SECTION = ".section";
var CSS_ITEMS = ".items";
var CSS_ITEM = ".item";
var CSS_ITEM_SELECTOR = ".items .item .selector";
var CSS_LOADING = ".lwloading";
var CSS_PAGING = ".paging";
var CSS_PAGING_FIRST = ".first";
var CSS_PAGING_PREVIOUS = ".previous";
var CSS_PAGING_NEXT = ".next";
var CSS_PAGING_LAST = ".last";
var CSS_CONTENT = ".content";
var CSS_GROUP = ".group";
var CSS_LABEL = ".label";
var CSS_SUMMARY = ".summary";
var CSS_FILTERS = ".filters";
var CSS_COUNT = ".count";
var ITEMS_PER_PAGE = 40;
var CODE_WORDS_FILTER = "__words";
var KEY_UP = 38;
var KEY_DOWN = 40;
var KEY_ENTER = 13;
var KEY_LEFT = 37;
var KEY_RIGHT = 39;
var KEY_SHIFT = 16;
var KEY_CONTROL = 17;

function isFunction(item) {
  return (typeof item == "function");
};

function htmlDecode(input){
  var e = document.createElement('div');
  e.innerHTML = input;
  return e.childNodes[0].nodeValue;
};

function CGListViewer(Options, language) {
  this.Options = Options;
  this.aParameters = new Object();
  this.extFilter = null;
  this.extFilterEmpty = null;
  this.iNumPages = 0;
  this.iItemsPerPage = ITEMS_PER_PAGE;
  this.initState();
  this.loading = false;
  this.initialized = false;
  this.sWizardLayer = null;
  this.sName = "";
  this.sLabel = "";
  this.language = language;
  if (aTemplates[this.language] == null) this.language = "es";
};

CGListViewer.prototype.setWizardLayer = function(sWizardLayer) {
  this.sWizardLayer = sWizardLayer;
};

CGListViewer.prototype.getName = function() {
  return this.sName;
};

CGListViewer.prototype.setName = function(sName) {
  this.sName = sName;
};

CGListViewer.prototype.getLabel = function() {
  return this.sLabel;
};

CGListViewer.prototype.setLabel = function(sLabel) {
  this.sLabel = sLabel;
};

CGListViewer.prototype.init = function(sViewerLayer) {
  
  if (this.initialized) return;
  if (sViewerLayer == null) return;
  
  this.extViewerLayer = Ext.get(sViewerLayer);
  this.extViewerLayer.dom.innerHTML = aTemplates[this.language]["LIST_VIEWER_TEMPLATE"];
  this.extViewerLayer.addClass(CLASS_LISTVIEWER);
  
  if (this.sWizardLayer == null) {
    var extSummary = this.extViewerLayer.select(CSS_SUMMARY).first();
    this.sWizardLayer = Ext.id();
    new Insertion.Before(extSummary.dom, "<div id='" + this.sWizardLayer + "'></div>");
  }

  this.extWizardLayer = Ext.get(this.sWizardLayer);
  
  if (this.extWizardLayer != null) {
    this.extWizardLayer.dom.innerHTML = aTemplates[this.language]["LIST_VIEWER_WIZARD_TEMPLATE"];
    this.extWizardLayer.addClass(CLASS_LISTVIEWER_WIZARD);
  }

  this.loading = false;
  this.initialized = true;
  
  this.extScrollParent = this.extViewerLayer.up(".x-tabs-item-body");
  if (this.extScrollParent) this.extScrollParent.addListener("scroll", this.atScroll.bind(this));
};

CGListViewer.prototype.showLoading = function() {
  if (this.extViewerLayer == null) return;
  this.extViewerLayer.select(CSS_LOADING).first().dom.style.display = "block";
  //this.extViewerLayer.select(CSS_ITEMS).first().dom.style.display = "none";
};

CGListViewer.prototype.hideLoading = function() {
  if (this.extViewerLayer == null) return;
  this.extViewerLayer.select(CSS_LOADING).first().dom.style.display = "none";
  //this.extViewerLayer.select(CSS_ITEMS).first().dom.style.display = "block";
};

CGListViewer.prototype.addItemsToDOMSelector = function(DOMSelector, Options) {
  for (var index in Options) {
    if (isFunction(Options[index])) continue;
    var Option = Options[index];
    var DOMOption = document.createElement('option');
    
    DOMOption.value = Option.Code;
    DOMOption.text = Option.Label;
    DOMOption.name = Option.Command;
    
    try {
      DOMSelector.add(DOMOption, null);
    }
    catch(ex) {
      DOMSelector.add(DOMOption);
    }
  }
};

CGListViewer.prototype.isItemOnSelection = function(CodeList, CodeItem) {
  if (CodeList == LIST_ADD) return false;
  return (this.State.Lists[CodeList].Items[CodeItem] != null);
};

CGListViewer.prototype.addItemOnSelection = function(CodeList, Item) {
  var AuxItem = this.State.Lists[CodeList].Items[Item.Code];

  if (AuxItem) {
    Item.Order = AuxItem.Order;
    this.State.Lists[CodeList].Items[Item.Code] = Item;
  }
  else {
    this.State.Lists[CodeList].Orders.push(Item.Code);
    Item.Order = this.State.Lists[CodeList].Orders.length-1;
    this.State.Lists[CodeList].Items[Item.Code] = Item;
  }
};

CGListViewer.prototype.removeItemOnSelection = function(CodeList, CodeItem) {
  var sResult = this.State.Lists[CodeList].Orders.join(",");
  var iPos = sResult.indexOf(CodeItem);
  var aItems = new Array();
  
  if (iPos != -1) {
    if (iPos > 0) iPos = iPos-1;
    sResult = sResult.substring(0, iPos) + sResult.substring(iPos+CodeItem.length+1);
  }
  
  this.State.Lists[CodeList].Orders = (sResult.length > 0)?sResult.split(","):new Array();
  for (var i=0; i<this.State.Lists[CodeList].Orders.length;i++) {
    var CodeItem = this.State.Lists[CodeList].Orders[i];
    aItems[CodeItem] = this.State.Lists[CodeList].Items[CodeItem];
    aItems[CodeItem].Order = i;
  }
    
  this.State.Lists[CodeList].Items = aItems;
};

CGListViewer.prototype.renderList = function(List, CodeList, ListStyle) {
  var extGroup = this.extWizardLayer.select(CSS_GROUP + "." + CodeList).first();

  if (List == null) {
    var extElement = extGroup;
    if (CodeList == LIST_GROUPBY) extElement = this.extWizardLayer.select(ListStyle).first();
    extElement.dom.style.display = "none";
    return;
  }
  
  List.bUpdate = true;
  this.updateList(List, CodeList, ListStyle, (CodeList == LIST_ADD)?"":",&nbsp;");
};

CGListViewer.prototype.renderFilter = function() {
  this.extFilter = this.extWizardLayer.select(CSS_FILTER).first();
  Event.observe(this.extFilter.dom, "keyup", CGListViewer.prototype.atFilterKeyUp.bind(this));
  Event.observe(this.extFilter.dom, "focus", CGListViewer.prototype.atFilterFocus.bind(this));
  Event.observe(this.extFilter.dom, "blur", CGListViewer.prototype.atFilterBlur.bind(this));
  new Insertion.After(this.extFilter.dom, aTemplates[this.language]["LIST_VIEWER_WIZARD_FILTER_EMPTY_TEMPLATE"]);
  this.extFilterEmpty = this.extWizardLayer.select(CSS_FILTER_EMPTY).first();
  Event.observe(this.extFilterEmpty.dom, "click", CGListViewer.prototype.atFilterEmptyClick.bind(this));
};

CGListViewer.prototype.renderPaging = function() {
  
  this.extPaging = this.extViewerLayer.select(CSS_PAGING).first();
  this.extFirst = this.extPaging.select(CSS_PAGING_FIRST).first();
  this.extPrevious = this.extPaging.select(CSS_PAGING_PREVIOUS).first();
  this.extNext = this.extPaging.select(CSS_PAGING_NEXT).first();
  this.extLast = this.extPaging.select(CSS_PAGING_LAST).first();

  Event.observe(this.extFirst.dom, "click", CGListViewer.prototype.atPagingFirstClick.bind(this, this.extFirst.dom));
  Event.observe(this.extPrevious.dom, "click", CGListViewer.prototype.atPagingPreviousClick.bind(this, this.extPrevious.dom));
  Event.observe(this.extNext.dom, "click", CGListViewer.prototype.atPagingNextClick.bind(this, this.extNext.dom));
  Event.observe(this.extLast.dom, "click", CGListViewer.prototype.atPagingLastClick.bind(this, this.extLast.dom));
};

CGListViewer.prototype.render = function(sViewerLayer) {

  this.init(sViewerLayer);
  
  if (this.Options.Editable) {
    this.extViewerLayer.addClass(CLASS_EDITABLE);
    this.extWizardLayer.addClass(CLASS_EDITABLE);
  }
  else {
    this.extViewerLayer.removeClass(CLASS_EDITABLE);
    this.extWizardLayer.removeClass(CLASS_EDITABLE);
  } 
      
  this.extAddListSelector = this.extViewerLayer.select(CSS_ADDLIST + " " + CSS_SELECTOR + " select").first();
  Event.observe(this.extAddListSelector.dom, "change", CGListViewer.prototype.atAddItem.bind(this, this.extAddListSelector.dom));

  this.extAddListLink = this.extViewerLayer.select(CSS_ADDLIST + " " + CSS_SELECTOR + " a").first();
  Event.observe(this.extAddListLink.dom, "click", CGListViewer.prototype.atAddItem.bind(this, this.extAddListLink.dom));

  this.extDeleteItems = this.extViewerLayer.select(CSS_DELETE).first();
  if (this.extDeleteItems != null) Event.observe(this.extDeleteItems.dom, "click", CGListViewer.prototype.atDeleteItems.bind(this, this.extDeleteItems.dom));

  this.extSelectAll = this.extViewerLayer.select(CSS_SELECT_ALL).first();
  if (this.extSelectAll != null) Event.observe(this.extSelectAll.dom, "click", CGListViewer.prototype.atSelectAll.bind(this, this.extSelectAll.dom));
  
  this.extMoreSortByList = this.extWizardLayer.select(CSS_MORE).first();
  if (this.extMoreSortByList != null) {
    Event.observe(this.extMoreSortByList.dom, "click", CGListViewer.prototype.atMoreSortByList.bind(this, this.extMoreSortByList.dom));
    this.extMoreSortByList.dom.style.display = "none";
  }

  this.renderList(this.Options.AddList, LIST_ADD, CSS_ADDLIST);
  this.renderList(this.Options.SortByList, LIST_SORTBY, CSS_SORTBYLIST);
  this.renderList(this.Options.GroupByList, LIST_GROUPBY, CSS_GROUPBYLIST);
  this.renderFilter();
  this.renderPaging();
  
  this.applyState();
  this.load(true);
};

CGListViewer.prototype.initState = function() {
  var i;
  
  this.State = new Object();
  this.State.CurrentPage = 1;
  this.State.Filter = "";
  this.State.Lists = new Object();
  this.State.Lists[LIST_SORTBY] = new Object();
  this.State.Lists[LIST_SORTBY].Orders = new Array();
  this.State.Lists[LIST_SORTBY].Items = new Array();
  this.State.Lists[LIST_GROUPBY] = new Object();
  this.State.Lists[LIST_GROUPBY].Orders = new Array();
  this.State.Lists[LIST_GROUPBY].Items = new Array();
  this.State.Selection = new Array();
  
  if (this.Options.SortByList && this.Options.SortByList.Selection) {
    for (i=0; i<this.Options.SortByList.Selection.length; i++) {
      var CodeItem = this.Options.SortByList.Selection[i];
      this.State.Lists[LIST_SORTBY].Items[CodeItem] = {Code: CodeItem, Mode: MODE_ASCENDANT, Order: i};
    }
    this.State.Lists[LIST_SORTBY].Orders = this.Options.SortByList.Selection;
    
    for (i in this.Options.SortByList.Items) {
      if (isFunction(this.Options.SortByList.Items[i])) continue;
      var ItemOption = this.Options.SortByList.Items[i];
      if (this.State.Lists[LIST_SORTBY].Items[ItemOption.Code] == null) continue;
      if (ItemOption.DefaultMode) this.State.Lists[LIST_SORTBY].Items[ItemOption.Code].Mode = (ItemOption.DefaultMode.toLowerCase() == "descendant")?MODE_DESCENDANT:MODE_ASCENDANT;
    }
  }
  
  if (this.Options.GroupByList && this.Options.GroupByList.Selection) {
    for (i=0; i<this.Options.GroupByList.Selection.length; i++) {
      var aItem = this.Options.GroupByList.Selection[i];
      this.State.Lists[LIST_GROUPBY].Items[aItem[0]] = {Code: aItem[0], Value: aItem[1]};
      this.State.Lists[LIST_GROUPBY].Orders.push(aItem[0]);
    }
  }
};

CGListViewer.prototype.getState = function() {
  var extFilter;
  var Result = new Object();
  
  extFilter = this.extWizardLayer.select(CSS_FILTER).first();
  
  Result.Filter = extFilter.dom.value;
  Result.CurrentPage = this.CurrentPage;
  Result.Limit = ITEMS_PER_PAGE;
  
  Result.Sorts = new Array();
  for (var i=0; i<this.State.Lists[LIST_SORTBY].Orders.length;i++) {
    var CodeSort = this.State.Lists[LIST_SORTBY].Orders[i];
    Result.Sorts.push(this.State.Lists[LIST_SORTBY].Items[CodeSort]);
  }
  
  Result.Groups = new Array();
  for (var i=0; i<this.State.Lists[LIST_GROUPBY].Orders.length;i++) {
    var CodeGroup = this.State.Lists[LIST_GROUPBY].Orders[i];
    Result.Groups.push(this.State.Lists[LIST_GROUPBY].Items[CodeGroup]);
  }
  
  Result.Selection = this.State.Selection;
  
  return Result;
};

CGListViewer.prototype.setState = function(NewState) {
  if (NewState == null) return;
  
  this.State.CurrentPage = NewState.CurrentPage;
  this.State.Filter = NewState.Filter;
  
  this.State.Lists[LIST_SORTBY].Items = new Array();
  this.State.Lists[LIST_SORTBY].Orders = new Array();
  for (var i=0; i<NewState.Sorts.length; i++) {
    var Sort = NewState.Sorts[i];
    Sort.Order = i;
    this.State.Lists[LIST_SORTBY].Items[Sort.Code] = Sort;
    this.State.Lists[LIST_SORTBY].Orders[i] = Sort.Code;
  }
  
  this.State.Lists[LIST_GROUPBY].Items = new Array();
  this.State.Lists[LIST_GROUPBY].Orders = new Array();
  for (var i=0; i<NewState.Groups.length; i++) {
    var Group = NewState.Groups[i];
    Group.Order = this.State.Lists[LIST_GROUPBY].Orders.length;
    this.State.Lists[LIST_GROUPBY].Items[Group.Code] = Group;
    this.State.Lists[LIST_GROUPBY].Orders[i] = Group.Code;
  }
  
  this.State.Selection = NewState.Selection;
};

CGListViewer.prototype.applyState = function() {
  
  this.extFilter.dom.value = this.State.Filter;
  this.extFilterEmpty.dom.style.display = (this.State.Filter <= 0)?"block":"none";
  
  this.CurrentPage = this.State.CurrentPage;
  
  var extSortByList = this.extWizardLayer.select(CSS_SORTBYLIST + " li");
  extSortByList.each(function(extSortByItem) { this.unselectSortByItem(extSortByItem); }, this);
  
  for (var i=0; i<this.State.Lists[LIST_SORTBY].Orders.length; i++) {
    var CodeSort = this.State.Lists[LIST_SORTBY].Orders[i];
    var Sort = this.State.Lists[LIST_SORTBY].Items[CodeSort];
    var extItem;
    
    if (i==0) extItem = this.extWizardLayer.select(CSS_SORTBYLIST + " ." + Sort.Code).first();
    else {
      var extSection = this.createSortByListSection();
      extItem = extSection.select("." + Sort.Code).first();
    }
    
    if (extItem == null) continue;
    
    this.selectSortByItem(extItem);
    if (Sort.Mode == MODE_ASCENDANT) {
      extItem.addClass(CLASS_ASCENDANT);
      extItem.removeClass(CLASS_DESCENDANT);
    }
    else {
      extItem.removeClass(CLASS_ASCENDANT);
      extItem.addClass(CLASS_DESCENDANT);
    }
  }
  
  var extGroupByList = this.extWizardLayer.select(CSS_GROUPBYLIST + " li");
  extGroupByList.each(function(extGroupByItem) { this.unselectGroupByItem(extGroupByItem); }, this);
  
  var aDummyGroupsByItems = new Object();
  var aDummyGroupsByOrders = new Array();
  for (var i=0; i<this.State.Lists[LIST_GROUPBY].Orders.length;i++) {
    var CodeGroup = this.State.Lists[LIST_GROUPBY].Orders[i];
    var Group = this.State.Lists[LIST_GROUPBY].Items[CodeGroup];
    var extItem = this.extWizardLayer.select(CSS_GROUPBYLIST + " ." + Group.Code).first();
    if (extItem == null) continue;
    this.selectGroupByItem(extItem, Group.Value);
    aDummyGroupsByItems[CodeGroup] = Group;
    aDummyGroupsByOrders.push(CodeGroup);
  }
  this.State.Lists[LIST_GROUPBY].Orders = aDummyGroupsByOrders;
  this.State.Lists[LIST_GROUPBY].Items = aDummyGroupsByItems; 
  
  this.extDeleteItems.dom.style.display = "none";
};

CGListViewer.prototype.load = function(bClearItems) {
  var extItems = this.extViewerLayer.select(CSS_ITEMS).first();
  var sRequestUrl = this.Options.DataSource.RequestUrl;
  var iStart = (this.State.CurrentPage * ITEMS_PER_PAGE) - ITEMS_PER_PAGE;
  
  if (this.onUpdateState) this.onUpdateState(this.getState());
  
  if (bClearItems) extItems.dom.innerHTML = "";

  sSorts = "";
  for (var i=0; i<this.State.Lists[LIST_SORTBY].Orders.length;i++) {
    var CodeSort = this.State.Lists[LIST_SORTBY].Orders[i];
    var Sort = this.State.Lists[LIST_SORTBY].Items[CodeSort];
    if (i!=0) sSorts += LIST_VIEWER_QUERIES_SEPARATOR;
    sSorts += Sort.Code + LIST_VIEWER_QUERY_SEPARATOR + Sort.Mode;
  }
  
  sGroups = "";
  for (var i=0; i<this.State.Lists[LIST_GROUPBY].Orders.length;i++) {
    var CodeGroup = this.State.Lists[LIST_GROUPBY].Orders[i];
    var Group = this.State.Lists[LIST_GROUPBY].Items[CodeGroup];
    if (i!=0) sGroups += LIST_VIEWER_QUERIES_SEPARATOR;
    sGroups += Group.Code + LIST_VIEWER_QUERY_SEPARATOR + escape(Group.Value);
  }
  
  this.showLoading();

  var newScript = document.createElement('script');
  newScript.type = 'text/javascript';
  newScript.onload = CGListViewer.prototype.atLoadCallback.bind(this, newScript, bClearItems);
  newScript.src = sRequestUrl + "?query=" + this.State.Filter + "&sortsby=" + sSorts + "&groupsby=" + sGroups + "&start=" + iStart + "&limit=" + ITEMS_PER_PAGE;
  this.extViewerLayer.dom.appendChild(newScript);
  /*
  Ext.Ajax.request({
    url: sRequestUrl,
    params: ,
    method: "GET",
    callback: function(sOptions, bSuccess, Response){
      eval("var data = " + Response.responseText);
      this.loading = false;
      this.hideLoading();
      this.update(data, bClearItems);
    },
    failure: function(response, options) {
      alert(response.responseText);
    },
    scope: this
  });
  */
};

CGListViewer.prototype.atLoadCallback = function(newScript, bClearItems) {
  this.loading = false;
  this.hideLoading();
  this.update(data, bClearItems);
  newScript.remove();
};

CGListViewer.prototype.updateListItem = function(CodeList, Item, DOMItem) {

  Event.observe(DOMItem, "click", CGListViewer.prototype.atListItemClick.bind(this, CodeList, DOMItem));

  if (CodeList == LIST_ADD) {
    this.addItemsToDOMSelector(this.extAddListSelector.dom, [Item]);
    this.extAddListLink.dom.innerHTML = Item.Label;
    this.extAddListLink.dom.CodeItem = Item.Code;
    this.extAddListLink.dom.href = Item.Command;
  }
  if (CodeList == LIST_GROUPBY) {
    var DOMGroupBySelector = Ext.get(DOMItem).select(CSS_SELECTOR).first().dom;
    DOMGroupBySelector.Code = Item.Code;
    Event.observe(DOMGroupBySelector, "change", CGListViewer.prototype.atGroupByItemOptionClick.bind(this, DOMGroupBySelector));
    this.addItemsToDOMSelector(DOMGroupBySelector, Item.Options);
  }
};

CGListViewer.prototype.updateList = function(List, CodeList, ListStyle, sSeparator) {
  var ListItemTemplate = null;
  var extGroup;
  var DOMLayer;
  
  if (this.extWizardLayer == null) return;

  extGroup = this.extWizardLayer.select(CSS_GROUP + "." + CodeList).first();
  if (List == null) {
    var extElement = extGroup;
    if (CodeList == LIST_GROUPBY) extElement = this.extWizardLayer.select(ListStyle).first();
    extElement.dom.style.display = "none";
    return;
  }

  if (this.extWizardLayer == null) return;
  if (! List.bUpdate) return;
  
  DOMList = this.extWizardLayer.select(ListStyle).first().dom;
  List.Code = CodeList;
  
  if (CodeList == LIST_GROUPBY) ListItemTemplate = new Template(aTemplates[this.language]["LIST_VIEWER_WIZARD_GROUPBYLIST_ITEM_TEMPLATE"]);
  else if (CodeList == LIST_SORTBY) ListItemTemplate = new Template(aTemplates[this.language]["LIST_VIEWER_WIZARD_SORTBYLIST_ITEM_TEMPLATE"]);
  else if (CodeList == LIST_ADD) {
    ListItemTemplate = new Template(aTemplates[this.language]["LIST_VIEWER_WIZARD_ADDLIST_ITEM_TEMPLATE"]);
    this.extAddListSelector.dom.innerHTML = "<option value='-1'>&nbsp;</option>";
    this.extAddListLink.dom.innerHTML = "";
  }

  DOMList.innerHTML = "";
  DOMLayer = DOMList;
  if (List.Code == LIST_SORTBY) DOMLayer = new Insertion.Bottom(DOMList, aTemplates[this.language]["LIST_VIEWER_WIZARD_SORTBYLIST_SECTION_TEMPLATE"]).element.immediateDescendants().last();
  
  List.CountItems = 0;
  for(var Code in List.Items) {
    if (isFunction(List.Items[Code])) continue;
    var Item = List.Items[Code];
    var DOMItem = new Insertion.Bottom(DOMLayer, ListItemTemplate.evaluate({"code": Item.Code, "label": Item.Label, "description": Item.Description, "command": Item.Command?Item.Command:"#"})).element.immediateDescendants().last();
    DOMItem.Code = Item.Code;
    this.updateListItem(List.Code, Item, DOMItem);
    List.CountItems++;
  }

  if (CodeList == LIST_ADD) {
    if (List.CountItems > 1) {
      this.extAddListSelector.dom.style.display = "inline";
      this.extAddListLink.dom.style.display = "none";
      this.extDeleteItems.dom.addClassName("top");
    }
    else {
      this.extAddListSelector.dom.style.display = "none";
      this.extAddListLink.dom.style.display = "inline";
      this.extDeleteItems.dom.removeClassName("top");
    }
  }
  
  extGroup.dom.style.display = ((List.CountItems > 0) || (CodeList == LIST_GROUPBY))?"block":"none";
  List.bUpdate = false;
};

CGListViewer.prototype.updateLabel = function() {
  var extLabel = this.extViewerLayer.select(CSS_LABEL).first();
  extLabel.dom.style.display = (this.sLabel != null && this.sLabel != "")?"block":"none";
  extLabel.dom.innerHTML = this.sLabel;    
};

CGListViewer.prototype.updateSummary = function() {
  var CountTemplate = new Template(this.Options.Templates.CountItems?this.Options.Templates.CountItems:aTemplates[this.language]["LIST_VIEWER_DEFAULT_COUNT_ITEMS_TEMPLATE"]);
  var extCount = this.extViewerLayer.select(CSS_SUMMARY + " " + CSS_COUNT).first();
  extCount.dom.innerHTML = CountTemplate.evaluate({"count" : this.data.nrows});
  extCount.dom.style.display = this.data.nrows == 0?"none":"block";
};

CGListViewer.prototype.updateFilters = function() {
  var extFilter = this.extWizardLayer.select(CSS_FILTER).first();
  var extFilters = this.extViewerLayer.select(CSS_FILTERS).first();
  var FilterTemplate = new Template(aTemplates[this.language]["LIST_VIEWER_FILTER_TEMPLATE"]);
  var extUnselectList, DOMFilter;

  extFilters.dom.innerHTML = "";
  
  if (extFilter.dom.value != "") {
    DOMFilter = new Insertion.Bottom(extFilters.dom, FilterTemplate.evaluate({"code": CODE_WORDS_FILTER, "label": extFilter.dom.value})).element.immediateDescendants().last();
    DOMFilter.Code = CODE_WORDS_FILTER;
  }
  
  for (var i=0; i<this.State.Lists[LIST_GROUPBY].Orders.length;i++) {
    var CodeGroup = this.State.Lists[LIST_GROUPBY].Orders[i];
    var extItem = this.extWizardLayer.select(CSS_GROUPBYLIST + " ." + CodeGroup).first();
    var extItemSelector = extItem.select(CSS_SELECTOR).first();
    if(extItemSelector.dom.selectedIndex == -1) continue;
    var optionValue = extItemSelector.dom.options[extItemSelector.dom.selectedIndex].value;
    if (optionValue == "all") continue;
    if (extFilters.dom.innerHTML == "") extFilters.dom.innerHTML = aTemplates[this.language]["LIST_VIEWER_FILTERS_MESSAGE_TEMPLATE"];
    DOMFilter = new Insertion.Bottom(extFilters.dom, FilterTemplate.evaluate({"code": extItem.Code, "label": extItemSelector.dom.options[extItemSelector.dom.selectedIndex].text})).element.immediateDescendants().last();
    DOMFilter.Code = extItem.dom.Code;
  }
  
  extUnselectList = extFilters.select(CSS_UNSELECT);
  extUnselectList.each(function(extUnselect) {
    var extFilter = extUnselect.up(".filter");
    extUnselect.dom.Code = extFilter.dom.Code;
    Event.observe(extUnselect.dom, "click", CGListViewer.prototype.atUnselectFilter.bind(this, extUnselect.dom));
  }, this);
};

CGListViewer.prototype.updateItems = function(bClearItems) {
  var extItems = this.extViewerLayer.select(CSS_ITEMS).first();
  var ItemTemplate = new Template(aTemplates[this.language]["LIST_VIEWER_ITEM_TEMPLATE"]);
  var sContentTemplate = this.Options.Templates.Item?htmlDecode(this.Options.Templates.Item):null;
  var sNoItemsTemplate = this.Options.Templates.NoItems?htmlDecode(this.Options.Templates.NoItems):null;
  var ContentTemplate, NoItemsTemplate;
  
  if (this.data == null) return;
  
  if (sContentTemplate == null) sContentTemplate = aTemplates[this.language]["LIST_VIEWER_DEFAULT_ITEM_CONTENT_TEMPLATE"];
  ContentTemplate = new Template(sContentTemplate);

  if (sNoItemsTemplate == null) sNoItemsTemplate = aTemplates[this.language]["LIST_VIEWER_DEFAULT_NO_ITEMS_TEMPLATE"];
  NoItemsTemplate = new Template(sNoItemsTemplate);

  this.extSelectAll.dom.style.visibility = (this.data.nrows > 0)?"visible":"hidden";
  this.iNumPages = (this.data.nrows <= ITEMS_PER_PAGE)?0:Math.floor(this.data.nrows/ITEMS_PER_PAGE);
  if ((this.data.nrows % ITEMS_PER_PAGE) != 0) this.iNumPages++;
  
  if (this.data.nrows == 0) {
    extItems.dom.innerHTML = NoItemsTemplate.evaluate();
    return;
  }
  
  if ((this.data.rows.length == 0) && (this.State.CurrentPage >= 2)) {
    this.State.CurrentPage--;
    this.load(bClearItems);
    return;
  }

  var IdActiveItem = (this.State.ActiveItemId != null)?this.State.ActiveItemId:null;
  if (IdActiveItem == null) IdActiveItem = (this.DOMActiveItem != null)?this.DOMActiveItem.Id:null;
  
  for (var i=0; i<this.data.rows.length;i++) {
    var Item = this.data.rows[i];
    Item.position = i;
    
    if (this.onBoundItem) this.onBoundItem(this, Item);
    
    var sContent = ContentTemplate.evaluate(Item);
    var extItem = Ext.get(new Insertion.Bottom(extItems.dom, ItemTemplate.evaluate({"content":sContent})).element.immediateDescendants().last());
    var extContent = extItem.select(CSS_CONTENT).first();
    var extSelector = extItem.select(CSS_SELECTOR).first();
    var extDelete = extItem.select(CSS_DELETE).first();
    
    Item.id = (Item.id != null)?Item.id:i;
    extItem.dom.Id = Item.id;
    extItem.dom.addClassName(Item.id);
    Event.observe(extContent.dom, "click", CGListViewer.prototype.atItemContentClick.bind(this, extItem.dom, extContent.dom));
    Event.observe(extSelector.dom, "click", CGListViewer.prototype.atItemSelectorClick.bind(this, extItem.dom, extSelector.dom));
    Event.observe(extDelete.dom, "click", CGListViewer.prototype.atItemDeleteClick.bind(this, extItem.dom, extDelete.dom));
    
    if (Item.css)
      extContent.dom.addClassName(Item.css);
    
    if (IdActiveItem != null && Item.id == IdActiveItem) {
      this.activateItem(IdActiveItem);
    }

    if (this.State.Selection[Item.id]) {
      extSelector.dom.checked = true;
      extItem.dom.addClassName(CLASS_SELECTED);
      if (this.extViewerLayer.hasClass(CLASS_EDITABLE)) this.extDeleteItems.dom.style.display = "block";
      if (this.onSelectItem) this.onSelectItem(this, Item.id);
    }
  }  
  
};

CGListViewer.prototype.updatePaging = function() {
  
  this.extPaging.dom.style.display = "none";
  return;
  
  if (this.iNumPages == 0) {
    this.extPaging.hide();
    return;
  }

  if (this.State.CurrentPage == 1 && this.iNumPages == 1) {
    this.extPaging.hide();
    return;
  }
  
  this.extPaging.show();

  if (this.State.CurrentPage == 1) {
    this.extFirst.dom.addClassName(CLASS_DISABLED);
    this.extPrevious.dom.addClassName(CLASS_DISABLED);
  }
  else {
    this.extFirst.dom.removeClassName(CLASS_DISABLED);
    this.extPrevious.dom.removeClassName(CLASS_DISABLED);
  }

  if (this.State.CurrentPage == this.iNumPages) {
    this.extLast.dom.addClassName(CLASS_DISABLED);
    this.extNext.dom.addClassName(CLASS_DISABLED);
  }
  else {
    this.extLast.dom.removeClassName(CLASS_DISABLED);
    this.extNext.dom.removeClassName(CLASS_DISABLED);
  }
};

CGListViewer.prototype.update = function(data, bClearItems) {
  if (this.extViewerLayer == null) return;
  if (this.extWizardLayer == null) return;
  
  this.data = data;
  this.updateList(this.Options.AddList, LIST_ADD, CSS_ADDLIST, "");
  this.updateList(this.Options.SortByList, LIST_SORTBY, CSS_SORTBYLIST, ",&nbsp;");
  this.updateList(this.Options.GroupByList, LIST_GROUPBY, CSS_GROUPBYLIST, ",&nbsp;");
  this.updateLabel();
  this.updateSummary();
  this.updateFilters();
  this.updateItems(bClearItems);
  this.updatePaging();
  
  this.isRefreshing = false;
  if (this.onRefresh) this.onRefresh(this);
};

CGListViewer.prototype.refresh = function() {
  if (this.isRefreshing) return;
  this.isRefreshing = true;
  this.applyState();
  this.load(true);
};

CGListViewer.prototype.unselectSortByItem = function(extItem) {
  var extSortByOptions = extItem.select(CSS_SORTBYOPTIONS).first();
  extItem.removeClass(CLASS_SELECTED);
  extItem.removeClass(CLASS_ASCENDANT);
  extItem.removeClass(CLASS_DESCENDANT);
  extItem.addClass(CLASS_ASCENDANT);
  if (extSortByOptions != null) extSortByOptions.dom.style.display = "none";
};

CGListViewer.prototype.removeSortByListSectionsFromItem = function(extItem) {
  var extSections = this.extWizardLayer.select(CSS_LIST_SECTION);
  var extLastSection;
  
  for (var i=0; i<extSections.getCount(); i++) {
    var CodeSort = this.State.Lists[LIST_SORTBY].Orders[i];
    if (CodeSort == null) {
      var extSection = extSections.elements[i];
      if (extSection) extSection.remove();
    }
  }

  extSections = this.extWizardLayer.select(CSS_LIST_SECTION);
  extLastSection = extSections.last();
  if (extLastSection) extLastSection.removeClass(CLASS_READONLY);
  this.extMoreSortByList.dom.style.display = (extSections.getCount() == this.Options.SortByList.CountItems)?"none":"block";
};

CGListViewer.prototype.createSortByListSection = function() {
  var DOMList = this.extWizardLayer.select(CSS_SORTBYLIST).first().dom;
  var extSections = Ext.get(DOMList).select(CSS_LIST_SECTION);
  var iNumSections = extSections.getCount();
  var extLastSection = extSections.last();
  var extLayer = Ext.get(new Insertion.Bottom(DOMList, aTemplates[this.language]["LIST_VIEWER_WIZARD_SORTBYLIST_SECTION_TEMPLATE"]).element.immediateDescendants().last());
  var ListItemTemplate = new Template(aTemplates[this.language]["LIST_VIEWER_WIZARD_SORTBYLIST_ITEM_TEMPLATE"]);

  if (extLastSection) extLastSection.addClass(CLASS_READONLY);
  this.extMoreSortByList.dom.style.display = "none"; 
  
  var extSortByOptions = extLayer.select(CSS_SORTBYOPTIONS).first();
  if (extSortByOptions == null) {
    new Insertion.Bottom(extLayer.dom, new Template(aTemplates[this.language]["LIST_VIEWER_WIZARD_SORTBYLIST_SECTION_OPTIONS_TEMPLATE"]).evaluate());
    extSortByOptions = extLayer.select(CSS_SORTBYOPTIONS).first();
    var extSortByOptionsUnselect = extLayer.select(CSS_SORTBYOPTIONS + " " + CSS_UNSELECT).first();
    Event.observe(extSortByOptionsUnselect.dom, "click", CGListViewer.prototype.atSortBySectionUnselectClick.bind(this, extLayer.dom));
  }
  extSortByOptions.show();
  
  var i = 0;
  for (var Code in this.Options.SortByList.Items) {
    if (isFunction(this.Options.SortByList.Items[Code])) continue;
    var Item = this.Options.SortByList.Items[Code];
    var StateItem = this.State.Lists[LIST_SORTBY].Items[Code];
    var DOMItem = new Insertion.Bottom(extLayer.dom, ListItemTemplate.evaluate({"code": Item.Code, "label": Item.Label, "description": Item.Description})).element.immediateDescendants().last();
    DOMItem.Code = Item.Code;
    DOMItem.addClassName(CLASS_ASCENDANT);
    if (StateItem != null && StateItem.Order <= iNumSections) DOMItem.addClassName(CLASS_READONLY);
    Event.observe(DOMItem, "click", CGListViewer.prototype.atListItemClick.bind(this, LIST_SORTBY, DOMItem));
    i++;
  }
  
  return extLayer;
};

CGListViewer.prototype.removeSortByListSection = function(DOMSection) {
  DOMSection.remove();
  
  var extLastSection = this.extWizardLayer.select(CSS_LIST_SECTION).last();
  if (extLastSection) {
    extLastSection.removeClass(CLASS_READONLY);
    var extSelectedItem = extLastSection.select(CSS_SELECTED).first();
    this.extMoreSortByList.dom.style.display = (extSelectedItem != null)?"block":"none";
  }
  else this.extMoreSortByList.dom.style.display = "block";
};

CGListViewer.prototype.selectSortByItem = function(extItem) {
  this.removeSortByListSectionsFromItem(extItem);
  if (!extItem.hasClass(CLASS_SELECTED)) extItem.addClass(CLASS_SELECTED);
  if (extItem.hasClass(CLASS_READONLY)) extItem.removeClass(CLASS_READONLY);
};

CGListViewer.prototype.toggleSortByItemMode = function(extItem) {
  var DOMItem = extItem.dom;
  var Sort = this.State.Lists[LIST_SORTBY].Items[DOMItem.Code];

  DOMItem.toggleClassName(CLASS_ASCENDANT);
  DOMItem.toggleClassName(CLASS_DESCENDANT);
  Sort.Mode = DOMItem.hasClassName(CLASS_ASCENDANT)?MODE_ASCENDANT:MODE_DESCENDANT;
};

CGListViewer.prototype.unselectGroupByItem = function(extItem) {
  var extItemSelector = extItem.select(CSS_SELECTOR).first();
  if (extItemSelector) extItemSelector.dom.selectedIndex = 0;
};

CGListViewer.prototype.selectGroupByItem = function(extItem, sValue) {
  var extItemSelector = extItem.select(CSS_SELECTOR).first();
  var index = -1;
  
  for (var i=0; i<extItemSelector.dom.options.length; i++) {
    var extOption = extItemSelector.dom.options[i];
    if (extOption.value == sValue) index = i;
  }
  extItemSelector.dom.selectedIndex = index;
};

CGListViewer.prototype.clearFilter = function() {
  this.State.Filter = "";
  this.extFilter.dom.value = "";
  this.extFilterEmpty.dom.style.display = "block";
  this.load(true);
};

CGListViewer.prototype.filter = function() {
  this.State.Filter = this.extFilter.dom.value;
  this.extFilterEmpty.dom.style.display = (this.State.Filter.length<=0)?"block":"none";
  this.load(true);
};

CGListViewer.prototype.addItem = function(Item) {
  if (this.onAddItem) this.onAddItem(this, Item);
};

CGListViewer.prototype.sortByItem = function(DOMItem) {
  var DOMSection = DOMItem.up(CSS_LIST_SECTION);
  var extSelected = Ext.get(DOMSection).select(CSS_SELECTED).first();
  
  this.State.CurrentPage = 1;
  
  if (DOMItem.hasClassName(CLASS_SELECTED)) {
    this.toggleSortByItemMode(Ext.get(DOMItem));
    this.load(true);
    return;
  }
  
  if (extSelected != null) {
    this.removeSortByListSectionsFromItem(extSelected);
    this.removeItemOnSelection(LIST_SORTBY, extSelected.dom.Code);
    this.unselectSortByItem(extSelected);
  }
  
  this.addItemOnSelection(LIST_SORTBY, {Code: DOMItem.Code, Mode: DOMItem.hasClassName(CLASS_ASCENDANT)?MODE_ASCENDANT:MODE_DESCENDANT});
  this.selectSortByItem(Ext.get(DOMItem));
  this.load(true);
};

CGListViewer.prototype.getSelectedItems = function() {
  var aExtSelectorItems = this.extViewerLayer.select(CSS_ITEM_SELECTOR);
  var aResult = new Array();
  
  aExtSelectorItems.each(function(extItemSelector) { 
    if (!extItemSelector.dom.checked) return;
    var DOMItem = extItemSelector.dom.up(CSS_ITEM);
    aResult[DOMItem.Id] = DOMItem.Id;
  }, this);
  
  return aResult;
};

CGListViewer.prototype.getItem = function(Id) {
  for (var i=0;i<this.data.rows.length;i++) {
    if (this.data.rows[i].id == Id) {
      return this.data.rows[i];
    };
  }
  return null;
};

CGListViewer.prototype.updateItem = function(Item) {
  var sContentTemplate = this.Options.Templates.Item?htmlDecode(this.Options.Templates.Item):null;
  var ContentTemplate = null;
  var DOMItem = this.getDOMItem(Item.id);
  
  for (var i=0;i<this.data.rows.length;i++) {
    if (this.data.rows[i].id == Item.id) {
      this.data.rows[i] = Item;
      break;
    };
  }

  if (this.onBoundItem) this.onBoundItem(this, Item);

  if (sContentTemplate == null) sContentTemplate = aTemplates[this.language]["LIST_VIEWER_DEFAULT_ITEM_CONTENT_TEMPLATE"];
  ContentTemplate = new Template(sContentTemplate);
  
  var extItem = Ext.get(DOMItem);
  var sContent = ContentTemplate.evaluate(Item);
  var extContent = extItem.select(CSS_CONTENT).first();
  extContent.dom.innerHTML = sContent; 
};

CGListViewer.prototype.getDOMItem = function(Id) {
  var extItems = this.extViewerLayer.select(CSS_ITEMS).first();
  var extItemList = extItems.select(CSS_ITEM);
  var DOMResult = null;
  
  extItemList.each(function(extItem) {
    if (extItem.dom.Id == Id) DOMResult = extItem.dom;
  }, this);
  
  return DOMResult;
};

CGListViewer.prototype.getActiveItem = function() {
  if (this.DOMActiveItem == null) return null;
  
  return this.getItem(this.DOMActiveItem.Id);
};

CGListViewer.prototype.setActiveItem = function(Id) {
  this.State.ActiveItemId = Id;
};

CGListViewer.prototype.activateItem = function(Id) {
  var DOMItem = this.getDOMItem(Id);

  if (DOMItem == null) return;
  
  var extItem = Ext.get(DOMItem);
  var extContent = extItem.select(CSS_CONTENT).first();
  extContent.dom.click();
};

CGListViewer.prototype.selectAll = function() {
  var aExtSelectorItems = this.extViewerLayer.select(CSS_ITEM_SELECTOR);
  var aItems = new Array();
  aExtSelectorItems.each(function(extItemSelector) { 
    if (extItemSelector.dom.checked) return;
    extItemSelector.dom.checked = true;
    var DOMItem = extItemSelector.dom.up(CSS_ITEM);
    DOMItem.addClassName(CLASS_SELECTED);
    aItems[DOMItem.Id] = DOMItem.Id;
  }, this);
  this.extDeleteItems.dom.style.display = "block";
  this.State.Selection = this.getSelectedItems();
  if (this.onSelectAllItems) this.onSelectAllItems(this, aItems);
};

CGListViewer.prototype.unselectAll = function() {
  var aExtSelectorItems = this.extViewerLayer.select(CSS_ITEM_SELECTOR);
  var aItems = new Array();
  aExtSelectorItems.each(function(extItemSelector) { 
    if (!extItemSelector.dom.checked) return;
    extItemSelector.dom.checked = false;
    var DOMItem = extItemSelector.dom.up(CSS_ITEM);
    DOMItem.removeClassName(CLASS_SELECTED);
    aItems[DOMItem.Id] = DOMItem.Id;
  }, this);
  this.extDeleteItems.dom.style.display = "none";
  this.State.Selection = this.getSelectedItems();
  if (this.onUnselectAllItems) this.onUnselectAllItems(this, aItems);
};

CGListViewer.prototype.firstPage = function() {
  this.State.CurrentPage = 1;
  this.load(false);
};

CGListViewer.prototype.previousPage = function() {
  this.State.CurrentPage--;
  if (this.State.CurrentPage < 1) this.State.CurrentPage = 1;
  this.load(false);
};

CGListViewer.prototype.nextPage = function() {
  this.State.CurrentPage++;
  
  if (this.State.CurrentPage > this.iNumPages) {
    this.State.CurrentPage = this.iNumPages;
    return;
  }
  
  this.load(false);
};

CGListViewer.prototype.lastPage = function() {
  this.State.CurrentPage = this.iNumPages;
  this.load(false);
};

CGListViewer.prototype.setAddList = function(AddList) {
  this.Options.AddList = AddList;
  this.Options.AddList.bUpdate = true;
};

CGListViewer.prototype.setSortByList = function(SortByList) {
  this.Options.SortByList = SortByList;
  this.Options.SortByList.bUpdate = true;
};

CGListViewer.prototype.setGroupByList = function(GroupByList) {
  this.Options.GroupByList = GroupByList;
  this.Options.GroupByList.bUpdate = true;
};

CGListViewer.prototype.setBaseUrl = function(sUrl) {
  this.Options.DataSource.Remote = true;
  this.Options.DataSource.RequestUrl = sUrl; 
};

CGListViewer.prototype.dispose = function() {
  Event.stopObserving(this.extFilter.dom, "keyup", CGListViewer.prototype.atFilterKeyUp.bind(this));
  Event.stopObserving(this.extFilter.dom, "focus", CGListViewer.prototype.atFilterFocus.bind(this));
  Event.stopObserving(this.extFilter.dom, "blur", CGListViewer.prototype.atFilterBlur.bind(this));
  Event.stopObserving(this.extFirst.dom, "click", CGListViewer.prototype.atPagingFirstClick.bind(this, this.extFirst.dom));
  Event.stopObserving(this.extPrevious.dom, "click", CGListViewer.prototype.atPagingPreviousClick.bind(this, this.extPrevious.dom));
  Event.stopObserving(this.extNext.dom, "click", CGListViewer.prototype.atPagingNextClick.bind(this, this.extNext.dom));
  Event.stopObserving(this.extLast.dom, "click", CGListViewer.prototype.atPagingLastClick.bind(this, this.extLast.dom));
  
  if (this.extViewerLayer) {
    var extSelectAll = this.extViewerLayer.select(CSS_SELECT_ALL).first();
    if (extSelectAll != null) Event.stopObserving(extSelectAll.dom, "click", CGListViewer.prototype.atSelectAll.bind(this, extSelectAll.dom));
  }
  
  this.extWizardLayer = null;
  this.extViewerLayer = null;
};

CGListViewer.prototype.atAddItem = function(DOMElement, EventLaunched) {
  var tagName = DOMElement.tagName.toLowerCase();
  if (tagName == "select") {
    if (DOMElement.selectedIndex == 0) return false;
    var DOMOption = DOMElement.options[DOMElement.selectedIndex];
    this.addItem({Code: DOMOption.value, Command: DOMOption.name});
  }
  else if (tagName == "a") {
    this.addItem({Code: DOMElement.CodeItem, Command: DOMElement.href});
  }
  Event.stop(EventLaunched);
  return false;
};

CGListViewer.prototype.atDeleteItems = function() {
  if (this.onDeleteItems) this.onDeleteItems(this.getSelectedItems());
  this.extSelectAll.dom.checked = false;
};

CGListViewer.prototype.atListItemClick = function(CodeList, DOMItem, EventLaunched) {
  var href = DOMItem.href;
  
  if (!href) {
    var DOMAnchor = DOMItem.down("a");
    if (!DOMAnchor) return;
    href = DOMAnchor.href;
  }
  
  if (CodeList == LIST_ADD) this.addItem({Code: DOMItem.Code, Command: href});
  else if (CodeList == LIST_SORTBY) this.sortByItem(DOMItem);
  
  Event.stop(EventLaunched);
  return false;
};

CGListViewer.prototype.atGroupByItemOptionClick = function(DOMSelector) {
  this.State.CurrentPage = 1;
  if (DOMSelector.selectedIndex == 0) this.removeItemOnSelection(LIST_GROUPBY, DOMSelector.Code);
  else this.addItemOnSelection(LIST_GROUPBY, {Code: DOMSelector.Code, Value: DOMSelector.options[DOMSelector.selectedIndex].value});
  this.load(true);
};

CGListViewer.prototype.atSortBySectionUnselectClick = function(DOMSection, EventLaunched) {
  var extSection = Ext.get(DOMSection);
  var extSelectedItem = extSection.select(CSS_SELECTED).first();

  if (extSelectedItem != null) {
    extSelectedItem.removeClass(CLASS_SELECTED);
    this.removeItemOnSelection(LIST_SORTBY, extSelectedItem.dom.Code);
    this.removeSortByListSectionsFromItem(extSelectedItem);
  }
  else this.removeSortByListSection(DOMSection);
  
  this.load(true);
  
  Event.stop(EventLaunched);
};

CGListViewer.prototype.atFilterKeyUp = function (oEvent) {
  var codeKey = oEvent.keyCode;
  var sFilter = this.extFilter.dom.value;
  
  if ((codeKey == KEY_UP) || (codeKey == KEY_DOWN) || (codeKey == KEY_ENTER) || (codeKey == KEY_LEFT) || (codeKey == KEY_RIGHT) || (codeKey == KEY_CONTROL) || (codeKey == KEY_SHIFT)) return;
  if (sFilter != "" && sFilter.length < 3) return;

  window.clearTimeout(this.idTimeoutFilter);
  this.idTimeoutFilter = window.setTimeout(this.filter.bind(this), 500);
};

CGListViewer.prototype.atFilterFocus = function () {
  this.extFilterEmpty.dom.style.display = "none";
  this.extFilter.dom.select();
};

CGListViewer.prototype.atFilterBlur = function () {
  var sValue = this.extFilter.dom.value;
  this.extFilterEmpty.dom.style.display = (sValue.length<=0)?"block":"none";
};

CGListViewer.prototype.atFilterEmptyClick = function () {
  this.extFilter.focus();
};

CGListViewer.prototype.atSelectAll = function (DOMSelectAll) {
  if (DOMSelectAll.checked) this.selectAll();
  else this.unselectAll();
};

CGListViewer.prototype.atPagingFirstClick = function (DOMPagingItem) {
  if (DOMPagingItem.hasClassName(CLASS_DISABLED)) return true;
  this.firstPage();
};

CGListViewer.prototype.atPagingPreviousClick = function (DOMPagingItem) {
  if (DOMPagingItem.hasClassName(CLASS_DISABLED)) return true;
  this.previousPage();
};

CGListViewer.prototype.atPagingNextClick = function (DOMPagingItem) {
  if (DOMPagingItem.hasClassName(CLASS_DISABLED)) return true;
  this.nextPage();
};

CGListViewer.prototype.atPagingLastClick = function (DOMPagingItem) {
  if (DOMPagingItem.hasClassName(CLASS_DISABLED)) return true;
  this.lastPage();
};

CGListViewer.prototype.atItemContentClick = function(DOMItem, DOMContent, EventLaunched) {
  
  if (this.DOMActiveItem != null) this.DOMActiveItem.removeClassName(CLASS_ACTIVE);
  
  this.DOMActiveItem = DOMItem;
  this.DOMActiveItem.addClassName(CLASS_ACTIVE);
  
  if (this.Options.Templates.ShowItemCommand != null) {
    var CommandTemplate = new Template(this.Options.Templates.ShowItemCommand);
    window.location.href = CommandTemplate.evaluate({"id": DOMItem.Id});
    return false;
  }
  
  var Item = null;
  for (var i=0;i<this.data.rows.length;i++) {
    if (this.data.rows[i].id == DOMItem.Id) {
      Item = this.data.rows[i];
      break;
    }
  }
  
  if (this.onShowItem) this.onShowItem(this, Item);
};

CGListViewer.prototype.atItemSelectorClick = function(DOMItem, DOMSelector, EventLaunched) {
  if (DOMSelector.checked) {
    DOMItem.addClassName(CLASS_SELECTED);
    if (this.onSelectItem) this.onSelectItem(this, DOMItem.Id);
  }
  else {
    DOMItem.removeClassName(CLASS_SELECTED);
    if (this.onUnselectItem) this.onUnselectItem(this, DOMItem.Id);
  }
  this.State.Selection = this.getSelectedItems();
  this.extDeleteItems.dom.style.display = (this.State.Selection.size() > 0)?"block":"none";
};

CGListViewer.prototype.atItemDeleteClick = function(DOMItem, DOMDelete, EventLaunched) {
  if (this.onDeleteItem) this.onDeleteItem(this, DOMItem.Id);
  Event.stop(EventLaunched);
};

CGListViewer.prototype.atUnselectFilter = function(DOMUnselect) {
  if (DOMUnselect.Code == CODE_WORDS_FILTER) { this.clearFilter(); }
  else {
    var extItem = this.extWizardLayer.select(CSS_GROUPBYLIST + " ." + DOMUnselect.Code).first();
    this.removeItemOnSelection(LIST_GROUPBY, extItem.dom.Code);
    this.unselectGroupByItem(extItem);
    this.load(true);
  }
};

CGListViewer.prototype.atMoreSortByList = function() {
  this.createSortByListSection();
};

CGListViewer.prototype.atScroll = function() {
  if(this.extScrollParent.dom.scrollHeight - this.extScrollParent.dom.clientHeight - this.extScrollParent.dom.scrollTop < 200 && !this.loading) {
    this.loading = true;
    this.nextPage();
  }
};
