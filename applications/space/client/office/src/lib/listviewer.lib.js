var aListViewerTemplates = new Array();
aListViewerTemplates["es"] = new Array();
aListViewerTemplates["es"]["MAIN"] = "<div class='label'></div><div class='summary'><ul class='filters'></ul><div style='float:right;'><div class='count'></div><div class='folder list'><div class='selector'><select style='width:200px;margin-top:-2px;'></select></div></div></div></div><table class='toolbar' width='100%'><tr><td class='edition' width='18px'><input type='checkbox' class='selectall'/></td><td width='100%'><div class='add list'><div class='edition selector'>Añadir&nbsp;<select style='width:200px;margin-top:-2px;font-size:14px;'></select><a href='javascript:void(null)'></a></div><a class='edition delete' style='display:none;'>Eliminar elementos seleccionados</a></div></td></tr></table><div class='layer'><ul class='items'></ul><ul class='operations'></ul><div class='lwloading'><p>Cargando...</p></div></div><div class='paging'><a class='first'>primero</a><a class='previous'>anterior</a><a class='next'>siguiente</a><a class='last'>último</a></div>";
aListViewerTemplates["es"]["ITEM"] = "<li class='item'><table width='100%'><tr><td width='18px' class='edition'><input type='checkbox' class='selector'/></td><td width='100%'><a class='content' href='javascript:void(null)'>#{content}</a></td><td><img class='locked' src='#{imagesPath}/s.gif' title='el objeto no se puede eliminar'/><a class='delete edition' href='javascript:void(null)'></a></td></tr></table></li>";
aListViewerTemplates["es"]["GROUP_ITEM"] = "<li class='itemgroup'><table width='100%'><tr><td width='18px' class='edition'></td><td width='100%'><div class='label'>#{label}</div></td><td></td></tr></table></li>";
aListViewerTemplates["es"]["OPERATION"] = "<li class='operation visible_#{visible}'><a href='javascript:void(null)'>#{label}</a></li>";
aListViewerTemplates["es"]["DEFAULT_ITEM_CONTENT"] = "<div class='item' style='display:none;'><div class='label'>#{label}</div><div class='description'>#{description}</div></div></div>";
aListViewerTemplates["es"]["DEFAULT_NO_ITEMS"] = "<div style='margin: 10px 0px;'>No existen elementos</div>";
aListViewerTemplates["es"]["DEFAULT_COUNT_ITEMS"] = "#{count} elementos";
aListViewerTemplates["es"]["FILTERS_MESSAGE"] = "<li style='margin-right:5px;'>Filtrando:&nbsp;</li>";
aListViewerTemplates["es"]["FILTER"] = "<li class='filter #{code}'><span>#{label}</span>&nbsp;<a class='unselect' href='javascript:void(null)' alt='quitar'></a></li>";
aListViewerTemplates["es"]["WIZARD"] = "<div class='separator'><div class='group groupby'><label>Mostrar</label><table class='filter'><tr><td class='content'><input type='text' class='value'/></td></tr><tr><td class='filtertitle'><input id='listviewerwizardbyTitle' type='checkbox'/><label for='listviewerwizardbyTitle'>filtrar solo por título</label></td></tr></table><ul class='groupby list'></ul></div><div class='group sortby'><label>Ordenar por</label><ul class='sortby list'></ul><ul class='toolbar'><li><a class='more' href='javascript:void(null)'>más</a></li></ul></div><div class='group add edition'><label>Añadir</label><ul class='add list'></ul></div></div>";
aListViewerTemplates["es"]["WIZARD_FILTER_EMPTY"] = "<span class='empty'>Introduzca el texto que desee encontrar</span>";
aListViewerTemplates["es"]["WIZARD_SORTBYLIST_SECTION"] = "<ul class='section'></ul>";
aListViewerTemplates["es"]["WIZARD_SORTBYLIST_SECTION_OPTIONS"] = "<div class='sortby options'>&nbsp;<a class='unselect' href='javascript:void(null)' alt='quitar'></a></div>";
aListViewerTemplates["es"]["WIZARD_GROUPBYLIST_ITEM"] = "<li class='#{code}'><div class='label'>#{label}</div><div class='content'><select class='selector'><option value='all'></option></select></div></li>";
aListViewerTemplates["es"]["WIZARD_SORTBYLIST_ITEM"] = "<li class='#{code}'><a class='label' href='javascript:void(null)'>#{label}</a></li>";
aListViewerTemplates["es"]["WIZARD_ADDLIST_ITEM"] = "<li><a class='#{code}' href='#{command}'>#{label}</a><div class='description'>#{description}</div></li>";
aListViewerTemplates["en"] = new Array();
aListViewerTemplates["en"]["MAIN"] = "<div class='label'></div><div class='summary'><ul class='filters'></ul><div style='float:right;'><div class='count'></div><div class='folder list'><div class='selector'><select style='width:200px;margin-top:-2px;'></select></div></div></div></div><table class='toolbar' width='100%'><tr><td class='edition' width='18px'><input type='checkbox' class='selectall'/></td><td width='100%'><div class='add list'><div class='edition selector'>Add&nbsp;<select style='width:200px;margin-top:-2px;font-size:14px;'></select><a href='javascript:void(null)'></a></div><a class='edition delete' style='display:none;'>Delete selected elements</a></div></td></tr></table><div class='layer'><ul class='items'></ul><ul class='operations'></ul><div class='lwloading'><p>Loading…</p></div></div><div class='paging'><a class='first'>first</a><a class='previous'>previous</a><a class='next'>next</a><a class='last'>last</a></div>";
aListViewerTemplates["en"]["ITEM"] = "<li class='item'><table width='100%'><tr><td width='18px' class='edition'><input type='checkbox' class='selector'/></td><td width='100%'><a class='content' href='javascript:void(null)'>#{content}</a></td><td><img class='locked' src='#{imagesPath}/s.gif' title='could not delete this object'/><a class='delete edition' href='javascript:void(null)'></a></td></tr></table></li>";
aListViewerTemplates["en"]["GROUP_ITEM"] = "<li class='itemgroup'><table width='100%'><tr><td width='18px' class='edition'></td><td width='100%'><div class='label'>#{label}</div></td><td></td></tr></table></li>";
aListViewerTemplates["en"]["OPERATION"] = "<li class='operation visible_#{visible}'><a href='javascript:void(null)'>#{label}</a></li>";
aListViewerTemplates["en"]["DEFAULT_ITEM_CONTENT"] = "<div class='item' style='display:none;'><div class='label'>#{label}</div><div class='description'>#{description}</div></div></div>";
aListViewerTemplates["en"]["DEFAULT_NO_ITEMS"] = "<div style='margin: 10px 0px;'>No elements</div>";
aListViewerTemplates["en"]["DEFAULT_COUNT_ITEMS"] = "#{count} elements";
aListViewerTemplates["en"]["FILTERS_MESSAGE"] = "<li style='margin-right:5px;'>Filtering:&nbsp;</li>";
aListViewerTemplates["en"]["FILTER"] = "<li class='filter #{code}'><span>#{label}</span>&nbsp;<a class='unselect' href='javascript:void(null)' alt='delete'></a></li>";
aListViewerTemplates["en"]["WIZARD"] = "<div class='separator'><div class='group groupby'><label>Show</label><table class='filter'><tr><td class='content'><input type='text' class='value'/></td></tr><tr><td class='filtertitle'><input id='listviewerwizardbyTitle' type='checkbox'/><label for='listviewerwizardbyTitle'>filter only by title</label></td></tr></table><ul class='groupby list'></ul></div><div class='group sortby'><label>Sort by</label><ul class='sortby list'></ul><ul class='toolbar'><li><a class='more' href='javascript:void(null)'>more</a></li></ul></div><div class='group add edition'><label>Add</label><ul class='add list'></ul></div></div>";
aListViewerTemplates["en"]["WIZARD_FILTER_EMPTY"] = "<span class='empty'>Enter the text you are looking for</span>";
aListViewerTemplates["en"]["WIZARD_SORTBYLIST_SECTION"] = "<ul class='section'></ul>";
aListViewerTemplates["en"]["WIZARD_SORTBYLIST_SECTION_OPTIONS"] = "<div class='sortby options'>&nbsp;<a class='unselect' href='javascript:void(null)' alt='delete'></a></div>";
aListViewerTemplates["en"]["WIZARD_GROUPBYLIST_ITEM"] = "<li class='#{code}'><div class='label'>#{label}</div><div class='content'><select class='selector'><option value='all'></option></select></div></li>";
aListViewerTemplates["en"]["WIZARD_SORTBYLIST_ITEM"] = "<li class='#{code}'><a class='label' href='javascript:void(null)'>#{label}</a></li>";
aListViewerTemplates["en"]["WIZARD_ADDLIST_ITEM"] = "<li><a class='#{code}' href='#{command}'>#{label}</a><div class='description'>#{description}</div></li>";

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
var LIST_FOLDER = "folder";
var LIST_SORTBY = "sortby";
var LIST_GROUPBY = "groupby";
var CSS_SELECTED = ".selected";
var CSS_SELECT_ALL = ".selectall";
var CSS_DELETE = ".delete";
var CSS_LOCKED = ".locked";
var CSS_MORE = ".more";
var CSS_FILTER = ".filter input.value";
var CSS_FILTER_EMPTY = ".filter .empty";
var CSS_FILTER_BY_TITLE = ".filter .filtertitle input";
var CSS_ADDLIST = ".add.list";
var CSS_FOLDERLIST = ".folder.list";
var CSS_SORTBYLIST = ".sortby.list";
var CSS_SORTBYOPTIONS = ".sortby.options";
var CSS_GROUPBYLIST = ".groupby.list";
var CSS_SELECTOR = ".selector";
var CSS_UNSELECT = ".unselect";
var CSS_LIST_SECTION = ".section";
var CSS_ITEMS = ".items";
var CSS_OPERATIONS = ".operations";
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
var CSS_VISIBLE = ".visible";
var CSS_SUMMARY = ".summary";
var CSS_FILTERS = ".filters";
var CSS_COUNT = ".count";
var CSS_LAYER = ".layer";
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

function listViewerHtmlDecode(input) {
  if (input == null) return null;
  var e = document.createElement('div');
  e.innerHTML = input;
  if (e.childNodes.length <= 0) return input;
  return e.childNodes[0].nodeValue;
};

function CGListViewer(Options, language, imagesPath) {
  this.Options = Options;
  this.aParameters = new Object();
  this.extFilter = null;
  this.extFilterEmpty = null;
  this.extFilterByTitle = null;
  this.iNumPages = 0;
  this.itemsPerPage = (Options.itemsPerPage) ? Options.itemsPerPage : ITEMS_PER_PAGE;
  this.initState();
  this.loading = false;
  this.initialized = false;
  this.sWizardLayer = null;
  this.sName = "";
  this.sLabel = "";
  this.language = language;
  if (aListViewerTemplates[this.language] == null) this.language = "es";
  this.items = new Array();
  this.imagesPath = imagesPath;
  this.groupByWidgets = new Array();
};

CGListViewer.prototype.setWizardLayer = function (sWizardLayer) {
  this.sWizardLayer = sWizardLayer;
};

CGListViewer.prototype.getName = function () {
  return this.sName;
};

CGListViewer.prototype.setName = function (sName) {
  this.sName = sName;
};

CGListViewer.prototype.getLabel = function () {
  return this.sLabel;
};

CGListViewer.prototype.setLabel = function (sLabel) {
  this.sLabel = sLabel;
};

CGListViewer.prototype.init = function (sViewerLayer) {

  if (this.initialized) return;
  if (sViewerLayer == null) return;

  this.extViewerLayer = Ext.get(sViewerLayer);
  this.extViewerLayer.dom.innerHTML = aListViewerTemplates[this.language]["MAIN"];
  this.extViewerLayer.addClass(CLASS_LISTVIEWER);

  if (this.sWizardLayer == null) {
    var extSummary = this.extViewerLayer.select(CSS_SUMMARY).first();
    this.sWizardLayer = Ext.id();
    new Insertion.Before(extSummary.dom, "<div id='" + this.sWizardLayer + "'></div>");
  }

  this.extWizardLayer = Ext.get(this.sWizardLayer);

  if (this.extWizardLayer != null) {
    this.extWizardLayer.dom.innerHTML = aListViewerTemplates[this.language]["WIZARD"];
    this.extWizardLayer.addClass(CLASS_LISTVIEWER_WIZARD);
  }

  this.loading = false;
  this.initialized = true;

  this.extScrollParent = null;
  if (this.Options.selfScroller) this.extScrollParent = this.extViewerLayer.down(CSS_LAYER);

  if (this.extScrollParent == null) this.extScrollParent = this.extViewerLayer.up(".x-tabs-item-body");
  if (this.extScrollParent) this.extScrollParent.addListener("scroll", this.atScroll.bind(this));
};

CGListViewer.prototype.showLoading = function () {
  if (this.extViewerLayer == null) return;
  this.extViewerLayer.select(CSS_LOADING).first().dom.style.display = "block";
  this.loading = true;
  //this.extViewerLayer.select(CSS_ITEMS).first().dom.style.display = "none";
};

CGListViewer.prototype.hideLoading = function () {
  if (this.extViewerLayer == null) return;
  this.extViewerLayer.select(CSS_LOADING).first().dom.style.display = "none";
  this.loading = false;
  //this.extViewerLayer.select(CSS_ITEMS).first().dom.style.display = "block";
};

CGListViewer.prototype.addItemsToDOMSelector = function (DOMSelector, Options) {
  for (var index in Options) {
    if (isFunction(Options[index])) continue;
    var Option = Options[index];
    var DOMOption = document.createElement('option');

    DOMOption.value = listViewerHtmlDecode(Option.Code);
    DOMOption.text = listViewerHtmlDecode(Option.Label);
    DOMOption.name = listViewerHtmlDecode(Option.Command);

    try {
      DOMSelector.add(DOMOption, null);
    }
    catch (ex) {
      DOMSelector.add(DOMOption);
    }
  }
};

CGListViewer.prototype.isItemOnSelection = function (CodeList, CodeItem) {
  if (CodeList == LIST_ADD) return false;
  return (this.State.Lists[CodeList].Items[CodeItem] != null);
};

CGListViewer.prototype.addItemOnSelection = function (CodeList, Item) {
  var AuxItem = this.State.Lists[CodeList].Items[Item.Code];

  if (AuxItem) {
    Item.Order = AuxItem.Order;
    this.State.Lists[CodeList].Items[Item.Code] = Item;
  }
  else {
    this.State.Lists[CodeList].Orders.push(Item.Code);
    Item.Order = this.State.Lists[CodeList].Orders.length - 1;
    this.State.Lists[CodeList].Items[Item.Code] = Item;
  }
};

CGListViewer.prototype.removeItemOnSelection = function (CodeList, CodeItem) {
  var sResult = this.State.Lists[CodeList].Orders.join(",");
  var iPos = sResult.indexOf(CodeItem);
  var aItems = new Array();

  if (iPos != -1) {
    if (iPos > 0) iPos = iPos - 1;
    sResult = sResult.substring(0, iPos) + sResult.substring(iPos + CodeItem.length + 1);
  }

  this.State.Lists[CodeList].Orders = (sResult.length > 0) ? sResult.split(",") : new Array();
  for (var i = 0; i < this.State.Lists[CodeList].Orders.length; i++) {
    var CodeItem = this.State.Lists[CodeList].Orders[i];
    aItems[CodeItem] = this.State.Lists[CodeList].Items[CodeItem];
    aItems[CodeItem].Order = i;
  }

  this.State.Lists[CodeList].Items = aItems;
};

CGListViewer.prototype.renderList = function (List, CodeList, ListStyle) {
  var extGroup = this.extWizardLayer.select(CSS_GROUP + "." + CodeList).first();

  if (List == null) {

    if (CodeList == LIST_FOLDER) {
      this.extFolderListSelector.dom.style.display = "none";
      return;
    }

    var extElement = extGroup;
    if (CodeList == LIST_GROUPBY) extElement = this.extWizardLayer.select(ListStyle).first();
    extElement.dom.style.display = "none";

    return;
  }

  List.bUpdate = true;
  this.updateList(List, CodeList, ListStyle, (CodeList == LIST_ADD) ? "" : ",&nbsp;");
};

CGListViewer.prototype.renderFilter = function () {
  this.extFilter = this.extWizardLayer.select(CSS_FILTER).first();
  Event.observe(this.extFilter.dom, "keyup", CGListViewer.prototype.atFilterKeyUp.bind(this));
  Event.observe(this.extFilter.dom, "change", CGListViewer.prototype.atFilterChange.bind(this));
  Event.observe(this.extFilter.dom, "focus", CGListViewer.prototype.atFilterFocus.bind(this));
  Event.observe(this.extFilter.dom, "blur", CGListViewer.prototype.atFilterBlur.bind(this));
  this.extFilterEmpty = this.extWizardLayer.select(CSS_FILTER_EMPTY).first();
  if (this.extFilterEmpty == null) {
    new Insertion.After(this.extFilter.dom, aListViewerTemplates[this.language]["WIZARD_FILTER_EMPTY"]);
    this.extFilterEmpty = this.extWizardLayer.select(CSS_FILTER_EMPTY).first();
  }
  Event.observe(this.extFilterEmpty.dom, "click", CGListViewer.prototype.atFilterEmptyClick.bind(this));
  this.extFilterByTitle = this.extWizardLayer.select(CSS_FILTER_BY_TITLE).first();
  Event.observe(this.extFilterByTitle.dom, "change", CGListViewer.prototype.atFilterByTitleChange.bind(this));
};

CGListViewer.prototype.renderPaging = function () {

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

CGListViewer.prototype.render = function (sViewerLayer) {

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

  this.extFolderListSelector = this.extViewerLayer.select(CSS_FOLDERLIST + " " + CSS_SELECTOR + " select").first();
  Event.observe(this.extFolderListSelector.dom, "change", CGListViewer.prototype.atFolderClick.bind(this, this.extFolderListSelector.dom));

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
  this.renderList(this.Options.FolderList, LIST_FOLDER, CSS_FOLDERLIST);
  this.renderList(this.Options.SortByList, LIST_SORTBY, CSS_SORTBYLIST);
  this.renderList(this.Options.GroupByList, LIST_GROUPBY, CSS_GROUPBYLIST);
  this.renderFilter();
  this.renderPaging();

  CommandListener.capture(this.extViewerLayer.dom);
  if (this.extWizardLayer != null) CommandListener.capture(this.extWizardLayer.dom);

  this.applyState();
  this.load(true);
};

CGListViewer.prototype.initState = function () {
  var i;

  this.State = new Object();
  this.State.CurrentPage = 1;
  this.State.Filter = "";
  this.State.FilterTag = "";
  this.State.Lists = new Object();
  this.State.Lists[LIST_SORTBY] = new Object();
  this.State.Lists[LIST_SORTBY].Orders = new Array();
  this.State.Lists[LIST_SORTBY].Items = new Array();
  this.State.Lists[LIST_GROUPBY] = new Object();
  this.State.Lists[LIST_GROUPBY].Orders = new Array();
  this.State.Lists[LIST_GROUPBY].Items = new Array();
  this.State.Folder = (this.Options.FolderList != null && this.Options.FolderList.Selection != null) ? this.Options.FolderList.Selection : null;
  this.State.Selection = new Array();

  if (this.Options.SortByList && this.Options.SortByList.Selection) {
    for (i = 0; i < this.Options.SortByList.Selection.length; i++) {
      var CodeItem = this.Options.SortByList.Selection[i];
      this.State.Lists[LIST_SORTBY].Items[CodeItem] = {Code: CodeItem, Mode: MODE_ASCENDANT, Order: i};
    }
    this.State.Lists[LIST_SORTBY].Orders = this.Options.SortByList.Selection;

    for (i in this.Options.SortByList.Items) {
      if (isFunction(this.Options.SortByList.Items[i])) continue;
      var ItemOption = this.Options.SortByList.Items[i];
      if (this.State.Lists[LIST_SORTBY].Items[ItemOption.Code] == null) continue;
      if (ItemOption.DefaultMode) this.State.Lists[LIST_SORTBY].Items[ItemOption.Code].Mode = (ItemOption.DefaultMode.toLowerCase() == "descendant") ? MODE_DESCENDANT : MODE_ASCENDANT;
    }
  }

  if (this.Options.GroupByList && this.Options.GroupByList.Selection) {
    for (i = 0; i < this.Options.GroupByList.Selection.length; i++) {
      var aItem = this.Options.GroupByList.Selection[i];
      this.State.Lists[LIST_GROUPBY].Items[aItem[0]] = {Code: aItem[0], Value: aItem[1]};
      this.State.Lists[LIST_GROUPBY].Orders.push(aItem[0]);
    }
  }
};

CGListViewer.prototype.getState = function () {
  var extFilter;
  var Result = new Object();

  extFilter = this.extWizardLayer.select(CSS_FILTER).first();

  if (extFilter == null)
    return;

  Result.Filter = extFilter.dom.value;
  Result.FilterTag = this.State.FilterTag;
  Result.CurrentPage = this.State.CurrentPage;
  Result.Limit = this.itemsPerPage;

  Result.Sorts = new Array();
  for (var i = 0; i < this.State.Lists[LIST_SORTBY].Orders.length; i++) {
    var CodeSort = this.State.Lists[LIST_SORTBY].Orders[i];
    Result.Sorts.push(this.State.Lists[LIST_SORTBY].Items[CodeSort]);
  }

  Result.Groups = new Array();
  for (var i = 0; i < this.State.Lists[LIST_GROUPBY].Orders.length; i++) {
    var CodeGroup = this.State.Lists[LIST_GROUPBY].Orders[i];
    Result.Groups.push(this.State.Lists[LIST_GROUPBY].Items[CodeGroup]);
  }

  Result.Folder = this.State.Folder;
  Result.Selection = this.State.Selection;

  Result.toEscapeUrl = function() {
    var sorts = "";
    for (var i = 0; i < this.Sorts.length; i++) {
      var Sort = this.Sorts[i];
      if (i != 0) sorts += LIST_VIEWER_QUERIES_SEPARATOR;
      sorts += Sort.Code + LIST_VIEWER_QUERY_SEPARATOR + Sort.Mode;
    }

    var groups = "";
    for (var i = 0; i < this.Groups.length; i++) {
      var Group = this.Groups[i];
      if (i != 0) groups += LIST_VIEWER_QUERIES_SEPARATOR;
      groups += Group.Code + LIST_VIEWER_QUERY_SEPARATOR + escape(encodeURIComponent(Group.Value));
    }

    var start = (this.CurrentPage * this.Limit) - this.Limit;

    var result = "&query=" + this.Filter + "&sortsby=" + sorts + "&groupsby=" + groups + ((this.Folder != null) ? "&folder=" + this.Folder : "");
    if (this.FilterTag != null && this.FilterTag != "") result += "&tag=" + this.FilterTag;

    return result + "&start=" + start + "&limit=" + this.Limit;
  };

  return Result;
};

CGListViewer.prototype.setState = function (NewState) {
  if (NewState == null) return;

  //this.State.CurrentPage = NewState.CurrentPage;
  this.State.Filter = NewState.Filter;
  this.State.FilterTag = NewState.FilterTag;

  this.State.Lists[LIST_SORTBY].Items = new Array();
  this.State.Lists[LIST_SORTBY].Orders = new Array();
  for (var i = 0; i < NewState.Sorts.length; i++) {
    var Sort = NewState.Sorts[i];
    Sort.Order = i;
    this.State.Lists[LIST_SORTBY].Items[Sort.Code] = Sort;
    this.State.Lists[LIST_SORTBY].Orders[i] = Sort.Code;
  }
  
  this.State.Lists[LIST_GROUPBY].Items = new Array();
  this.State.Lists[LIST_GROUPBY].Orders = new Array();
  for (var i = 0; i < NewState.Groups.length; i++) {
    var Group = NewState.Groups[i];
    Group.Order = this.State.Lists[LIST_GROUPBY].Orders.length;
    this.State.Lists[LIST_GROUPBY].Items[Group.Code] = Group;
    this.State.Lists[LIST_GROUPBY].Orders[i] = Group.Code;
  }

  this.State.Folder = NewState.Folder;
  this.State.Selection = NewState.Selection;
};

CGListViewer.prototype.applyState = function () {

  this.extFilter.dom.value = this.State.Filter;
  this.extFilterEmpty.dom.style.display = (this.State.Filter.length <= 0) ? "block" : "none";
  this.extFilterByTitle.dom.checked = this.State.FilterTag != null && this.State.FilterTag == "title";

  if (this.State.Folder != null) {
    for (var i = 0; i < this.extFolderListSelector.dom.options.length; i++) {
      var DOMOption = this.extFolderListSelector.dom.options[i];
      if (DOMOption.value == this.State.Folder) DOMOption.selected = true;
    }
  }

  var extSortByList = this.extWizardLayer.select(CSS_SORTBYLIST + " li");
  extSortByList.each(function (extSortByItem) {
    this.unselectSortByItem(extSortByItem);
  }, this);

  for (var i = 0; i < this.State.Lists[LIST_SORTBY].Orders.length; i++) {
    var CodeSort = this.State.Lists[LIST_SORTBY].Orders[i];
    var Sort = this.State.Lists[LIST_SORTBY].Items[CodeSort];
    var extItem;

    if (i == 0) extItem = this.extWizardLayer.select(CSS_SORTBYLIST + " ." + Sort.Code).first();
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
  extGroupByList.each(function (extGroupByItem) {
    this.unselectGroupByItem(extGroupByItem);
  }, this);

  var aDummyGroupsByItems = new Object();
  var aDummyGroupsByOrders = new Array();
  for (var i = 0; i < this.State.Lists[LIST_GROUPBY].Orders.length; i++) {
    var CodeGroup = this.State.Lists[LIST_GROUPBY].Orders[i];
    var Group = this.State.Lists[LIST_GROUPBY].Items[CodeGroup];
    var extItem = this.extWizardLayer.select(CSS_GROUPBYLIST + " ." + Group.Code).first();
    if (extItem == null) continue;
    this.selectGroupByItem(extItem, Group.Label);
    aDummyGroupsByItems[CodeGroup] = Group;
    aDummyGroupsByOrders.push(CodeGroup);
  }
  this.State.Lists[LIST_GROUPBY].Orders = aDummyGroupsByOrders;
  this.State.Lists[LIST_GROUPBY].Items = aDummyGroupsByItems;

  this.extDeleteItems.dom.style.display = "none";
};

CGListViewer.prototype.loadDelayed = function(bClearItems, delay) {
    var component = this;
    this.loadTimeout = window.setTimeout(function() {
        component.load(bClearItems);
    }, delay != null ? delay : 1000);
};

CGListViewer.prototype.clearDelayedTimeout = function() {
    if (this.loadTimeout == null) return;
    window.clearTimeout(this.loadTimeout);
    this.loadTimeout = null;
};

CGListViewer.prototype.load = function (bClearItems) {
  var extItems = this.extViewerLayer.select(CSS_ITEMS).first();
  var sRequestUrl = this.Options.DataSource.RequestUrl;

  if (this.onUpdateState) this.onUpdateState(this.getState());
  if (extItems == null) return;

  if (bClearItems) {
    if (this.loading) return;
    extItems.dom.innerHTML = "";
  }

  this.showLoading();

  var stateData = this.getState().toEscapeUrl();

  if (this.Options.DataSource.Remote) {
    Ext.Ajax.request({
      url: sRequestUrl,
      params: stateData,
      method: "GET",
      callback: function (sOptions, bSuccess, Response) {
        eval("var data = " + Response.responseText.replace(/\n/g, ""));
        this.loading = false;
        this.hideLoading();
        this.update(data, bClearItems);
      },
      scope: this
    });
  }
  else {
    this.loading = false;
    this.hideLoading();
    this.update(this.Options.DataSource.Items, bClearItems);
  }
};

CGListViewer.prototype.loadGroupByOptions = function (GroupBy, DOMSelectorWidget) {
	var sRequestUrl = this.Options.DataSource.RequestGroupByOptionsUrl;

	if (this.Options.DataSource.Remote) {
		Ext.Ajax.request({
			url: sRequestUrl,
			params: "query=" + this.State.Filter + "&code=" + GroupBy.Code,
			method: "GET",
			callback: function (sOptions, bSuccess, Response) {
                var Options = new Array();
				eval(Response.responseText.replace(/\n/g, ""));
				GroupBy.Options = Options;
				this.renderGroupByOptions(GroupBy, DOMSelectorWidget);
			},
			scope: this
		});
	}
};

CGListViewer.prototype.renderGroupByOptions = function(GroupBy, DOMGroupBySelectorWidget) {
	var RecordDefinition = new Ext.data.Record.create({value: 'value'}, {text: 'text'});
	var Store = DOMGroupBySelectorWidget.store;
	var Options = GroupBy.Options;
	var RecordAll = Store.getAt(0);

	Store.removeAll();
	Store.add(RecordAll);
	for (var index in Options) {
		if (isFunction(Options[index])) continue;
		var Option = Options[index];

		Store.add(new RecordDefinition({value: Option.Code, text: Option.Label}));
	}

	var height = 23*(Options.length+1);
    DOMGroupBySelectorWidget.list.setHeight(height > 305 ? 305 : height);
    DOMGroupBySelectorWidget.list.dom.style.overflowY = "auto";
};

CGListViewer.prototype.updateListItem = function (CodeList, Item, DOMItem, width) {

  if (DOMItem != null)
    Event.observe(DOMItem, "click", CGListViewer.prototype.atListItemClick.bind(this, CodeList, DOMItem));

  if (CodeList == LIST_FOLDER)
    this.addItemsToDOMSelector(this.extFolderListSelector.dom, [Item]);
  else if (CodeList == LIST_ADD) {
    this.addItemsToDOMSelector(this.extAddListSelector.dom, [Item]);
    this.extAddListLink.dom.innerHTML = Item.Label;
    this.extAddListLink.dom.CodeItem = Item.Code;
    this.extAddListLink.dom.href = Item.Command;
  }
  else if (CodeList == LIST_GROUPBY) {
    var DOMGroupBySelector = Ext.get(DOMItem).select(CSS_SELECTOR).first().dom;
	var height = DOMGroupBySelector.offsetHeight;

	var DOMGroupBySelectorWidget = new Ext.form.ComboBox({
	  typeAhead: false,
	  triggerAction: 'all',
	  transform: DOMGroupBySelector,
	  width:135,
	  forceSelection:true
	});
	//DOMGroupBySelectorWidget.applyTo(DOMGroupBySelector);

	this.groupByWidgets[Item.Code] = DOMGroupBySelectorWidget;
	DOMGroupBySelectorWidget.setSize(width, height);
	DOMGroupBySelectorWidget.doQuery = CGListViewer.prototype.queryGroupByOptions.bind(DOMGroupBySelectorWidget);

	DOMGroupBySelector.Code = Item.Code;
    DOMGroupBySelectorWidget.Code = Item.Code;
	DOMGroupBySelectorWidget.on("focus", CGListViewer.prototype.atGroupByItemLoadOptions.bind(this, Item, DOMGroupBySelectorWidget));
    DOMGroupBySelectorWidget.on("expand", CGListViewer.prototype.atGroupByItemLoadOptions.bind(this, Item, DOMGroupBySelectorWidget));
	DOMGroupBySelectorWidget.on("beforeselect", CGListViewer.prototype.atGroupByItemOptionBeforeSelect.bind(this));

    //this.updateGroupByOptions(Item, DOMGroupBySelectorWidget, true);
  }
};

CGListViewer.prototype.queryGroupByOptions = function (C, B) {
	if (C === undefined || C === null) {
		C = ""
	}
	var A = {query: C, forceAll: B, combo: this, cancel: false};
	if (this.fireEvent("beforequery", A) === false || A.cancel) {
		return false
	}
	C = A.query;
	B = A.forceAll;
	if (B === true || (C.length >= this.minChars)) {
		if (this.lastQuery != C) {
			this.lastQuery = C;
			if (this.mode == "local") {
				this.selectedIndex = -1;
				if (B) {
					this.store.clearFilter()
				} else {
					this.store.filter(this.displayField, C, true);
				}
				this.onLoad()
			} else {
				this.store.baseParams[this.queryParam] = C;
				this.store.load({params: this.getParams(C)});
				this.expand()
			}
		} else {
			this.selectedIndex = -1;
			this.onLoad()
		}
	}
};

CGListViewer.prototype.updateGroupByOptions = function (GroupBy, DOMSelectorWidget, loadRemote) {
    if (DOMSelectorWidget.store.getCount() > 1)
		return;

    if (GroupBy.Options.length > 0)
        this.renderGroupByOptions(GroupBy, DOMSelectorWidget);
    else if (loadRemote)
        this.loadGroupByOptions(GroupBy, DOMSelectorWidget);
};

CGListViewer.prototype.updateList = function (List, CodeList, ListStyle, sSeparator) {
  var ListItemTemplate = null;
  var extGroup;
  var DOMLayer = null;
  var extList;

  if (this.extWizardLayer == null) return;

  extGroup = this.extWizardLayer.select(CSS_GROUP + "." + CodeList).first();
  if (List == null) {

    if (CodeList == LIST_FOLDER) {
      this.extFolderListSelector.dom.style.display = "none";
      return;
    }

    var extElement = extGroup;
    if (CodeList == LIST_GROUPBY) extElement = this.extWizardLayer.select(ListStyle).first();
    extElement.dom.style.display = "none";
    return;
  }

  if (this.extWizardLayer == null) return;
  if (!List.bUpdate) return;

  extList = this.extWizardLayer.select(ListStyle).first();
  List.Code = CodeList;

  if (CodeList == LIST_GROUPBY) ListItemTemplate = new Template(aListViewerTemplates[this.language]["WIZARD_GROUPBYLIST_ITEM"]);
  else if (CodeList == LIST_SORTBY) ListItemTemplate = new Template(aListViewerTemplates[this.language]["WIZARD_SORTBYLIST_ITEM"]);
  else if (CodeList == LIST_ADD) {
    ListItemTemplate = new Template(aListViewerTemplates[this.language]["WIZARD_ADDLIST_ITEM"]);
    this.extAddListSelector.dom.innerHTML = "<option value='-1'>&nbsp;</option>";
    this.extAddListLink.dom.innerHTML = "";
  }
  else if (CodeList == LIST_FOLDER)
    ListItemTemplate = null;

  if (ListItemTemplate != null) {
    var DOMList = extList.dom;
    DOMList.innerHTML = "";
    DOMLayer = DOMList;
    if (List.Code == LIST_SORTBY) DOMLayer = new Insertion.Bottom(DOMList, aListViewerTemplates[this.language]["WIZARD_SORTBYLIST_SECTION"]).element.immediateDescendants().last();
  }

  List.CountItems = 0;
  var itemWidth = 0;
  for (var Code in List.Items) {
    if (isFunction(List.Items[Code])) continue;
    var Item = List.Items[Code];
    var DOMItem = null;

    if (ListItemTemplate != null) {
      DOMItem = new Insertion.Bottom(DOMLayer, ListItemTemplate.evaluate({"code": Item.Code, "label": Item.Label, "description": Item.Description, "command": Item.Command ? Item.Command : "#"})).element.immediateDescendants().last();
      DOMItem.Code = Item.Code;
    }

    if (itemWidth == 0 && DOMItem != null) itemWidth = DOMItem.offsetWidth-20;
    this.updateListItem(List.Code, Item, DOMItem, itemWidth);
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
  else if (CodeList == LIST_FOLDER)
    this.extFolderListSelector.dom.style.display = (List.CountItems > 1) ? "inline" : "none";

  if (extGroup != null)
    extGroup.dom.style.display = ((List.CountItems > 0) || (CodeList == LIST_GROUPBY)) ? "block" : "none";

  List.bUpdate = false;
};

CGListViewer.prototype.updateLabel = function () {
  var extLabel = this.extViewerLayer.select(CSS_LABEL).first();
  extLabel.dom.style.display = (this.sLabel != null && this.sLabel != "") ? "block" : "none";
  extLabel.dom.innerHTML = this.sLabel;
};

CGListViewer.prototype.updateSummary = function () {
  var CountTemplate = new Template(this.Options.Templates.CountItems ? this.Options.Templates.CountItems : aListViewerTemplates[this.language]["DEFAULT_COUNT_ITEMS"]);
  var extCount = this.extViewerLayer.select(CSS_SUMMARY + " " + CSS_COUNT).first();
  extCount.dom.innerHTML = CountTemplate.evaluate({"count": this.data.nrows});
  extCount.dom.style.display = this.data.nrows == 0 && this.extFolderListSelector.dom.options.length <= 1 ? "none" : "block";
};

CGListViewer.prototype.updateFilters = function () {
  var extFilter = this.extWizardLayer.select(CSS_FILTER).first();
  var extFilters = this.extViewerLayer.select(CSS_FILTERS).first();
  var FilterTemplate = new Template(aListViewerTemplates[this.language]["FILTER"]);
  var extUnselectList, DOMFilter;

  extFilters.dom.innerHTML = "";

  if (extFilter.dom.value != "") {
    DOMFilter = new Insertion.Bottom(extFilters.dom, FilterTemplate.evaluate({"code": CODE_WORDS_FILTER, "label": extFilter.dom.value})).element.immediateDescendants().last();
    DOMFilter.Code = CODE_WORDS_FILTER;
  }

  for (var i = 0; i < this.State.Lists[LIST_GROUPBY].Orders.length; i++) {
    var CodeGroup = this.State.Lists[LIST_GROUPBY].Orders[i];
    var Group = this.State.Lists[LIST_GROUPBY].Items[CodeGroup];
	var Widget = this.groupByWidgets[CodeGroup];

	if (Widget == null) continue;
    if (Widget.getValue() == null) continue;
    if (Widget.getValue() == "all") continue;

    if (extFilters.dom.innerHTML == "") extFilters.dom.innerHTML = aListViewerTemplates[this.language]["FILTERS_MESSAGE"];
    DOMFilter = new Insertion.Bottom(extFilters.dom, FilterTemplate.evaluate({"code": CodeGroup, "label": Group.Label})).element.immediateDescendants().last();
    DOMFilter.Code = CodeGroup;
  }

  extUnselectList = extFilters.select(CSS_UNSELECT);
  extUnselectList.each(function (extUnselect) {
    var extFilter = extUnselect.up(".filter");
    extUnselect.dom.Code = extFilter.dom.Code;
    Event.observe(extUnselect.dom, "click", CGListViewer.prototype.atUnselectFilter.bind(this, extUnselect.dom));
  }, this);
};

CGListViewer.prototype.addItemsGroup = function (extContainer, group) {
  var ContentTemplate = new Template(aListViewerTemplates[this.language]["GROUP_ITEM"]);
  var sContent = ContentTemplate.evaluate(this.Options.DataSource.Groups[group]);
  new Insertion.Bottom(extContainer.dom, sContent);
};

CGListViewer.prototype.updateItems = function (bClearItems) {
  var extItems = this.extViewerLayer.select(CSS_ITEMS).first();
  var ItemTemplate = new Template(aListViewerTemplates[this.language]["ITEM"]);
  var sContentTemplate = this.Options.Templates.Item ? listViewerHtmlDecode(this.Options.Templates.Item) : null;
  var sNoItemsTemplate = this.Options.Templates.NoItems ? listViewerHtmlDecode(this.Options.Templates.NoItems) : null;
  var ContentTemplate, NoItemsTemplate;

  if (this.data == null) return;

  if (sContentTemplate == null) sContentTemplate = aListViewerTemplates[this.language]["DEFAULT_ITEM_CONTENT"];
  ContentTemplate = new Template(sContentTemplate);

  if (sNoItemsTemplate == null) sNoItemsTemplate = aListViewerTemplates[this.language]["DEFAULT_NO_ITEMS"];
  NoItemsTemplate = new Template(sNoItemsTemplate);

  this.extSelectAll.dom.style.visibility = (this.data.nrows > 0) ? "visible" : "hidden";
  this.iNumPages = (this.data.nrows <= this.itemsPerPage) ? 0 : Math.floor(this.data.nrows / this.itemsPerPage);
  if ((this.data.nrows % this.itemsPerPage) != 0) this.iNumPages++;

  if (this.data.nrows == 0) {
    extItems.dom.innerHTML = NoItemsTemplate.evaluate();
    return;
  }

  if ((this.data.rows.length == 0) && (this.State.CurrentPage >= 2)) {
    this.State.CurrentPage--;
    this.load(bClearItems);
    return;
  }

  var IdActiveItem = (this.State.ActiveItemId != null) ? this.State.ActiveItemId : null;
  if (IdActiveItem == null) IdActiveItem = (this.DOMActiveItem != null) ? this.DOMActiveItem.Id : null;

  var currentGroup = null;
  for (var i = 0; i < this.data.rows.length; i++) {
    var Item = this.data.rows[i];
    Item.position = i;

    if (this.onBoundItem) this.onBoundItem(this, Item);

    if (Item.group != null && currentGroup != Item.group) {
      this.addItemsGroup(extItems, Item.group);
      currentGroup = Item.group;
    }

    var sContent = ContentTemplate.evaluate(Item);
    var extItem = Ext.get(new Insertion.Bottom(extItems.dom, ItemTemplate.evaluate({"content": sContent, "imagesPath": this.imagesPath})).element.immediateDescendants().last());
    var extContent = extItem.select(CSS_CONTENT).first();
    var extSelector = extItem.select(CSS_SELECTOR).first();
    var extDelete = extItem.select(CSS_DELETE).first();
    var extLocked = extItem.select(CSS_LOCKED).first();

    Item.id = (Item.id != null) ? Item.id : i;
    extItem.dom.Id = Item.id;
    extItem.dom.addClassName(Item.id);

    Event.observe(extContent.dom, "click", CGListViewer.prototype.atItemContentClick.bind(this, extItem.dom, extContent.dom, i, this.data.nrows));
    Event.observe(extSelector.dom, "click", CGListViewer.prototype.atItemSelectorClick.bind(this, extItem.dom, extSelector.dom));
    Event.observe(extDelete.dom, "click", CGListViewer.prototype.atItemDeleteClick.bind(this, extItem.dom, extDelete.dom));

    if (Item.locked) {
      extLocked.dom.style.display = "block";
      extDelete.dom.style.display = "none";
      extSelector.dom.disabled = true;
    }

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

    if (this.onRenderItem) this.onRenderItem(this, Item, extItem.dom);
  }

};

CGListViewer.prototype.updateOperations = function (bClearItems) {

  if (this.Options.Operations == null)
    return;

  var OperationTemplate = new Template(aListViewerTemplates[this.language]["OPERATION"]);
  var extOperations = this.extViewerLayer.select(CSS_OPERATIONS).first();
  extOperations.dom.innerHTML = "";
  for (var i = 0; i < this.Options.Operations.length; i++) {
    var operation = this.Options.Operations[i];

    if (this.onBoundOperation) this.onBoundOperation(this, operation);

    var extOperation = Ext.get(new Insertion.Bottom(extOperations.dom, OperationTemplate.evaluate({"label": operation.label, "visible": operation.visible})).element.immediateDescendants().last());
    var extLink = extOperation.down("a");

    extLink.dom.name = operation.name;
    Event.observe(extLink.dom, "click", CGListViewer.prototype.atOperationClick.bind(this, extOperation.dom, extLink.dom));
  }
};

CGListViewer.prototype.updatePaging = function () {

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

CGListViewer.prototype.update = function (data, bClearItems) {
  if (this.extViewerLayer == null) return;
  if (this.extWizardLayer == null) return;

  this.data = data;
  if (bClearItems) this.items = new Array();

  for (var i = 0; i < this.data.rows.length; i++)
    this.items[this.data.rows[i].id] = this.data.rows[i];

  this.updateList(this.Options.AddList, LIST_ADD, CSS_ADDLIST, "");
  this.updateList(this.Options.FolderList, LIST_FOLDER, CSS_FOLDERLIST, "");
  this.updateList(this.Options.SortByList, LIST_SORTBY, CSS_SORTBYLIST, ",&nbsp;");
  this.updateList(this.Options.GroupByList, LIST_GROUPBY, CSS_GROUPBYLIST, ",&nbsp;");
  this.updateLabel();
  this.updateSummary();
  this.updateFilters();
  this.updateItems(bClearItems);
  this.updateOperations();
  this.updatePaging();

  this.isRefreshing = false;
  if (this.onRefresh) this.onRefresh(this);

  this.background = false;
};

CGListViewer.prototype.refresh = function () {
  if (this.isRefreshing) return;
  this.isRefreshing = true;
  this.applyState();
  this.load(true);
};

CGListViewer.prototype.refreshBackground = function () {
  this.background = true;
  this.refresh();
};

CGListViewer.prototype.unselectSortByItem = function (extItem) {
  var extSortByOptions = extItem.select(CSS_SORTBYOPTIONS).first();
  extItem.removeClass(CLASS_SELECTED);
  extItem.removeClass(CLASS_ASCENDANT);
  extItem.removeClass(CLASS_DESCENDANT);
  extItem.addClass(CLASS_ASCENDANT);
  if (extSortByOptions != null) extSortByOptions.dom.style.display = "none";
};

CGListViewer.prototype.removeSortByListSectionsFromItem = function (extItem) {
  var extSections = this.extWizardLayer.select(CSS_LIST_SECTION);
  var extLastSection;

  for (var i = 0; i < extSections.getCount(); i++) {
    var CodeSort = this.State.Lists[LIST_SORTBY].Orders[i];
    if (CodeSort == null) {
      var extSection = extSections.elements[i];
      if (extSection) extSection.remove();
    }
  }

  extSections = this.extWizardLayer.select(CSS_LIST_SECTION);
  extLastSection = extSections.last();
  if (extLastSection) extLastSection.removeClass(CLASS_READONLY);
  this.extMoreSortByList.dom.style.display = (extSections.getCount() == this.Options.SortByList.CountItems) ? "none" : "block";
};

CGListViewer.prototype.createSortByListSection = function () {
  var DOMList = this.extWizardLayer.select(CSS_SORTBYLIST).first().dom;
  var extSections = Ext.get(DOMList).select(CSS_LIST_SECTION);
  var iNumSections = extSections.getCount();
  var extLastSection = extSections.last();
  var extLayer = Ext.get(new Insertion.Bottom(DOMList, aListViewerTemplates[this.language]["WIZARD_SORTBYLIST_SECTION"]).element.immediateDescendants().last());
  var ListItemTemplate = new Template(aListViewerTemplates[this.language]["WIZARD_SORTBYLIST_ITEM"]);

  if (extLastSection) extLastSection.addClass(CLASS_READONLY);
  this.extMoreSortByList.dom.style.display = "none";

  var extSortByOptions = extLayer.select(CSS_SORTBYOPTIONS).first();
  if (extSortByOptions == null) {
    new Insertion.Bottom(extLayer.dom, new Template(aListViewerTemplates[this.language]["WIZARD_SORTBYLIST_SECTION_OPTIONS"]).evaluate());
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

CGListViewer.prototype.removeSortByListSection = function (DOMSection) {
  DOMSection.remove();

  var extLastSection = this.extWizardLayer.select(CSS_LIST_SECTION).last();
  if (extLastSection) {
    extLastSection.removeClass(CLASS_READONLY);
    var extSelectedItem = extLastSection.select(CSS_SELECTED).first();
    this.extMoreSortByList.dom.style.display = (extSelectedItem != null) ? "block" : "none";
  }
  else this.extMoreSortByList.dom.style.display = "block";
};

CGListViewer.prototype.selectSortByItem = function (extItem) {
  this.removeSortByListSectionsFromItem(extItem);
  if (!extItem.hasClass(CLASS_SELECTED)) extItem.addClass(CLASS_SELECTED);
  if (extItem.hasClass(CLASS_READONLY)) extItem.removeClass(CLASS_READONLY);
};

CGListViewer.prototype.toggleSortByItemMode = function (extItem) {
  var DOMItem = extItem.dom;
  var Sort = this.State.Lists[LIST_SORTBY].Items[DOMItem.Code];

  DOMItem.toggleClassName(CLASS_ASCENDANT);
  DOMItem.toggleClassName(CLASS_DESCENDANT);
  Sort.Mode = DOMItem.hasClassName(CLASS_ASCENDANT) ? MODE_ASCENDANT : MODE_DESCENDANT;
};

CGListViewer.prototype.unselectGroupByItem = function (extItem) {
  var Widget = this.groupByWidgets[extItem.dom.Code];
  if (Widget != null)
    Widget.setValue("all");
};

CGListViewer.prototype.selectGroupByItem = function (extItem, sValue) {
  var Widget = this.groupByWidgets[extItem.dom.Code];
  if (Widget != null)
    Widget.setValue(sValue);
};

CGListViewer.prototype.clearFilter = function () {
  this.State.Filter = "";
  this.extFilter.dom.value = "";
  this.extFilterEmpty.dom.style.display = "block";
  this.load(true);
};

CGListViewer.prototype.filter = function() {
    this.extFilterEmpty.dom.style.display = (this.extFilter.dom.value.length <= 0) ? "block" : "none";
  if (this.State.Filter != null && this.State.Filter == this.extFilter.dom.value) return;
  this.State.Filter = this.extFilter.dom.value;
  this.load(true);
};

CGListViewer.prototype.addItem = function (Item) {
  if (this.onAddItem) this.onAddItem(this, Item);
};

CGListViewer.prototype.sortByItem = function (DOMItem) {
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

  this.addItemOnSelection(LIST_SORTBY, {Code: DOMItem.Code, Mode: DOMItem.hasClassName(CLASS_ASCENDANT) ? MODE_ASCENDANT : MODE_DESCENDANT});
  this.selectSortByItem(Ext.get(DOMItem));
  this.load(true);
};

CGListViewer.prototype.clearSelection = function () {
  this.unselectAll();
};

CGListViewer.prototype.getSelectedItems = function () {
  var aExtSelectorItems = this.extViewerLayer.select(CSS_ITEM_SELECTOR);
  var aResult = new Array();

  aExtSelectorItems.each(function (extItemSelector) {
    if (!extItemSelector.dom.checked) return;
    var DOMItem = extItemSelector.dom.up(CSS_ITEM);
    aResult[DOMItem.Id] = DOMItem.Id;
  }, this);

  return aResult;
};

CGListViewer.prototype.getItem = function (Id) {
  for (var i in this.items) {
    if (isFunction(this.items[i])) continue;
    if (this.items[i].id == Id) {
      return this.items[i];
    }
    ;
  }
  return null;
};

CGListViewer.prototype.updateItem = function (Item) {
  var sContentTemplate = this.Options.Templates.Item ? listViewerHtmlDecode(this.Options.Templates.Item) : null;
  var ContentTemplate = null;
  var DOMItem = this.getDOMItem(Item.id);

  for (var i in this.items) {
    if (isFunction(this.items[i])) continue;
    if (this.items[i].id == Item.id) {
      this.items[i] = Item;
      break;
    }
    ;
  }

  if (this.onBoundItem) this.onBoundItem(this, Item);

  if (sContentTemplate == null) sContentTemplate = aListViewerTemplates[this.language]["DEFAULT_ITEM_CONTENT"];
  ContentTemplate = new Template(sContentTemplate);

  var extItem = Ext.get(DOMItem);
  var sContent = ContentTemplate.evaluate(Item);
  var extContent = extItem.select(CSS_CONTENT).first();
  extContent.dom.innerHTML = sContent;

  if (Item.css)
    extContent.dom.addClassName(Item.css);
};

CGListViewer.prototype.getDOMItem = function (Id) {
  var extItems = this.extViewerLayer.select(CSS_ITEMS).first();
  var extItemList = extItems.select(CSS_ITEM);
  var DOMResult = null;

  extItemList.each(function (extItem) {
    if (extItem.dom.Id == Id) DOMResult = extItem.dom;
  }, this);

  return DOMResult;
};

CGListViewer.prototype.getActiveItem = function () {
  if (this.DOMActiveItem == null) return null;

  return this.getItem(this.DOMActiveItem.Id);
};

CGListViewer.prototype.setActiveItem = function (Id) {
  this.State.ActiveItemId = Id;
};

CGListViewer.prototype.activateItem = function (Id) {
  var DOMItem = this.getDOMItem(Id);

  if (DOMItem == null) return;

  var extItem = Ext.get(DOMItem);
  var extContent = extItem.select(CSS_CONTENT).first();
  extContent.dom.click();
};

CGListViewer.prototype.selectAll = function () {
  var aExtSelectorItems = this.extViewerLayer.select(CSS_ITEM_SELECTOR);
  var aItems = new Array();
  aExtSelectorItems.each(function (extItemSelector) {
    if (extItemSelector.dom.disabled) return;
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

CGListViewer.prototype.unselectAll = function () {
  var aExtSelectorItems = this.extViewerLayer.select(CSS_ITEM_SELECTOR);
  var aItems = new Array();
  aExtSelectorItems.each(function (extItemSelector) {
    if (extItemSelector.dom.disabled) return;
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

CGListViewer.prototype.firstPage = function () {
  this.State.CurrentPage = 1;
  this.load(false);
};

CGListViewer.prototype.previousPage = function () {
  this.State.CurrentPage--;
  if (this.State.CurrentPage < 1) this.State.CurrentPage = 1;
  this.load(false);
};

CGListViewer.prototype.nextPage = function () {
  this.State.CurrentPage++;

  if (this.State.CurrentPage > this.iNumPages) {
    this.State.CurrentPage = this.iNumPages;
    return;
  }

  this.load(false);
};

CGListViewer.prototype.lastPage = function () {
  this.State.CurrentPage = this.iNumPages;
  this.load(false);
};

CGListViewer.prototype.setAddList = function (AddList) {
  this.Options.AddList = AddList;
  this.Options.AddList.bUpdate = true;
};

CGListViewer.prototype.setSortByList = function (SortByList) {
  this.Options.SortByList = SortByList;
  this.Options.SortByList.bUpdate = true;
};

CGListViewer.prototype.setGroupByList = function (GroupByList) {
  this.Options.GroupByList = GroupByList;
  this.Options.GroupByList.bUpdate = true;
};

CGListViewer.prototype.setDataSourceUrls = function (itemsUrl, groupByOptionsUrl) {
  this.Options.DataSource.Remote = true;
  this.Options.DataSource.RequestUrl = itemsUrl;
  this.Options.DataSource.RequestGroupByOptionsUrl = groupByOptionsUrl;
};

CGListViewer.prototype.dispose = function () {
  if (this.extFilter.dom == null || this.extFirst.dom == null ||
      this.extPrevious.dom == null || this.extNext.dom == null ||
      this.extLast.dom == null)
    return;

  Event.stopObserving(this.extFilter.dom, "keyup", CGListViewer.prototype.atFilterKeyUp.bind(this));
  Event.stopObserving(this.extFilter.dom, "change", CGListViewer.prototype.atFilterChange.bind(this));
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

CGListViewer.prototype.atAddItem = function (DOMElement, EventLaunched) {
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

CGListViewer.prototype.atFolderClick = function (DOMElement, EventLaunched) {
  var DOMOption = DOMElement.options[DOMElement.selectedIndex];

  this.State.Folder = DOMOption.value;
  this.load(true);

  Event.stop(EventLaunched);
  return false;
};

CGListViewer.prototype.atDeleteItems = function () {
  if (this.onDeleteItems) this.onDeleteItems(this, this.getSelectedItems());
  this.extSelectAll.dom.checked = false;
};

CGListViewer.prototype.atListItemClick = function (CodeList, DOMItem, EventLaunched) {
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

CGListViewer.prototype.atGroupByItemLoadOptions = function (GroupBy, DOMSelectorWidget) {
    if (this.loadTimeout != null) {
        this.clearDelayedTimeout();
        this.loadDelayed(true, 5000);
    }
    this.updateGroupByOptions(GroupBy, DOMSelectorWidget, true);
};

CGListViewer.prototype.atGroupByItemOptionBeforeSelect = function (DOMSelectorWidget, Record, index) {
  this.State.CurrentPage = 1;

  var value = Record.data.value;
  var label = Record.data.text;
  if (value == "all") this.removeItemOnSelection(LIST_GROUPBY, DOMSelectorWidget.Code);
  else this.addItemOnSelection(LIST_GROUPBY, {Code: DOMSelectorWidget.Code, Value: value, Label: label});

  this.clearDelayedTimeout();
  this.loadDelayed(true);

  return true;
};

CGListViewer.prototype.atSortBySectionUnselectClick = function (DOMSection, EventLaunched) {
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

    this.extFilterEmpty.dom.style.display = (sFilter.length <= 0) ? "block" : "none";

    if ((codeKey == KEY_UP) || (codeKey == KEY_DOWN) || (codeKey == KEY_ENTER) || (codeKey == KEY_LEFT) || (codeKey == KEY_RIGHT) || (codeKey == KEY_CONTROL) || (codeKey == KEY_SHIFT)) return;
    if (sFilter != "" && sFilter.length < 3) return;

    window.clearTimeout(this.idTimeoutFilter);
    this.idTimeoutFilter = window.setTimeout(this.filter.bind(this), 500);
};

CGListViewer.prototype.atFilterChange = function (oEvent) {
    var sFilter = this.extFilter.dom.value;
    if (sFilter != "" && sFilter.length < 3) return;
    this.filter();
};

CGListViewer.prototype.atFilterByTitleChange = function (oEvent) {
    var checked = this.extFilterByTitle.dom.checked;
    this.State.FilterTag = checked ? "title" : "";
    if (this.State.Filter == "") return;
    this.load(true);
};

CGListViewer.prototype.atFilterFocus = function () {
  this.extFilterEmpty.dom.style.display = "none";
  this.extFilter.dom.select();
};

CGListViewer.prototype.atFilterBlur = function () {
  var sValue = this.extFilter.dom.value;
  this.extFilterEmpty.dom.style.display = (sValue.length <= 0) ? "block" : "none";
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

CGListViewer.prototype.atItemContentClick = function (DOMItem, DOMContent, index, nrows, EventLaunched) {

  if (this.DOMActiveItem != null) this.DOMActiveItem.removeClassName(CLASS_ACTIVE);

  this.DOMActiveItem = DOMItem;
  this.DOMActiveItem.addClassName(CLASS_ACTIVE);

  if (this.Options.Templates.ShowItemCommand != null && !this.background) {
    var CommandTemplate = new Template(this.Options.Templates.ShowItemCommand);
    CommandListener.dispatchCommand(CommandTemplate.evaluate({"id": DOMItem.Id,"index":index,"count":nrows}));
    return false;
  }

  var Item = null;
  for (var i in this.items) {
    if (isFunction(this.items[i])) continue;
    if (this.items[i].id == DOMItem.Id) {
      Item = this.items[i];
      break;
    }
  }

  if (this.onShowItem) this.onShowItem(this, Item);
};

CGListViewer.prototype.atItemSelectorClick = function (DOMItem, DOMSelector, EventLaunched) {
  if (DOMSelector.checked) {
    DOMItem.addClassName(CLASS_SELECTED);
    if (this.onSelectItem) this.onSelectItem(this, DOMItem.Id);
  }
  else {
    DOMItem.removeClassName(CLASS_SELECTED);
    if (this.onUnselectItem) this.onUnselectItem(this, DOMItem.Id);
  }
  this.State.Selection = this.getSelectedItems();
  this.extDeleteItems.dom.style.display = (this.State.Selection.size() > 0) ? "block" : "none";
};

CGListViewer.prototype.atItemDeleteClick = function (DOMItem, DOMDelete, EventLaunched) {
  if (this.onDeleteItem) this.onDeleteItem(this, DOMItem.Id);
  Event.stop(EventLaunched);
};

CGListViewer.prototype.atOperationClick = function (DOMOperation, DOMLink, EventLaunched) {
  if (this.onOperationClick) this.onOperationClick(this, DOMLink.name);
};

CGListViewer.prototype.atUnselectFilter = function (DOMUnselect) {
  if (DOMUnselect.Code == CODE_WORDS_FILTER) {
    this.clearFilter();
  }
  else {
    var extItem = this.extWizardLayer.select(CSS_GROUPBYLIST + " ." + DOMUnselect.Code).first();
    this.removeItemOnSelection(LIST_GROUPBY, extItem.dom.Code);
    this.unselectGroupByItem(extItem);
    this.load(true);
  }
};

CGListViewer.prototype.atMoreSortByList = function () {
  this.createSortByListSection();
};

CGListViewer.prototype.atScroll = function () {
  if (this.extScrollParent.dom.scrollHeight - this.extScrollParent.dom.clientHeight - this.extScrollParent.dom.scrollTop < 200 && !this.loading)
    this.nextPage();
};
